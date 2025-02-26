package com.sait.tawajudpremiumplusnewfeatured.util.preferences

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginData
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NotificationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.Calendar
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object UserShardPrefrences {


    private const val FILE_NAME = "encrypted_user_prefs"
    private const val KEY_FILE_NAME = "key_storage"  // File to store the master key alias
    private const val KEY_ALIAS = "master_key_alias" // Alias for the master key
    private var encryptedPreferences: SharedPreferences? = null

    fun initialize(ctx: Context) {
        if (encryptedPreferences == null) {
            // Retrieve the stored master key alias from SharedPreferences
            val sharedPreferences = ctx.getSharedPreferences(KEY_FILE_NAME, Context.MODE_PRIVATE)
            val storedKeyAlias = sharedPreferences.getString(KEY_ALIAS, null)

            val masterKey: String
            // If no key alias is found (e.g., on first install or after app reset), create a new key
            if (storedKeyAlias == null) {
                // Create a new master key and store its alias for future use
                masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                sharedPreferences.edit().putString(KEY_ALIAS, masterKey).apply()
            } else {
                // Use the stored master key alias
                masterKey = storedKeyAlias
            }
            encryptedPreferences = EncryptedSharedPreferences.create(
                FILE_NAME,
                masterKey,
                ctx.applicationContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

        }
    }
    private fun getEncryptedSharedPreferences(): SharedPreferences {
        return encryptedPreferences ?: throw IllegalStateException("Preferences not initialized")
    }
    /*fun getEncryptedSharedPreferences_2(ctx: Context?): SharedPreferences? {

        try {
            return PreferenceManager.getDefaultSharedPreferences(ctx!!)
        } catch (e: Exception) {
            // Log the exception or handle it as needed
            e.printStackTrace()
            // Return a default SharedPreferences instance or throw a custom exception
            // Example: return a default SharedPreferences instance
            return null
        }
    }

*/
    fun saveUUIDInAccount(context: Context, uuid: String) {
        val accountManager = AccountManager.get(context)
        val account = Account("MyAppAccount", "com.sait.tawajudpremiumplusnewfeatured")
        if (accountManager.addAccountExplicitly(account, null, null)) {
            accountManager.setUserData(account, "device_uuid", uuid)
        }
    }

    fun getUUIDFromAccount(context: Context): String? {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType("com.sait.tawajudpremiumplusnewfeatured")
        return accounts.firstOrNull()?.let {
            accountManager.getUserData(it, "device_uuid")
        }
    }
    /**
     * Get or generate a persistent UUID.
     * Ensures the same UUID is used even after an OS update.
     */
    fun getOrGenerateUUID(ctx: Context): String {
        val storedUUID = getEncryptedSharedPreferences().getString("unique_id", null)
        if (storedUUID != null) {
            return storedUUID // Return if already stored
        }

        // Generate new UUID and store it securely
        val newUUID = UUID.randomUUID().toString()
        setUUID(ctx, newUUID)
        return newUUID
    }
    fun getOrCreateKeystoreUUID(context: Context): String {
        val encryptedFile = getEncryptedFile(context)

        // Check if the file exists — if yes, decrypt it
        if (encryptedFile.exists()) {
            return decryptUUIDFromFile(context)
        }

        // Generate a new UUID and store it securely
        val newUUID = UUID.randomUUID().toString()
        encryptAndStoreUUIDToFile(context,newUUID)
        return newUUID
    }

    private fun getEncryptedFile(context: Context?): File {
        val dir =  context!!.getExternalFilesDir(null) ?: context!!.filesDir
        return File(dir, "encrypted_uuid.dat")
    }

    /**
     * Encrypts a UUID and stores it in an encrypted file on external storage.
     */
    private fun encryptAndStoreUUIDToFile(context: Context?,uuid: String) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(
            KeyGenParameterSpec.Builder("unique_device_key", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setUserAuthenticationRequired(false)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )
        val secretKey = keyGenerator.generateKey()

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedUUID = cipher.doFinal(uuid.toByteArray(Charset.defaultCharset()))

        val encryptedFile = getEncryptedFile(context)
        FileOutputStream(encryptedFile).use { fos ->
            fos.write(iv)
            fos.write(encryptedUUID)
        }
    }

    /**
     * Decrypts the stored UUID from the encrypted file.
     */
    private fun decryptUUIDFromFile(context: Context): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey("unique_device_key", null) as SecretKey

        val encryptedFile = getEncryptedFile(context)
        val fileData = encryptedFile.readBytes()
        val iv = fileData.copyOfRange(0, 12) // GCM IV is 12 bytes
        val encryptedUUID = fileData.copyOfRange(12, fileData.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encryptedUUID), Charset.defaultCharset())
    }
    private fun setUUID(ctx: Context, uuid: String) {
        val editor = getEncryptedSharedPreferences().edit()
        editor.putString("unique_id", uuid).apply()
    }
 /*   *//**
     * Save UUID securely in EncryptedSharedPreferences.
     *//*
    private fun setUUID(ctx: Context, uuid: String) {
        val editor = getEncryptedSharedPreferences().edit()
        editor.putString("unique_id", uuid).apply()
    }
    *//**
     * Generates or retrieves a UUID stored securely in Android Keystore.
     *//*
    fun getOrCreateKeystoreUUID(context: Context): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        return if (keyStore.containsAlias("unique_device_id")) {
            // Retrieve the encrypted UUID and decrypt it
            decryptUUID(context)
        } else {
            // Generate a new UUID, encrypt it, and store it securely
            val newUUID = UUID.randomUUID().toString()
            encryptAndStoreUUID(context, newUUID)
            newUUID
        }
    }

    *//**
     * Encrypts a UUID and stores it in SharedPreferences.
     *//*
    private fun encryptAndStoreUUID(context: Context, uuid: String) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(
            KeyGenParameterSpec.Builder("unique_device_id", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setUserAuthenticationRequired(false)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )
        val secretKey = keyGenerator.generateKey()

        val cipher = Cipher.getInstance( "AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedUUID = cipher.doFinal(uuid.toByteArray(Charset.defaultCharset()))

        getEncryptedSharedPreferences().edit()
            .putString("uuid_encrypted", Base64.encodeToString(encryptedUUID, Base64.DEFAULT))
            .putString("uuid_iv", Base64.encodeToString(iv, Base64.DEFAULT))
            .apply()
    }

    *//**
     * Decrypts the stored UUID from Keystore.
     *//*
    private fun decryptUUID(context: Context): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey("unique_device_id", null) as SecretKey

        val encryptedUUID = Base64.decode(getEncryptedSharedPreferences().getString("uuid_encrypted", ""), Base64.DEFAULT)
        val iv = Base64.decode(getEncryptedSharedPreferences().getString("uuid_iv", ""), Base64.DEFAULT)

        val cipher = Cipher.getInstance( "AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encryptedUUID), Charset.defaultCharset())
    }
*/
   public fun saveUserInfo(ctx: Context?, userInfo: LoginData?) {
        CoroutineScope(Dispatchers.IO).launch {
            val editor = getEncryptedSharedPreferences()!!.edit()
            editor.putString("userdetails", Gson().toJson(userInfo))
            editor.commit()
        }

    }

    fun getUserInfo(ctx: Context?): LoginData {
        return Gson().fromJson(
            getEncryptedSharedPreferences()!!!!.getString("userdetails", ""),
            LoginData::class.java
        )
    }





    fun setNotificationData(context: Context?, notificationResponse: List<NotificationData>) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("notificationData", Gson().toJson(notificationResponse))
        editor.commit()
    }
    fun getNotificationData(ctx: Context?):List<NotificationData> {
        val json = getEncryptedSharedPreferences()!!.getString("notificationData", "")
        val type = object : TypeToken<List<NotificationData>>() {}.type
        return Gson().fromJson(json, type)
    }



    fun setuserLogin(context: Context?, isUserLogin: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("userlogin", isUserLogin)
        editor.commit()
    }
    fun setmultipleLoc(context: Context?, isMultpleLoc: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("multipleLoc", isMultpleLoc)
        editor.commit()
    }
    fun setminutelyReminder(context: Context?, minute: Int) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putInt("minutely_reminder", minute)
        editor.commit()
    }
    fun getminutelyReminder(context: Context?): Int? {
        return  context?.let { getEncryptedSharedPreferences()!!.getInt("minutely_reminder",0) }
    }


    fun setCustomReminder(context: Context?, minute: Int) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putInt("customly_reminder", minute)
        editor.commit()
    }
    fun getCustomReminder(context: Context?): Int? {
        return  context?.let { getEncryptedSharedPreferences()!!.getInt("customly_reminder",0) }
    }


    fun getMulipleLoc(context: Context?): Boolean? {
       return  context?.let { getEncryptedSharedPreferences()!!.getBoolean("multipleLoc",false) }
    }
    fun setAppliedViolationRequestData(context: Context?,value :String){
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor?.putString("saved_state", Gson().toJson(value))
        editor.commit()
    }
    fun getAppliedViolationRequestData(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("saved_state", "")
    }
    fun isUserLogin(context: Context?): Boolean {
        try {
            return  getEncryptedSharedPreferences()!!.getBoolean("userlogin", false)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * Wipes the data when the user wants to log out.
     *
     * @param ctx
     */
    fun clearUser(ctx: Context?) {
        val editor = getEncryptedSharedPreferences()!!!!.edit()
        editor.clear() //clear all stored data
        editor.commit()
    }

    fun setAppActive(context: Context?, isAppActive: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("isappactive", isAppActive)
        editor.commit()
    }

    fun isAppActive(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("isappactive", false)
    }
    fun setisRestart(context: Context?, b: Boolean): Boolean {
        return   getEncryptedSharedPreferences().edit().putBoolean("b_restart", b).commit()

    }

    fun getisRestart(context: Context?): Boolean {
        return  getEncryptedSharedPreferences().getBoolean("b_restart", true)

    }

    fun saveCredentials(context: Context?, userName: String?, password: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val editor = getEncryptedSharedPreferences()!!.edit()
            editor.putString("userName", userName)
            editor.putString("password", password)
            editor.commit()
        }

    }

    fun getUserName(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("userName", "")
    }


    fun setUserName(context: Context?, userName: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("userName", userName)
        editor.commit()
    }
    fun getPassword(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("password", "")
    }

    fun setnewPassword(context: Context?,newPassword :String?){
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("new_password", newPassword)
        editor.commit()
    }
        fun getnewPassword(context: Context?):String?{
            return getEncryptedSharedPreferences()!!.getString("new_password", "")
        }



    fun setPassword_new(context: Context?, password: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("password", password)
        editor.commit()
    }

    fun getPassword_new(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("password", "")
    }
    fun getCurrentTheme(context: Context?): Int {
        return getEncryptedSharedPreferences()?.getInt("currentTheme", R.style.AppTheme_skyblue) ?: R.style.AppTheme_skyblue

    }

    fun setCurrentTheme(context: Context?, newTheme: Int) {
        getEncryptedSharedPreferences()?.edit()?.putInt("currentTheme", newTheme)?.apply()

    }


    fun setUsername_new(context: Context?, username: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("username", username)
        editor.commit()
    }

    fun getUsername_new(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("username", "")
    }


    fun setSaveCradentials(context: Context?, isSave: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val editor = getEncryptedSharedPreferences()!!.edit()
            editor.putBoolean("ischecked", isSave)
            editor.commit()
        }
    }

    fun isSaveCradentials(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("ischecked", false)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun clearUserInfo(context: Context?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("userdetails", "")
        editor.commit()
    }

    fun saveWorkLocation(context: Activity?, lat: Double, lng: Double) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("lat", "" + lat)
        editor.putString("lng", "" + lng)
        editor.commit()
    }






    fun saveTimeIn(context: Activity?, reason: Int) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putInt("reason", reason)
        editor.commit()
    }

    fun getTimeIN(context: Activity?): Int {
        return getEncryptedSharedPreferences()!!.getInt("reason", -1)
    }


    fun getURL(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("BASE_URL", "")
    }
    fun setLastCallTime(context: Context?, lastCallTime: Long) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putLong("last_call_time", lastCallTime)
        editor.commit()
    }
    fun getLastCallTime(context: Context?): Long? {
        return  context?.let { getEncryptedSharedPreferences()!!.getLong("last_call_time", 0L) }
    }
    fun setinHome(context: Context?, inHome: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("user_in_home", inHome)
        editor.commit()
    }
    fun saveJobData(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val gson = Gson()
            val extras = intent.extras ?: return@launch
            // Convert the extras to a map of key-value pairs
            val extrasMap = extras.keySet().associateWith { extras[it] }
            // Convert the map to a JSON string
            val jsonString = gson.toJson(extrasMap)
            val editor = getEncryptedSharedPreferences()!!.edit()
            editor.putString("jobSchedulerData", jsonString)
            editor.apply()
        }
    }
    fun getJobData(context: Context?): Map<String, Any>?{
        try {
            context?.let { val jsonString=getEncryptedSharedPreferences()!!.getString("jobSchedulerData", "") ?: return@let false
                // Convert the JSON string back to a map
                val gson = Gson()
                val type = object : TypeToken<Map<String, Any>>() {}.type

                return   gson.fromJson(jsonString, type)}



        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return null
    }
    fun getInHome(context: Context?): Boolean? {
        return  context?.let { getEncryptedSharedPreferences()!!.getBoolean("user_in_home", false) }
    }
    fun setinAttendance(context: Context?, inAttendance: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("user_in_attendance", inAttendance)
        editor.commit()
    }
    fun getInAttendance(context: Context?): Boolean? {
        return  context?.let { getEncryptedSharedPreferences()!!.getBoolean("user_in_attendance", false) }
    }
    fun setfirstTimeNotificationAppears(context: Context?,showingNotification : Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("notification_appears", showingNotification)
        editor.commit()
    }
    fun getfirstTimeNotificationAppears(context: Context?): Boolean? {
        return  context?.let { getEncryptedSharedPreferences()!!.getBoolean("notification_appears",false) }
    }
    fun setLanguage(context: Context?, lang: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("app_lang", lang)
        editor.commit()
    }
    @JvmStatic
     fun  getLanguage(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("app_lang", "0")
    }


    fun setUniqueId(context: Context?, un_id: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("unique_id", un_id)
        editor.commit()
    }

    fun getUniqueId(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("unique_id", "")
    }

    fun setUniqueIdOnce(context: Context?, check: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("unique_id_once", check)
        editor.commit()
    }

    fun getUniqueIdOnce(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("unique_id_once", false)
    }


    fun setFingerSwitch(context: Context?, check: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("finger_switch", check)
        editor.commit()
    }

    fun getFingerSwitch(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("finger_switch", false)
    }

    fun setFaceLoginSwitch(context: Context?, check: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("FaceLoginSwitch", check)
        editor.commit()
    }

    fun getFaceLoginSwitch(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("FaceLoginSwitch", false)
    }

    fun setFaceVerificationSwitch(context: Context?, check: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("FaceVerificationSwitch", check)
        editor.commit()
    }

    fun getFaceVerificationSwitch(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("FaceVerificationSwitch", false)
    }

    fun setSelfServiceList(context: Context?, list: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("self_service_list", list)
        editor.commit()
    }

    fun getSelfServiceList(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("self_service_list", "")
    }

    fun setSelfServicePos(context: Activity?, pos: Int) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putInt("self_service_pos", pos)
        editor.commit()
    }

    fun getSelfServicePos(context: Context?): Int {
        return getEncryptedSharedPreferences()!!.getInt("self_service_pos", -1)
    }

    fun setCheckSelfService(context: Activity?, pos: Int) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putInt("check_self_service", pos)
        editor.commit()
    }

    fun getCheckSelfService(context: Context?): Int {
        return getEncryptedSharedPreferences()!!.getInt("check_self_service", 0)
    }





    fun setHearBeat(context: Context?, pos: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("save_heart_beat", pos)
        editor.commit()
    }

    fun getHeartBeat(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("save_heart_beat", false)
    }

    fun setFeedback(context: Context?, pos: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("save_feedback", pos)
        editor.commit()
    }

    fun getFeedback(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("save_feedback", false)
    }

    fun setFeedbackQuestionStatus(context: Context?, pos: Boolean) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putBoolean("feedback_status", pos)
        editor.commit()
    }

    fun getFeedbackQuestionStatus(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("feedback_status", false)
    }

    fun setEmployeeProfileData(context: Context?, list: String?) {
        val editor = getEncryptedSharedPreferences()!!.edit()
        editor.putString("employee_profile_data", list)
        editor.commit()
    }

    fun getEmployeeProfileData(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("employee_profile_data", "")
    }

    fun isFirstRun(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("firstRun", true)
    }

    fun updateFirstRun(context: Context?) {
        getEncryptedSharedPreferences()!!.edit().putBoolean("firstRun", false).commit()
    }

    fun resetFirstRun(context: Context?) {
        getEncryptedSharedPreferences()!!.edit().putBoolean("firstRun", true).commit()
    }




    fun setCheckInStatus(context: Context?) {
        getEncryptedSharedPreferences()!!.edit().putBoolean("check_in", true).commit()
    }

    fun getCheckInStatus(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("check_in", false)
    }

    fun setCheckOutStatus(context: Context?) {
        getEncryptedSharedPreferences()!!.edit().putBoolean("check_in", false).commit()
    }

    fun setLastWarned(context: Context?) {
        getEncryptedSharedPreferences()!!.edit()
            .putLong("lastWarned", Calendar.getInstance().timeInMillis).commit()
    }

    fun shouldWarnAgain(context: Context?): Boolean {
        val lastWarned = getEncryptedSharedPreferences()!!.getLong("lastWarned", 0)
        if (lastWarned == 0L) return true
        val diff = Calendar.getInstance().timeInMillis - lastWarned
        val seconds = diff / 1000
        val minutes = seconds / 60
        return minutes > 50
    }




    fun saveWorkLocationDistance(context: Context?, distance: Float) {
        getEncryptedSharedPreferences()!!.edit().putFloat("distance", distance).commit()
    }

    fun getWorkLocationDistance(context: Context?): Float {
        return getEncryptedSharedPreferences()!!.getFloat("distance", 0f)
    }

    fun saveWorkGPSCoordinates(context: Context?, GPS: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("GPS", GPS).commit()
    }

    fun getWorkGPSCoordinates(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("GPS", "")
    }

    fun saveWorkLocationGPSCoordinates(context: Context?, GPS: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("LocationGPS", GPS).commit()
    }

    fun getWorkLocationGPSCoordinates(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("LocationGPS", "")
    }





    fun setBaseUrl(context: Context?, baseUrl: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("baseUrl", baseUrl).commit()
    }

    fun getBaseUrl(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("baseUrl", "")
    }



    fun setName(context: Context?, name: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("name", name).commit()
    }

    fun getName(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("name", "")
    }

    fun setShortName(context: Context?, shortname: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("shortname", shortname).commit()
    }

    fun getShortName(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("shortname", "")
    }

    fun setPackage(context: Context?, packagename: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("packagename", packagename).commit()
    }

    fun getPackage(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("packagename", "")
    }



    fun setReleaseNo(context: Context?, releaseNo: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("releaseNo", releaseNo).commit()
    }

    fun getReleaseNo(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("releaseNo", "")
    }

    fun setLicenseStartDate(context: Context?, licenseStartDate: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("licenseStartDate", licenseStartDate).commit()
    }

    fun getLicenseStartDate(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("licenseStartDate", "")
    }


    fun setLicenseEndDate(context: Context?, licenseEndDate: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("licenseEndDate", licenseEndDate).commit()
    }

    fun getLicenseEndDate(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("licenseEndDate", "")
    }



    fun setEmail(context: Context?, email: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("email", email).commit()
    }

    fun getEmail(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("email", "")
    }



    fun setMobile(context: Context?, mobile: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("mobile", mobile).commit()
    }

    fun getMobile(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("mobile", "")
    }




    fun setNoUsers(context: Context?, nousers: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("nousers", nousers).commit()
    }

    fun getNoUsers(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("nousers", "")
    }




    fun setDomain(context: Context?, domain: String?) {
        getEncryptedSharedPreferences()!!.edit().putString("domain", domain).commit()
    }

    fun getDomain(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("domain", "")
    }








    fun saveWorkLocationId(context: Context?, workLocationId: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("workLocationId", workLocationId).commit()

    }

    fun getWorkLocationId(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("workLocationId", "")
    }

    fun saveWorkLocationName(context: Context?, WorkLocationName: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("WorkLocationName", WorkLocationName).commit()

    }

    fun getWorkLocationName(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("WorkLocationName", "")
    }






    fun saveGpsCoordinates(context: Context?, GpsCoordinates: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("GpsCoordinates", GpsCoordinates).commit()

    }

    fun getGpsCoordinates(context: Context?): String? {
        return getEncryptedSharedPreferences()!!.getString("GpsCoordinates", "")
    }


    fun getAllowedGPSFlag(context: Context?): Int {
        return getEncryptedSharedPreferences()!!.getInt("allowedGPSFlag", 0)
    }

    fun setAllowedGPSFlag(context: Context?, allowedGPSFlag: Int) {
        getEncryptedSharedPreferences()!!.edit().putInt("allowedGPSFlag", allowedGPSFlag).commit()
    }



    fun getAllowedDistance(context: Context?): Int? {
        try {
           return getEncryptedSharedPreferences()!!.getInt("allowedDistance", 0)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }

    }

    fun setAllowedDistance(context: Context?, allowedDistance: Int) {
        getEncryptedSharedPreferences()!!.edit().putInt("allowedDistance", allowedDistance).commit()
    }





    fun saveEmpCardId(context: Context?, EmpCardId: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("EmpCardId", EmpCardId).commit()

    }

    fun getEmpCardId(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("EmpCardId", "")
    }




    fun saveEntityname(context: Context?, Entityname: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("Entityname", Entityname).commit()

    }

    fun getEntityname(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("Entityname", "")
    }



    fun saveJoinDate(context: Context?, JoinDate: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("JoinDate", JoinDate).commit()

    }

    fun getJoinDate(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("JoinDate", "")
    }




    fun saveManagerId(context: Context?, ManagerId: Long) {
            getEncryptedSharedPreferences()!!.edit().putLong("ManagerId", ManagerId).commit()

    }

    fun getManagerId(context: Context): Long? {
        return getEncryptedSharedPreferences()!!.getLong("ManagerId", 0)
    }



    fun saveGrade(context: Context?, Grade: String?) {
            getEncryptedSharedPreferences()!!.edit().putString("Grade", Grade).commit()

    }

    fun getGrade(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("Grade", "")
    }


    fun getisAdmin(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("isAdmin", false)

    }

    fun setisAdmin(context: Context?, b: Boolean): Boolean {
        return  getEncryptedSharedPreferences()!!.edit().putBoolean("isAdmin", b).commit()

    }

    fun getisManager(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("isManager", false)

    }

    fun setisManager(context: Context?, b: Boolean): Boolean {
        return  getEncryptedSharedPreferences()!!.edit().putBoolean("isManager", b).commit()

    }
    fun setLoginToken(context: Context?, token: String): Boolean {
        return getEncryptedSharedPreferences().edit().putString("login_token", token).commit()
    }

    fun getLoginToken(context: Context?): String {
        val token = getEncryptedSharedPreferences().getString("login_token", "") // Default is empty string
        return token ?: ""  // Ensure it returns an empty string if the token is null
    }

    fun getisHR(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("isHRAdmin", false)

    }

    fun setisHR(context: Context?, b: Boolean): Boolean {
        return  getEncryptedSharedPreferences()!!.edit().putBoolean("isHRAdmin", b).commit()

    }




    fun getisMinute(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("minutely", false)

    }

    fun setisMinute(context: Context?, b: Boolean): Boolean {
        return  getEncryptedSharedPreferences()!!.edit().putBoolean("minutely", b).commit()

    }


    fun getisCustom(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("Custom", false)

    }

    fun setisCustom(context: Context?, b: Boolean): Boolean {
        return  getEncryptedSharedPreferences()!!.edit().putBoolean("Custom", b).commit()

    }







    fun isMustPhysicalPunch(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("isMustPhysicalPunch", false)
    }

    fun setMustPhysicalPunch(context: Context?, b: Boolean): Boolean {
        return getEncryptedSharedPreferences()!!.edit().putBoolean("isMustPhysicalPunch", b).commit()

    }

    fun markItemAsRead(context: Context, position: Int, month: Int) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("item_${position}_month_$month", true) // Save read status as true for the item at this position and month
            apply()
        }
    }

    fun isItemRead(context: Context, position: Int, month: Int): Boolean {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("item_${position}_month_$month", false) // Default to false if not found
    }







    fun saveCountAnnouncementsUnRead(context: Context?, count: String?) {
            getEncryptedSharedPreferences().edit().putString("count", count).commit()
    }

    fun getCountAnnouncementsUnRead(context: Context): String? {
        return getEncryptedSharedPreferences()!!.getString("count", "")
    }





    fun setisInside(context: Context?, b: Boolean): Boolean {
        return  getEncryptedSharedPreferences()!!.edit().putBoolean("isInside", b).commit()

    }

    fun getisInside(context: Context?): Boolean {
        return getEncryptedSharedPreferences()!!.getBoolean("isInside", false)

    }




}