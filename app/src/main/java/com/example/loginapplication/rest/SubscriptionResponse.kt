package com.example.loginapplication.rest

import com.google.gson.annotations.SerializedName

data class SubscriptionResponse(
    @SerializedName("@context")
    val context: String,
    @SerializedName("@id")
    val id: String,
    @SerializedName("@type")
    val type: String,
    @SerializedName("hydra:member")
    val hydra_member: List<HydraMember>,
    @SerializedName("hydra:totalItems")
    val hydra_totalItems: Int
)