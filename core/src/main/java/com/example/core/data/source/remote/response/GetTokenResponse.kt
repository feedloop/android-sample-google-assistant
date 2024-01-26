package com.example.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class GetTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("expiresIn")
    val expiresIn: String,
    @SerializedName("tokenType")
    val tokenType: String
)