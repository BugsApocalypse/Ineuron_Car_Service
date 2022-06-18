package com.adityagupta.ineuron.interfaces

import com.adityagupta.ineuron.data.services.services
import com.adityagupta.ineuron.data.users.user
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface DBApi {

    @GET("admins")
    suspend fun getUsers(): Response<user>

    @POST("createadmin")
    suspend fun createAdmin(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("createuser")
    suspend fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("services2")
    suspend fun getServices(@Query("adminid") id: String): Response<services>

    @POST("booking")
    suspend fun createBooking(@Body requestBody: RequestBody): Response<ResponseBody>
}