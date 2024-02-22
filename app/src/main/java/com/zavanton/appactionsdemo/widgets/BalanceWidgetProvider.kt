/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zavanton.appactionsdemo.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import com.zavanton.appactionsdemo.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException


/**
 * Implementation of App Widget functionality.
 */
class BalanceWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val client = OkHttpClient()
        val url = "https://jsonplaceholder.typicode.com/todos/1" // Ganti dengan URL API yang sesuai

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Tangani kegagalan panggilan API
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Tangani respons dari panggilan API yang berhasil
                    val responseData = response.body?.string()
                    Log.e("Data Widget#", responseData.toString())
                    // Iterasi melalui setiap widget dan update dengan accessToken
                    for (appWidgetId in appWidgetIds) {
                        val currentWidget = BalanceWidget(
                            context,
                            appWidgetManager,
                            appWidgetId,
                            R.layout.balance_widget,
                            responseData ?: "0",
                        )

                        currentWidget.updateAppWidget()
                    }
                    Log.d("BalanceWidgetProvider", "API Response: $responseData")
                    // Proses responseData sesuai kebutuhan
                } else {
                    // Tangani respons yang tidak berhasil
                    Log.e("BalanceWidgetProvider", "API Error: ${response.code}")
                }
            }
        })
    }
}
