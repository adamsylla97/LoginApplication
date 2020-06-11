package com.example.loginapplication.rest

import com.google.gson.annotations.SerializedName

data class HydraMember(
    @SerializedName("@id")
    val atId: String,
    @SerializedName("@type")
    val atType: String,
    val confirmed: Boolean,
    val ending: String,
    val id: Int,
    val owner: String,
    val paymentUrl: String,
    val price: Double,
    val start: String,
    val type: Int
)