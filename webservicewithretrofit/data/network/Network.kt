package com.pankti.webservicewithretrofit.data.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Network private constructor() {

//    private val BASE_URL = "https://payam-hayati.ir/"
//    private val BASE_URL = "http://192.168.94.5/"
    private val BASE_URL = "https://www.kpec-co.com/"


    companion object {

        fun init(): Network {
            return Network()
        }
    }

/*    private fun retrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient())
            .build()
    }*/

    private fun retrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create() // Allow lenient parsing for JSON
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient())
            .build()
    }

    private fun httpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logging).addNetworkInterceptor(Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                chain.proceed(requestBuilder.build())
            }).build()
    }

//    val apiService: RetrofitAPI = retrofit().create()

    val apiService: RetrofitAPI = retrofit().create(RetrofitAPI::class.java)

}



