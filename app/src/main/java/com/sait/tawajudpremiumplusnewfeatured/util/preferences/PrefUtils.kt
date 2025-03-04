package com.sait.tawajudpremiumplusnewfeatured.util.preferences

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.util.AppTerminateAlarmReceiver
import com.sait.tawajudpremiumplusnewfeatured.util.AppTerminateJobService
import com.sait.tawajudpremiumplusnewfeatured.util.AppTerminateWorker
import com.sait.tawajudpremiumplusnewfeatured.util.Cryptography_Android
import com.sait.tawajudpremiumplusnewfeatured.util.CustomTypefaceSpan
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.util.UUID

//class PrefUtils //no direct instances allowed. use di instead.
//@Inject
object PrefUtils {
    //preference file
    private const val DEFAULT_PREFS = "default_shared_prefs"

    //any numeric getter method will return -1 as default value
    private const val DEFAULT_NUMERIC_VALUE = -1

    //any string getter method will return empty string as default value
    private const val DEFAULT_STRING_VALUE = ""

    fun extractFileFromUri(contextCompat: Context,uri: Uri): File? {
        return try {
            // Verify if the URI points to a valid resource
            val cursor = contextCompat.contentResolver.query(uri, null, null, null, null)
            if (cursor == null || !cursor.moveToFirst()) {
                Log.e("CameraX", "No media found for URI: $uri")
                return null
            }
            cursor.close()

            // Use content resolver to open an InputStream
            val inputStream = contextCompat.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("CameraX", "InputStream is null for URI: $uri")
                return null
            }


            val file = File(contextCompat.cacheDir, "extracted_image_${System.currentTimeMillis()}.jpg")
            // Write the InputStream to a file
            inputStream.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            if (file.exists()) {
                Log.d("CameraX", "File created successfully: ${file.absolutePath}")
            } else {
                Log.e("CameraX", "Failed to create file: ${file.absolutePath}")
            }

            file // Return the extracted file
        } catch (e: Exception) {
            Log.e("CameraX", "Error extracting file from URI: ${e.message}", e)
            null
        }
    }
     fun parseDoubleSafely(input: String): Double {
        return try {
            input.toDouble()
        } catch (e: NumberFormatException) {
            Log.e("ParsingError", "Invalid number format: $input", e)
            0.0 // Return a default value or handle the error appropriately
        }
    }
     fun isSpoofed(location: Location,context: Context): Boolean {
        return isMockLocation(location,context) ||
                isUnrealisticSpeed(location) ||
                isSuddenJump(location) ||
                isLocationSourceSuspicious(location)
    }

    // 1. Check for Mock Location
    private fun isMockLocation(location: Location, context: Context): Boolean {
        return when {
            // Android 12+ (API 31): Use the built-in `isMock` flag
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> location.isMock

            // Android < 12: Check if "Allow mock locations" is enabled in Developer Options
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
                try {
                    val isDevSettingsEnabled = Settings.Secure.getInt(
                        context.contentResolver,
                        Settings.Secure.ALLOW_MOCK_LOCATION
                    ) != 0
                    isDevSettingsEnabled
                } catch (e: Exception) {
                    false
                }
            }

            else -> false
        }
    }
    fun getStableDeviceId(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        if (!androidId.isNullOrBlank() && androidId != "9774d56d682e549c") {
            return formatAsUUID(androidId)
        }

        // Fallback: Use a hashed combination of other stable hardware identifiers
        val hardwareId = "${Build.BOARD}${Build.BRAND}${Build.DEVICE}${Build.HARDWARE}${Build.MANUFACTURER}${Build.MODEL}${Build.PRODUCT}"

        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = digest.digest(hardwareId.toByteArray())
            val hexString = bytes.joinToString("") { "%02x".format(it) }
            formatAsUUID(hexString.take(32)) // Ensuring it’s the right length for UUID
        } catch (e: Exception) {
            UUID.randomUUID().toString() // As a last resort
        }
    }

    // Helper function to convert a hex string into a UUID format
    private fun formatAsUUID(input: String): String {
        return try {
            val uuid = UUID.nameUUIDFromBytes(input.toByteArray())
            uuid.toString()
        } catch (e: Exception) {
            UUID.randomUUID().toString()
        }
    }

    // 2. Check for Unrealistic Speed
    private fun isUnrealisticSpeed(location: Location): Boolean {
        val MAX_SPEED_THRESHOLD = 80f // 80 m/s (288 km/h)
        return location.speed > MAX_SPEED_THRESHOLD
    }

    // 3. Check for Sudden Large Location Jumps
    private fun isSuddenJump(location: Location): Boolean {
        location?.let {
            val MAX_DISTANCE_THRESHOLD = 5000f // 5 km sudden jump
            val distance = it.distanceTo(location)
            val timeDifference = (location.time - it.time) / 1000 // in seconds
            if (timeDifference > 0) {
                val speed = distance / timeDifference
                return speed > 150 // More than 150 m/s is unrealistic
            }
        }
        return false
    }

    // 4. Check for Location Source Suspicion
    private fun isLocationSourceSuspicious(location: Location): Boolean {
        return location.provider != LocationManager.FUSED_PROVIDER
    }

    public fun isVpnActive(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
    fun buildRequestLog(request: Request): String {
        val sb = StringBuilder()
        sb.append("→→→→→→→→→→ REQUEST →→→→→→→→→→\n")
        sb.append("URL: ${request.url}\n")
        sb.append("Method: ${request.method}\n")
        sb.append("Headers: \n${request.headers}\n")

        // Check if the request body is JSON and pretty print it
        request.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            val requestBodyStr = buffer.readUtf8()

            // Check if the body is in JSON format and pretty print
            if (isJson(requestBodyStr)) {
                val prettyJson = GsonBuilder().setPrettyPrinting().create().toJson(JsonParser().parse(requestBodyStr))
                sb.append("Body: $prettyJson\n")
            } else {
                sb.append("Body: $requestBodyStr\n")
            }
        }
        sb.append("→→→→→→→→→→ END REQUEST →→→→→→→→→→\n")
        return sb.toString()
    }

    fun buildResponseLog(response: Response, url: HttpUrl, httpUrl: String): String {
        val sb = StringBuilder()
        sb.append("←←←←←←←←←← RESPONSE ←←←←←←←←←←\n")
        sb.append("Url: ${url}\n")
        sb.append("Code: ${response.code}\n")
        sb.append("Message: ${response.message}\n")
        sb.append("Headers: ${response.headers}\n")

        // Pretty print the response body if it's JSON
        if (isJson(httpUrl)) {
            val prettyJson = GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(httpUrl))
            sb.append("Body: $prettyJson\n")
        } else {
            sb.append("Body: $httpUrl\n")
        }
        sb.append("←←←←←←←←←← END RESPONSE ←←←←←←←←←←\n")
        return sb.toString()
    }
    fun setString(key: String?, value: String?) {
        val prefs = TawajudApplication.getInstance().getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun addInWord(context: Context, text: String, reasonEn: String): SpannableString {
        // Split the text into words
        var spannableString : SpannableString? = null
       var spannableStringIN : SpannableString? = null
        var in_out_spannable : SpannableString? = null

        var startIndex :Int?=null
        var endIndex:Int?=null
        // Remove " OUT " from the text
        val cleanedText = text.replace(" OUT ", " ")
        val words = cleanedText.split(" ")
        if (words.size < 2) return SpannableString(text)

        val middleText = " IN " // Add spaces around "IN" to visually center it

        if (!text.contains("IN")) {
            val newText = "${words.first()}$middleText${words.drop(1).joinToString(" ")}"
            spannableStringIN = SpannableString(newText)
            in_out_spannable=SpannableString(middleText)

            // Find the start and end index for "IN"
            val inIndex = middleText.indexOf("IN")
            if (inIndex != -1) {
                startIndex = words.first().length + inIndex // Start index of "IN"
                endIndex = startIndex + "IN".length // End index of "IN"

                // Apply specific color to "IN" (e.g., green color)
                spannableStringIN.setSpan(
                    ForegroundColorSpan(context.getColor(R.color.green)),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Apply text size to "IN" (e.g., 24sp)
                spannableStringIN.setSpan(
                    AbsoluteSizeSpan(22, true), // true means "sp" units (scale-independent)
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // Apply a relative size span to "IN" (e.g., scale relative size)
                in_out_spannable.setSpan(
                    RelativeSizeSpan(2f),
                    0,
                    3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableStringIN.setSpan(
                    ResourcesCompat.getFont(context,R.font.robotocondensed_bold)
                        ?.let { CustomTypefaceSpan(it) },
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            // Apply bold style to "IN"


            return spannableStringIN
        }else{
            spannableString = SpannableString(text)
            startIndex = words.first().length + 1// After "Last "
            endIndex = startIndex + middleText.trim().length// Length of "OUT"
            in_out_spannable=SpannableString(middleText)
            spannableString.setSpan(
                ForegroundColorSpan(context.getColor(R.color.green)),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            in_out_spannable.setSpan(
                RelativeSizeSpan(0.3f),
                0,
                3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Apply text size to "IN" (e.g., 24sp)
            spannableString.setSpan(
                AbsoluteSizeSpan(22, true), // true means "sp" units (scale-independent)
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }


        // Apply bold style to "IN"
        spannableString.setSpan(
            ResourcesCompat.getFont(context,R.font.robotocondensed_bold)
                ?.let { CustomTypefaceSpan(it) },
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        return spannableString
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun addOutWord(context: Context, text: String, reasonEn: String): SpannableString {
        // Split the text into words
        var spannableString: SpannableString? = null
        var spannableStringOUT: SpannableString? = null
        var in_out_spannable: SpannableString? = null

        var startIndex: Int? = null
        var endIndex: Int? = null
        // Remove " IN " from the text
        val cleanedText = text.replace(" IN ", " ")
        // Check if "IN" is present and remove it
        val words = cleanedText.split(" ")


        if (words.size < 2) return SpannableString(text)
        // Determine whether to use "IN" or "OUT"
        val middleText = " OUT " // Add spaces around "OUT" to visually center it

        if (!text.contains("OUT")) {
            val newText = "${words.first()}$middleText${words.drop(1).joinToString(" ")}"
            spannableStringOUT = SpannableString(newText)
            in_out_spannable = SpannableString(middleText)

            // Find the start and end index for "OUT"
            val inIndex = middleText.indexOf("OUT")
            if (inIndex != -1) {
                startIndex = words.first().length + inIndex // Start index of "OUT"
                endIndex = startIndex + "OUT".length // End index of "OUT"

                spannableStringOUT.setSpan(
                    ForegroundColorSpan(context.getColor(R.color.red)),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                in_out_spannable.setSpan(
                    RelativeSizeSpan(0.3f),
                    0,
                    4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Apply text size to "OUT" (e.g., 24sp)
                spannableStringOUT.setSpan(
                    AbsoluteSizeSpan(20, true), // true means "sp" units (scale-independent)
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableStringOUT.setSpan(
                    ResourcesCompat.getFont(context, R.font.robotocondensed_bold)
                        ?.let { CustomTypefaceSpan(it) },
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            // Apply bold style to "IN"


            return spannableStringOUT
        } else {
            spannableString = SpannableString(text)
            startIndex = words.first().length + 1// After "Last "
            endIndex = startIndex + middleText.trim().length// Length of "OUT"
            in_out_spannable = SpannableString(middleText)
            spannableString.setSpan(
                ForegroundColorSpan(context.getColor(R.color.red)),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            in_out_spannable.setSpan(
                RelativeSizeSpan(0.3f),
                0,
                4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Apply text size to "OUT" (e.g., 24sp)
            spannableString.setSpan(
                AbsoluteSizeSpan(20, true), // true means "sp" units (scale-independent)
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // Apply bold style to "IN"
            spannableString.setSpan(
                ResourcesCompat.getFont(context, R.font.robotocondensed_bold)
                    ?.let { CustomTypefaceSpan(it) },
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


            return spannableString
        }
    }

    fun getEncriptedKey(): String? {
        try {
            return Cryptography_Android.getHashSha256("saitpksupreme", 32)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getVector(): String {
        return "0804198907081989"
    }
    private fun isJson(body: String): Boolean {
        return try {
            JsonParser().parse(body)
            true
        } catch (e: Exception) {
            false
        }
    }
    fun scheduleAppTerminateJob(context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<AppTerminateWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Only execute when connected to the internet
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "AppTerminateJob",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    fun addInForArabic(context: Context, text: String): SpannableString? {
        var spannableString : SpannableString? = null
        var spannableStringIN : SpannableString? = null
        var in_out_spannable : SpannableString? = null

        var startIndex :Int?=null
        var endIndex:Int?=null


        val cleanedText = text.replace(" خروج", "")
        val words = cleanedText.split(" ")

        val endText = " دخول"
        val inIndex = endText.indexOf("دخول")

        if (!text.contains("دخول")) {

            val newText = "${words.joinToString("")}$endText"
            spannableStringIN = SpannableString(newText)
            in_out_spannable = SpannableString(endText)

            if (inIndex != -1) {
                startIndex = 8 // Start at the index of "دخول"
                endIndex = startIndex + "دخول".length
                if (startIndex != null) {
                    spannableStringIN.setSpan(
                        ForegroundColorSpan(context.getColor(R.color.green)),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                in_out_spannable.setSpan(
                    RelativeSizeSpan(0.3f),
                    0,
                    4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Apply text size to "OUT" (e.g., 24sp)
                if (startIndex != null) {
                    spannableStringIN.setSpan(
                        AbsoluteSizeSpan(20, true), // true means "sp" units (scale-independent)
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                if (startIndex != null) {
                    spannableStringIN.setSpan(
                        ResourcesCompat.getFont(context, R.font.robotocondensed_bold)
                            ?.let { CustomTypefaceSpan(it) },
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            // Apply bold style to "IN"


            return spannableStringIN
        } else {
            spannableString = SpannableString(text)
            startIndex = 8 // Start at the index of "دخول"
            endIndex = startIndex + "دخول".length
            in_out_spannable = SpannableString(endText)
            if (endIndex != null) {
                spannableString.setSpan(
                    ForegroundColorSpan(context.getColor(R.color.green)),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            in_out_spannable.setSpan(
                RelativeSizeSpan(0.3f),
                0,
                4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Apply text size to "OUT" (e.g., 24sp)
            if (endIndex != null) {
                spannableString.setSpan(
                    AbsoluteSizeSpan(20, true), // true means "sp" units (scale-independent)
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // Apply bold style to "IN"
            spannableString.setSpan(
                ResourcesCompat.getFont(context, R.font.robotocondensed_bold)
                    ?.let { CustomTypefaceSpan(it) },
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


            return spannableString
        }
        return null
    }
    fun addOutForArabic(context: Context, text: String): SpannableString? {
        var spannableString : SpannableString? = null
        var spannableStringIN : SpannableString? = null
        var in_out_spannable : SpannableString? = null

        var startIndex :Int?=null
        var endIndex:Int?=null


        val cleanedText = text.replace(" دخول", "")
        val words = cleanedText.split(" ")

        val endText = " خروج"
        val inIndex = endText.indexOf("خروج")

        if (!text.contains("خروج")) {

            val newText = "${words.joinToString("")}$endText"
            spannableStringIN = SpannableString(newText)
            in_out_spannable = SpannableString(endText)

            if (inIndex != -1) {
                startIndex = 8 // Start at the index of "دخول"
                endIndex = startIndex + "دخول".length
                if (startIndex != null) {
                    spannableStringIN.setSpan(
                        ForegroundColorSpan(context.getColor(R.color.red)),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                in_out_spannable.setSpan(
                    RelativeSizeSpan(0.3f),
                    0,
                    4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Apply text size to "OUT" (e.g., 24sp)
                if (startIndex != null) {
                    spannableStringIN.setSpan(
                        AbsoluteSizeSpan(20, true), // true means "sp" units (scale-independent)
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                if (startIndex != null) {
                    spannableStringIN.setSpan(
                        ResourcesCompat.getFont(context, R.font.robotocondensed_bold)
                            ?.let { CustomTypefaceSpan(it) },
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            // Apply bold style to "IN"


            return spannableStringIN
        } else {
            spannableString = SpannableString(text)
            startIndex = 8 // Start at the index of "دخول"
            endIndex = startIndex + "دخول".length
            in_out_spannable = SpannableString(endText)
            if (endIndex != null) {
                spannableString.setSpan(
                    ForegroundColorSpan(context.getColor(R.color.red)),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            in_out_spannable.setSpan(
                RelativeSizeSpan(0.3f),
                0,
                4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Apply text size to "OUT" (e.g., 24sp)
            if (endIndex != null) {
                spannableString.setSpan(
                    AbsoluteSizeSpan(20, true), // true means "sp" units (scale-independent)
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // Apply bold style to "IN"
            spannableString.setSpan(
                ResourcesCompat.getFont(context, R.font.robotocondensed_bold)
                    ?.let { CustomTypefaceSpan(it) },
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


            return spannableString
        }
        return null
    }

    fun getString(key: String?): String? {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(key, DEFAULT_STRING_VALUE)
    }
    fun ensureValidUrlScheme(url: String?): String? {
        if (url.isNullOrEmpty()) return null

        return if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url" // Default to HTTPS if no scheme is provided
        } else {
            url
        }
    }

    fun isUrlReachable(url: String, onResult: (Boolean) -> Unit) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .head() // Use HEAD request to just check for existence
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false) // URL is not reachable
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful && response.code != 404)
            }
        })
    }
    fun setStringWithContext(context: Context, key: String?, value: String?) {
        val prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringWithContext(context: Context, key: String?): String? {
        val prefs = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(key, DEFAULT_STRING_VALUE)
    }
    fun cancelJob(context: Context, jobId: Int) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(jobId) // Cancel only the job with the specified jobId
    }
    fun cancelLocationJob(context: Context,jobId :Int) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
    }
    fun cancelAppTerminateAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AppTerminateAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
    fun setBoolean(key: String?, value: Boolean) {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String?): Boolean {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }

    fun setLong(key: String?, value: Long) {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String?): Long {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return prefs.getLong(key, DEFAULT_NUMERIC_VALUE.toLong())
    }

    fun setInteger(key: String?, value: Int) {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInteger(key: String?): Int {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(key, DEFAULT_NUMERIC_VALUE)
    }

    fun setFloat(key: String?, value: Float) {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun getFloat(key: String?): Float {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return prefs.getFloat(key, DEFAULT_NUMERIC_VALUE as Float)
    }

    /**
     * to set pojo object in preferences. will store json string of it.
     */
    fun setObject(key: String?, value: Any) {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, Gson().toJson(value))
        editor.apply()
    }

    /**
     * to get pojo object from json stored in preferences.
     * returns null if key doesn't exist in preferences
     */
    fun <T> getObject(key: String?, pojoClass: Class<T>?): T? {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(key, null) ?: return null
        return Gson().fromJson(jsonString, pojoClass)
    }

    /**
     * fetches all key-value pairs from preferences in the form of map
     */
    val all: Map<String, *>
        get() {
            val prefs = TawajudApplication.getInstance().getSharedPreferences(
                DEFAULT_PREFS,
                Context.MODE_PRIVATE
            )
            return prefs.all
        }

    /**
     * removes particular key (and its associated value) from preferences
     */
    fun removeKey(key: String?) {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * clears all key-value pairs in shared preferences
     */
    fun clearAll() {
        val prefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * check particular key is available or not before accessing
     */
    fun checkKeyAvailable(key: String?): Boolean {
        val sharedPrefs =
            TawajudApplication.getInstance()
                .getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)
        return sharedPrefs.contains(key)
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // Check for common root management apps
    private fun checkRootMethod1(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        return paths.any { File(it).exists()

        }
    }
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }
    // Check for 'su' binary in PATH environment variable
    private fun checkRootMethod2(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            process.inputStream.bufferedReader().readLine() != null
        } catch (e: Exception) {
            false
        }
    }

    // Check for dangerous properties indicating a rooted device
    private fun checkRootMethod3(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }
}
