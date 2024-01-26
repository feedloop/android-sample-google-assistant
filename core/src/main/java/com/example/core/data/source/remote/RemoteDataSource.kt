package com.example.core.data.source.remote

import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.ApiService
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.data.source.remote.response.GetTokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

class RemoteDataSource(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource
) {
    private fun isSuccessRequest(statusCode: Int) = statusCode == 200

    private fun getErrorMessage(errorBody: ResponseBody?): String {
        return JSONObject(
            errorBody?.charStream()?.readText().toString()
        ).getString("responseMessage")
    }

    suspend fun getToken(
        signature: String,
        clientKey: String,
        timestamp: String,
        requestBody: RequestBody
    ): Flow<ApiResponse<GetTokenResponse>> = flow {
        try {

            val response = apiService.getToken(
                signature = signature,
                clientKey = clientKey,
                timestamp = timestamp,
                contentType = "application/json",
                requestBody = requestBody,
            )

            if (isSuccessRequest(response.code())) {
                val data = response.body()

                if (data != null) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {

                try {
                    emit(
                        ApiResponse.Error(
                            getErrorMessage(response.errorBody()),
                            code = response.code()
                        )
                    )
                } catch (e: Exception) {
                    emit(
                        ApiResponse.Error(
                            "Kami sedang memeriksa hal ini. Silakan coba lagi atau kembali beberapa saat lagi",
                            code = response.code()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(errorMessage = e.message.toString(), code = 0))
        }
    }

    suspend fun getBalance(
        authorization: String,
        signature: String,
        timestamp: String,
        partnerId: String,
        chanelId: String,
        externalId: String,
        requestBody: RequestBody
    ): Flow<ApiResponse<GetBalanceResponse>> = flow {
        try {

            val response = apiService.getBalance(
                authorization = authorization,
                signature = signature,
                timestamp = timestamp,
                partnerId = partnerId,
                chanelId = chanelId,
                externalId = externalId,
                contentType = "application/json",
                requestBody = requestBody,
            )

            if (isSuccessRequest(response.code())) {
                val data = response.body()

                if (data != null) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } else {

                try {
                    emit(
                        ApiResponse.Error(
                            getErrorMessage(response.errorBody()),
                            code = response.code()
                        )
                    )
                } catch (e: Exception) {
                    emit(
                        ApiResponse.Error(
                            "Kami sedang memeriksa hal ini. Silakan coba lagi atau kembali beberapa saat lagi",
                            code = response.code()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(errorMessage = e.message.toString(), code = 0))
        }
    }

}