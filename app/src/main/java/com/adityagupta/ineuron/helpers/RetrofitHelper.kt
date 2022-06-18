package com.adityagupta.ineuron.helpers

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    val baseUrl = "https://0ce4-14-97-167-154.in.ngrok.io/"


    fun getInstance(): Retrofit {

        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(getHttpClient())
            // we need to add converter factory to
            // convert JSON object to Java object
            .addConverterFactory(GsonConverterFactory.create(gson))

            .build()
    }

    fun getHttpClient(): OkHttpClient {

        return  OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("Accept", "application/json")

                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()
    }
}