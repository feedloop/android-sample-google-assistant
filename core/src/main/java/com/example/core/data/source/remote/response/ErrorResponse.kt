package com.example.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("responseCode")
    val responseCode: String,
    @SerializedName("responseMessage")
    val responseMessage: String
)