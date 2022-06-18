package com.adityagupta.ineuron.interfaces

import com.adityagupta.ineuron.data.users.user
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DBApi {

    @GET("users")
    suspend fun getUsers(): Response<user>

    @POST("services")
    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>

}