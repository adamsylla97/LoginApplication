package com.example.loginapplication.rest

import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime

data class HydraMember(
    @SerializedName("@id")
    val atId: String,
    @SerializedName("@type")
    val atType: String,
    val confirmed: Boolean,
    val ending: OffsetDateTime,
    val id: Int,
    val owner: String,
    val paymentUrl: String,
    val price: Double,
    val start: OffsetDateTime,
    val type: Int
)