package com.example.loginapplication.rest

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestLoginService {

    @POST("/authentication_token")
    suspend fun login(@Body data: LoginData): LoginResponse

    @GET("/api/subscriptions")
    suspend fun getSubscriptions(): SubscriptionResponse

}