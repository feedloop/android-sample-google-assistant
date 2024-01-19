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
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.widget.RemoteViews
import com.google.assistant.appactions.widgets.AppActionsWidgetExtension
import com.zavanton.appactionsdemo.Params.BALANCE
import com.zavanton.appactionsdemo.R
import java.util.*


/**
 * Class that defines a Stats Widget which provides data on the last activity performed
 * or with a BII it provides the last activity performed of requested activity type
 */
class BalanceWidget(
    private val context: Context,
    private val appWidgetManager: AppWidgetManager,
    private val appWidgetId: Int,
    layout: Int,
) {
    private val views = RemoteViews(context.packageName, layout)
    private val hasBii: Boolean
    private val isFallbackIntent: Boolean
    private val accountName: String
    private val description: String
    private val providerName: String

    init {
        val optionsBundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val bii = optionsBundle.getString(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_BII)
        hasBii = !bii.isNullOrBlank()
        val params = optionsBundle.getBundle(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_PARAMS)
        if (params != null) {
            isFallbackIntent = params.isEmpty
            if (isFallbackIntent) {
                accountName = context.resources.getString(R.string.activity_unknown)
                description = context.resources.getString(R.string.activity_unknown)
                providerName = context.resources.getString(R.string.activity_unknown)
            } else {
                accountName = params.get("accountName") as String
                description = params.get("description") as String
                providerName = params.get("providerName") as String
            }
        } else {
            isFallbackIntent = false
            accountName = context.resources.getString(R.string.activity_unknown)
            description = context.resources.getString(R.string.activity_unknown)
            providerName = context.resources.getString(R.string.activity_unknown)
        }

        Log.e("Brimo data accountName", accountName)
        Log.e("Brimo data description", description)
        Log.e("Brimo data providerName", providerName)
    }

    /**
     * Checks if widget should get requested or last exercise data and updates widget
     * accordingly
     */
    fun updateAppWidget() {
        if (hasBii && !isFallbackIntent) {
            observeAndUpdateRequestedExercise()
        } else observeAndUpdateLastExercise()
    }


    /**
     * Sets title, duration and distance data to widget
     */
    private fun setDataToWidget(
        saldo: String,
    ) {
        views.setTextViewText(
            R.id.appwidgetDistance, "Rp $saldo"
        )
    }

    /**
     * Sets TTS to widget
     */
    private fun setTts(
        speechText: String,
        displayText: String,
    ) {
        val appActionsWidgetExtension: AppActionsWidgetExtension =
            AppActionsWidgetExtension.newBuilder(appWidgetManager)
                .setResponseSpeech(speechText)  // TTS to be played back to the user
                .setResponseText(displayText)  // Response text to be displayed in Assistant
                .build()

        // Update widget with TTS
        appActionsWidgetExtension.updateWidget(appWidgetId)
    }

    /**
     * Formats and sets activity data to Widget
     */
    private fun formatDataAndSetWidget(
    ) {
        // formats date of activity
        val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMM d")
        val formattedDate = DateFormat.format(datePattern, Calendar.getInstance())


        setDataToWidget(BALANCE)

        if (hasBii) {
            // formats speech and display text for Assistant
            // https://developers.google.com/assistant/app/widgets#tts
            val speechText =
                "Berikut informasi saldo anda pada tanggal $formattedDate. adalah sebesar $BALANCE Rupiah."
            val displayText = "Berikut informasi saldo anda pada tanggal $formattedDate"
            setTts(speechText, displayText)
        }
    }

    /**
     * Formats and sets no activity data to Widget
     */
    private fun setNoActivityDataWidget() {
        val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMM d")
        val formattedDate = DateFormat.format(datePattern, Calendar.getInstance())

        setDataToWidget(BALANCE)

        if (hasBii) {
            // formats speech and display text for Assistant
            // https://developers.google.com/assistant/app/widgets#library
            val speechText =
                "Berikut informasi saldo anda pada tanggal $formattedDate. adalah sebesar $BALANCE Rupiah."
            val displayText = "Berikut informasi saldo anda pada tanggal $formattedDate"
            setTts(speechText, displayText)
        }
    }

    /**
     * Instruct the widget manager to update the widget
     */
    private fun updateWidget() {
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    /**
     * Create and observe the last exerciseType activity LiveData.
     */
    private fun observeAndUpdateRequestedExercise() {
        formatDataAndSetWidget()
        updateWidget()
    }


    /**
     * Create and observe the last activity LiveData.
     */
    private fun observeAndUpdateLastExercise() {
        formatDataAndSetWidget()
        updateWidget()
    }

}
