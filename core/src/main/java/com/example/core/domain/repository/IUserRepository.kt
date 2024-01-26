package com.example.core.domain.repository

import com.example.core.data.Resource
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.data.source.remote.response.GetTokenResponse
import com.example.core.domain.model.*
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getDataUser(): Flow<Resource<User>>
    fun saveDataUser(user:User)

    suspend fun getToken(): Flow<Resource<GetTokenResponse>>

    suspend fun getBalance(token:String, accountNo :String): Flow<Resource<GetBalanceResponse>>
}