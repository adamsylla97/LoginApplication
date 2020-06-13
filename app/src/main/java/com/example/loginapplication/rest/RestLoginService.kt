package com.example.loginapplication.rest

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestLoginService {

    @POST("/authentication_token")
    suspend fun login(@Body data: LoginData): LoginResponse

    @GET("/api/subscriptions")
    suspend fun getSubscriptions(): List<SubscriptionResponse>

    //not working e952bd47-5857-4351-8f1b-6ba397f55c1b
    //working 9e7839ff-7422-414d-b45c-7c4888b47622
    //active 9e051d38-422b-4f59-a47d-d1239a0ddfdb

    @GET("v3/9e051d38-422b-4f59-a47d-d1239a0ddfdb")
    suspend fun testGetSubscriptions(): SubscriptionResponse

    @POST("v3/57888646-048a-478b-a6fc-d766441f64ad")
    suspend fun testLogin(): LoginResponse

}