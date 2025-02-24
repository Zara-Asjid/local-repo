package com.sait.tawajudpremiumplusnewfeatured.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import com.pixplicity.easyprefs.library.Prefs
import com.sait.tawajudpremiumplusnewfeatured.util.Const
import com.sait.tawajudpremiumplusnewfeatured.util.PrefKeys
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.buildRequestLog
import com.sait.tawajudpremiumplusnewfeatured.util.preferences.PrefUtils.buildResponseLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * This class is used to call apis. It will return Retrofit api service for call an api
 */
object ApiClient {
    private lateinit var httpBuilder: OkHttpClient.Builder
    private lateinit var retrofitBuilder: Retrofit.Builder
    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient
    private var loggingInterceptor = HttpLoggingInterceptor()
    lateinit var baseUrl: String
    private var connection: HttpsURLConnection? = null

    private var BASE_URL = "https://sgi.software/SmartRegister2.0/api/"

    //private var BASE_URL = "https://sgi.software/SmartRegister/api/"
    //private var BASE_URL = "https://sgi.software/SmartRegister_Dev/api/"

    fun init() {
        baseUrl = BASE_URL

        retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        httpBuilder = OkHttpClient.Builder()
            .addInterceptor(ServiceInterceptor())
            .addInterceptor(loggingInterceptor) // Logs the API call
        addLoggingInterceptor(true)
        setTimeout(httpBuilder)

        okHttpClient = getUnsafeOkHttpClient()

        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = retrofitBuilder
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createService(url: String = BASE_URL): ApiService {
        val updatedUrl = updateBaseUrl(url)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(updatedUrl)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        httpBuilder = OkHttpClient.Builder()
            .addInterceptor(ServiceInterceptor())
            .addInterceptor(loggingInterceptor) // Logs the API call
        setTimeout(httpBuilder)

        okHttpClient = getUnsafeOkHttpClient()

        retrofit = retrofitBuilder
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }

    private fun updateBaseUrl(url: String): String {
        return when {
            url.isEmpty() -> baseUrl
            url.endsWith("/", true) -> url
            else -> "$url/"
        }
    }

    private fun addLoggingInterceptor(isLoggingEnabled: Boolean) {
        if (isLoggingEnabled) {
            if (!httpBuilder.interceptors().contains(loggingInterceptor)) {
                httpBuilder.addInterceptor(loggingInterceptor)
            }
        }
    }

    private fun setTimeout(builder: OkHttpClient.Builder) {
        builder.connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
    }

    class ServiceInterceptor : Interceptor {
        var token: String = ""




        fun Token(token: String) {
            this.token = token
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            // Capture request details
            val requestLog = buildRequestLog(request)

            // Log request in a pretty format
            Log.d("API Request", requestLog)

            if (request.header("No-Authentication") == null) {
                val token = Prefs.getString(PrefKeys.Auth_key, "")
                if (!token.isNullOrEmpty()) {
                    val finalToken = "Bearer $token"
                    request = request.newBuilder()
                        .addHeader("Authorization", finalToken)
                        .build()
                    Log.e("finalToken", finalToken)
                }
            }
            val response = chain.proceed(request)
            // Clone response body for logging
            val responseBody = response.body
            val responseBodyString = responseBody?.string() ?: ""

            // Rebuild the response before returning it because the body has been consumed
            val newResponseBody = ResponseBody.create(responseBody?.contentType(), responseBodyString)
            val newResponse = response.newBuilder().body(newResponseBody).build()
            // Capture response details
            val responseLog = buildResponseLog(response,request.url,responseBodyString)
            // Log response in a pretty format
            Log.d("API Response", responseLog)

            return newResponse
        }
    }

    private val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(
                certs: Array<X509Certificate>, authType: String
            ) {
            }

            override fun checkServerTrusted(
                certs: Array<X509Certificate>, authType: String
            ) {
            }
        }
    )

    @Throws(IOException::class)
    fun serviceConnectionSE(url: String?) {
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e.message
        }
        connection = URL(url).openConnection() as HttpsURLConnection
        (connection as HttpsURLConnection).hostnameVerifier = AllowAllHostnameVerifier()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            // Trust all certificates
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            // Initialize OkHttpClient.Builder
            val builder = OkHttpClient.Builder()

            // Set the SSL socket factory and trust manager
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)

            // Disable hostname verification
            builder.hostnameVerifier { _, _ -> true }

            // Add the ServiceInterceptor
            builder.addInterceptor(ServiceInterceptor())

            // Set the timeout
            setTimeout(builder)

            // Add retry interceptor
            builder.addInterceptor(RetryInterceptor(3))

            // Build the OkHttpClient instance
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create a trust all certificates OkHttpClient", e)
        }
    }

    class RetryInterceptor(private val maxRetries: Int) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var response: Response? = null
            var attempt = 0
            var success = false

            while (attempt < maxRetries && !success) {
                try {
                    response?.close()
                    response = chain.proceed(chain.request())
                    success = response.isSuccessful
                } catch (e: IOException) {
                    attempt++
                    if (attempt >= maxRetries) throw e
                }
            }

            return response ?: throw IOException("Maximum retry attempts exceeded")
        }
    }
}
