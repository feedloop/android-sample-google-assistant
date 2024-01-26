package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.data.source.remote.response.GetTokenResponse
import com.example.core.domain.model.*
import com.example.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserUseCase(private val userRepository: IUserRepository) : IUserUsecase {
    override suspend fun alreadyLoggedIn(): Flow<Boolean> = flow {
        userRepository.getDataUser().collect { resource ->
            when (resource) {
                is Resource.Error -> emit(false)
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    if (resource.data != null) emit(true)
                    else emit(false)
                }
            }
        }
    }

    override suspend fun getDataUser(): Flow<Resource<User>> =
        userRepository.getDataUser()

    override suspend fun getToken(): Flow<Resource<GetTokenResponse>> =
        userRepository.getToken()

    override suspend fun getBalance(token:String, accountNo :String): Flow<Resource<GetBalanceResponse>> =
        userRepository.getBalance(token = token, accountNo = accountNo)
}