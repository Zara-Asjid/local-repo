package com.sait.tawajudpremiumplusnewfeatured.util
import android.net.Uri

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import android.provider.OpenableColumns
import com.google.common.io.Files.getFileExtension
import java.io.File
import java.io.*
object FileUtils {

    fun getFileExtensionUpload(url: String): String {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        return mimeTypeMap.getMimeTypeFromExtension(extension)?.split("/")?.lastOrNull() ?: "unknown"
    }
    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var fileName = ""

        when (uri.scheme) {
            "file" -> {
                // For file scheme, directly get the file name from the path
                fileName = File(uri.path!!).name
            }
            "content" -> {
                // For content scheme, use the ContentResolver to query the display name
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (displayNameIndex != -1) {
                            fileName = it.getString(displayNameIndex)
                        }
                    }
                }
            }
        }

        // If the file name is still empty, try to extract it from the last path segment
        if (fileName.isEmpty()) {
            fileName = uri.lastPathSegment ?: ""
        }

        // If the file name is still empty, generate a random name with an extension
        if (fileName.isEmpty()) {
            val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            fileName = "file_${System.currentTimeMillis()}.$extension"
        }

        return fileName
    }
    fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
        }
    }
    fun uriToBase64(context: Context, uri: Uri): String? {
        var inputStream: InputStream? = null
        var outputStream: ByteArrayOutputStream? = null

        try {
            inputStream = context.contentResolver.openInputStream(uri)
           // val bytes = readBytes(inputStream)
            val bufferedInputStream = BufferedInputStream(inputStream)
            outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(4096) // Adjust buffer size as needed

            var bytesRead: Int
            while (bufferedInputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                val chunkBytes = outputStream.toByteArray()
                if (chunkBytes.size >= 1024 * 1024) { // Check if chunk size is too large
                    return Base64.encodeToString(chunkBytes, Base64.DEFAULT)
                }
            }
            val bytes = outputStream.toByteArray()

            return if (bytes.isNotEmpty()) {
                Base64.encodeToString(bytes, Base64.DEFAULT)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }
    /*fun saveBundle(context: Context?,bundle: Bundle, filename: String) {
        val file = File(context!!.filesDir, filename)
        val json = JSONObject(bundleToMap(bundle))
        FileWriter(file).use { fileWriter ->
            fileWriter.write(json.toString())
        }
    }
    fun bundleToMap(bundle: Bundle): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        for (key in bundle.keySet()) {
            map[key] = bundle.get(key)
        }
        return map
    }
    fun loadBundle(context: Context?,filename: String): Bundle? {
        val file = File(context!!.filesDir, filename)
        if (!file.exists()) return null
        val json = FileReader(file).use { fileReader ->
            fileReader.readText()
        }
        val bundle = Bundle()
        val jsonObject = JSONObject(json)
        val keysIterator = jsonObject.keys()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next()
            val value = jsonObject.get(key)
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                // Add more cases for other types as needed
            }
        }
        return bundle
    }

    fun deleteBundle(context: Context?,filename: String) {
        val file = File(context!!.filesDir, filename)
        if (file.exists()) {
            file.delete()
        }
    }*/
    fun bitmapToBase64(context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Function to convert file to Base64 using Bitmap (passed directly)
    fun fileToBase64FromBitmap(context: Context, bitmap: Bitmap, fileName: String): String? {
        val fileExtension = getFileExtension(fileName)
        return when (fileExtension) {
            "jpg", "jpeg" -> bitmapToBase64(context, bitmap, Bitmap.CompressFormat.JPEG)
            "png" -> bitmapToBase64(context, bitmap, Bitmap.CompressFormat.PNG)
            "webp" -> bitmapToBase64(context, bitmap, Bitmap.CompressFormat.WEBP)
            else -> null // Handle unsupported formats
        }
    }
    @Throws(IOException::class)
    private fun readBytes(inputStream: InputStream?): ByteArray? {
        if (inputStream == null) return null
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}

