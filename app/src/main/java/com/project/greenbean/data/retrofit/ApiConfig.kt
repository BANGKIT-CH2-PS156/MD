package com.project.greenbean.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
        fun retrofitInstance(token: String): ApiService {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(authInterceptor).addInterceptor(loggingInterceptor)
            val api = Retrofit.Builder()
                .baseUrl("https://app-api-i7g2vjhjra-et.a.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()

            return api.create(ApiService::class.java)
        }
    fun retrofitInstanceNoHeader(): ApiService {
        val api = Retrofit.Builder()
            .baseUrl("https://app-api-i7g2vjhjra-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return api.create(ApiService::class.java)
    }
}