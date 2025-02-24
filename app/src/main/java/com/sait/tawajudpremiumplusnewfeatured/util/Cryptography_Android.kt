package com.sait.tawajudpremiumplusnewfeatured.util

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.logging.Logger
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

 class Cryptography_Android {

    enum class EncryptMode {
        ENCRYPT, DECRYPT
    }


     companion object {
         fun get_SHA256(text: String, length: Int): String {
             val md = MessageDigest.getInstance("SHA-256")
             md.update(text.toByteArray(StandardCharsets.UTF_8))
             val digest = md.digest()

             val result = StringBuffer()
             for (b in digest) {
                 result.append(String.format("%02x", b)) // Convert to hex
             }

             return if (length > result.length) {
                 result.toString()
             } else {
                 result.toString().substring(0, length)
             }
         }

         fun getHashSha256(input: String, length: Int): String {
             val digest = MessageDigest.getInstance("SHA-256")
             val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
             val hash = hashBytes.joinToString("") { "%02x".format(it) }
             return if (hash.length > length) hash.substring(0, length) else hash.padEnd(length, '0')
         }

         fun decryptTo(value: String, key: String, iv: String): String {
             return try {
                 val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                 val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
                 val ivSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
                 cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

                 val decodedBytes = Base64.decode(value, Base64.DEFAULT)
                 val decryptedBytes = cipher.doFinal(decodedBytes)
                 String(decryptedBytes, Charsets.UTF_8) // Return decrypted string
             } catch (e: Exception) {
                 e.printStackTrace() // Log the error (optional: use Log.e in Android)
                 "Error: ${e.localizedMessage}" // Return a default error message
             }
         }


         fun decrypt(encryptedText: String, key: String, iv: String): String {
             return encryptDecrypt(encryptedText, key, EncryptMode.DECRYPT, iv)
         }
         fun encryptDecrypt(
             inputText: String,
             encryptionKey: String,
             mode: EncryptMode,
             initVector: String
         ): String {
             // Adjust key and IV lengths to 16 bytes
             val key = encryptionKey.toByteArray(StandardCharsets.UTF_8).copyOf(16)
             val iv = initVector.toByteArray(StandardCharsets.UTF_8).copyOf(16)

             val keySpec = SecretKeySpec(key, "AES")
             val ivSpec = IvParameterSpec(iv)

             return try {
                 val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                 when (mode) {
                     EncryptMode.ENCRYPT -> {
                         cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
                         val encryptedBytes = cipher.doFinal(inputText.toByteArray(StandardCharsets.UTF_8))
                         Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
                     }

                     EncryptMode.DECRYPT -> {
                         cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
                         val decodedBytes = Base64.decode(inputText.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)
                         val decryptedBytes = cipher.doFinal(decodedBytes)
                         String(decryptedBytes, StandardCharsets.UTF_8)
                     }
                 }
             } catch (e: Exception) {
                 e.printStackTrace()
                 throw RuntimeException("Error in encryption/decryption: ${e.message}")
             }
         }


     }
}