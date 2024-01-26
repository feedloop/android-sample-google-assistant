package com.example.core.data.source.remote.network

import com.example.core.data.source.remote.response.*
import com.example.core.domain.model.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.sql.Timestamp

interface ApiService {

    @POST("/snap/v1.0/access-token/b2b")
    suspend fun getToken(
        @Header("X-SIGNATURE") signature: String,
        @Header("X-CLIENT-KEY") clientKey: String,
        @Header("X-TIMESTAMP") timestamp: String,
        @Header("Content-Type") contentType: String,
        @Body requestBody: RequestBody
    ): Response<GetTokenResponse>

    @POST("/snap/v1.0/balance-inquiry")
    suspend fun getBalance(
        @Header("X-TIMESTAMP") timestamp: String,
        @Header("X-SIGNATURE") signature: String,
        @Header("X-PARTNER-ID") partnerId: String,
        @Header("CHANNEL-ID") chanelId: String,
        @Header("X-EXTERNAL-ID") externalId: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String,
        @Body requestBody: RequestBody
    ): Response<GetBalanceResponse>

}