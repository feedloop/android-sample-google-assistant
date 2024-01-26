package com.example.core.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

fun mapGetTokenToRequestBody(): RequestBody =
    JSONObject().apply {

        put("grantType", "client_credentials")

    }.toString()
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


fun mapGetBalanceToRequestBody(accountNo :String): RequestBody =
    JSONObject().apply {
        put("accountNo", accountNo)
    }.toString()
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

fun mapGetBalanceToRequestBodyString(accountNo: String): String {
    return JSONObject().apply {
        put("accountNo", accountNo)
    }.toString()
}