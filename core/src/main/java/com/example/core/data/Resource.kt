package com.example.core.data

sealed class Resource<T>(val data: T? = null, var message: String? = null, var code: Int? = null) {
    class Success<T>(data: T, code: Int? = null, message: String? = null) :
        Resource<T>(data, code = code, message = message)

    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, val errorCode: Int) :
        Resource<T>(data, message, code = errorCode)
}