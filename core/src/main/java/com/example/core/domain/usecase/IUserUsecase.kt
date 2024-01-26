package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.data.source.remote.response.GetTokenResponse
import com.example.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IUserUsecase {
    suspend fun alreadyLoggedIn(): Flow<Boolean>
    suspend fun getDataUser(): Flow<Resource<User>>

    suspend fun getToken(): Flow<Resource<GetTokenResponse>>

    suspend fun getBalance(token:String, accountNo :String): Flow<Resource<GetBalanceResponse>>
}