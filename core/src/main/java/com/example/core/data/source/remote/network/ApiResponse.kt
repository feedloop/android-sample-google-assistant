package com.example.core.data.source.remote.network

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T, val code: Int? = null, val message: String? = null) :
        ApiResponse<T>()

    data class Error(val errorMessage: String, var code: Int) : ApiResponse<Nothing>()

    object Empty : ApiResponse<Nothing>()
}