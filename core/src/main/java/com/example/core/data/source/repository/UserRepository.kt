package com.example.core.data.source.repository

import android.util.Log
import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.data.source.remote.response.GetTokenResponse
import com.example.core.domain.model.User
import com.example.core.domain.repository.IUserRepository
import com.example.core.utils.Const
import com.example.core.utils.generateRequest
import com.example.core.utils.generateSHA256withRSA
import com.example.core.utils.getPrivateKey
import com.example.core.utils.getTimeStamp
import com.example.core.utils.mapGetBalanceToRequestBody
import com.example.core.utils.mapGetBalanceToRequestBodyString
import com.example.core.utils.mapGetTokenToRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IUserRepository {

    private var timeStamp = getTimeStamp()

    override suspend fun getToken(): Flow<Resource<GetTokenResponse>> =
        flow {
            emit(Resource.Loading())

            val requestBody = mapGetTokenToRequestBody()
            timeStamp = getTimeStamp()

            remoteDataSource.getToken(
                signature = generateSHA256withRSA(
                    "${Const.CLIENT_KEY}|${timeStamp}".toByteArray(),
                    getPrivateKey(Const.PRIVATE_KEY)
                ),
                clientKey = Const.CLIENT_KEY,
                timestamp = timeStamp,
                requestBody = requestBody,
            ).collect { response ->

                when (response) {
                    is ApiResponse.Empty -> emit(
                        Resource.Error(
                            message = "Terjadi kesalahan pada koneksi, silahkan coba kembali",
                            errorCode = 0
                        )
                    )

                    is ApiResponse.Error -> {
                        Log.e("Errorcode", response.code.toString())
                        emit(
                            Resource.Error(
                                message = response.errorMessage,
                                errorCode = response.code
                            )
                        )
                    }

                    is ApiResponse.Success -> emit(Resource.Success(response.data))
                    else -> {}
                }
            }
        }

    override suspend fun getBalance(
        token: String,
        accountNo: String,
    ): Flow<Resource<GetBalanceResponse>> =
        flow {
            emit(Resource.Loading())

            val requestBody = mapGetBalanceToRequestBody(accountNo = accountNo)
            val bodyRequest = mapGetBalanceToRequestBodyString(accountNo = accountNo)
            val hmac = generateRequest(Const.CLIENT_SECRET,"POST",timeStamp,token,bodyRequest,"/snap/v1.0/balance-inquiry")

            Log.e("dataToken",hmac)

            remoteDataSource.getBalance(
                authorization = "Bearer $token",
                signature = hmac,
                timestamp = timeStamp,
                partnerId = "feedloop",
                chanelId = "SNBPI",
                externalId = "123456789",
                requestBody = requestBody,
            ).collect { response ->

                when (response) {
                    is ApiResponse.Empty -> emit(
                        Resource.Error(
                            message = "Terjadi kesalahan pada koneksi, silahkan coba kembali",
                            errorCode = 0
                        )
                    )

                    is ApiResponse.Error -> {
                        Log.e("Errorcode", response.code.toString())
                        emit(
                            Resource.Error(
                                message = response.errorMessage,
                                errorCode = response.code
                            )
                        )
                    }

                    is ApiResponse.Success -> emit(Resource.Success(response.data))
                    else -> {}
                }
            }
        }

    override suspend fun getDataUser(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        val user = localDataSource.getDataUser()
        if (user != null) emit(Resource.Success(user))
        else emit(Resource.Error("Data user not found", errorCode = 0))
    }

    override fun saveDataUser(user: User) {
        localDataSource.saveDataUser(user)
    }

}