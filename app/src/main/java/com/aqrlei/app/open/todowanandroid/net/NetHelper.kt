package com.aqrlei.app.open.todowanandroid.net

import android.os.Build
import com.aqrlei.open.retrofit.livedatacalladapter.LiveDataCallAdapterFactory
import com.aqrlei.open.retrofit.livedatacalladapter.LiveDataConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 * @author aqrlei on 2018/12/20
 */

class NetHelper private constructor() {
    companion object {
        fun get(): NetHelper {
            return Holder.instance
        }
    }

    private val apiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
            .addConverterFactory(LiveDataConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getCompatOkHttpClient())
            .baseUrl(NetConfigure.BASE_URL)
            .build()
    }

    fun <T> createService(clazz: Class<T>): T = apiRetrofit.create(clazz)

    private fun getCompatOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(HttpResponseInterceptor())
            .addInterceptor(HttpRequestInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(3000L, TimeUnit.MILLISECONDS)
            .connectTimeout(3000L, TimeUnit.MILLISECONDS)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            /**绕过证书验证*/
            val trustManager = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            }
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory, trustManager)

            builder.hostnameVerifier { _, _ -> true }
        }
        return builder.build()
    }

    private object Holder {
        val instance = NetHelper()
    }
}