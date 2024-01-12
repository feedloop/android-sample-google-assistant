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

package id.co.bri.brimo.widgets

import id.co.bri.brimo.BiiIntents
import android.appwidget.AppWidgetManager
import android.content.Context
import android.text.format.DateFormat
import android.widget.RemoteViews
import id.co.bri.brimo.R
import id.co.bri.brimo.model.FitActivity
import id.co.bri.brimo.model.FitRepository
import id.co.bri.brimo.observeOnce
import com.google.assistant.appactions.widgets.AppActionsWidgetExtension
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Class that defines a Stats Widget which provides data on the last activity performed
 * or with a BII it provides the last activity performed of requested activity type
 */
class TransferWidget(
    private val context: Context,
    private val appWidgetManager: AppWidgetManager,
    private val appWidgetId: Int,
    layout: Int,
) {
    private val views = RemoteViews(context.packageName, layout)
    private val repository = FitRepository.getInstance(context)
    private val hasBii: Boolean
    private val isFallbackIntent: Boolean
    private val transferMode: String
    private val value: String
    private val currency: String
    private val moneyTransferOriginName: String
    private val moneyTransferDestinationName: String
    private val moneyTransferOriginProvidername: String
    private val moneyTransferDestinationProvidername: String

    init {
        val optionsBundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val bii = optionsBundle.getString(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_BII)
        hasBii = !bii.isNullOrBlank()
        val params = optionsBundle.getBundle(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_PARAMS)
        if (params != null) {
            isFallbackIntent = params.isEmpty
            if (isFallbackIntent) {
                transferMode = context.resources.getString(R.string.activity_unknown)
                value = context.resources.getString(R.string.activity_unknown)
                currency = context.resources.getString(R.string.activity_unknown)
                moneyTransferOriginName = context.resources.getString(R.string.activity_unknown)
                moneyTransferDestinationName =
                    context.resources.getString(R.string.activity_unknown)
                moneyTransferOriginProvidername =
                    context.resources.getString(R.string.activity_unknown)
                moneyTransferDestinationProvidername =
                    context.resources.getString(R.string.activity_unknown)
            } else {
                transferMode = params.get("transferMode") as String
                value = params.get("value") as String
                currency = params.get("currency") as String
                moneyTransferOriginName = params.get("moneyTransferOriginName") as String
                moneyTransferDestinationName = params.get("moneyTransferDestinationName") as String
                moneyTransferOriginProvidername =
                    params.get("moneyTransferOriginProvidername") as String
                moneyTransferDestinationProvidername =
                    params.get("moneyTransferDestinationProvidername") as String
            }
        } else {
            isFallbackIntent = false
            transferMode = context.resources.getString(R.string.activity_unknown)
            value = context.resources.getString(R.string.activity_unknown)
            currency = context.resources.getString(R.string.activity_unknown)
            moneyTransferOriginName = context.resources.getString(R.string.activity_unknown)
            moneyTransferDestinationName = context.resources.getString(R.string.activity_unknown)
            moneyTransferOriginProvidername = context.resources.getString(R.string.activity_unknown)
            moneyTransferDestinationProvidername =
                context.resources.getString(R.string.activity_unknown)
        }
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
        transferMode: String,
        value: String,
        currency: String,
        moneyTransferOriginName: String,
        moneyTransferDestinationName: String,
        moneyTransferOriginProvidername: String,
        moneyTransferDestinationProvidername: String,
    ) {
        views.setTextViewText(
            R.id.txtAmount,
            value
        )
        views.setTextViewText(
            R.id.txtFromName,
            moneyTransferOriginName
        )
        views.setTextViewText(
            R.id.txtToName,
            moneyTransferDestinationName
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
        setDataToWidget(
            transferMode,
            value,
            currency,
            moneyTransferOriginName,
            moneyTransferDestinationName,
            moneyTransferOriginProvidername,
            moneyTransferDestinationProvidername
        )

        if (hasBii) {
            // formats speech and display text for Assistant
            // https://developers.google.com/assistant/app/widgets#tts
            val speechText =
                "Anda akan melakukan transfer dana sebesar $value rupiah kepada $moneyTransferDestinationName"
            val displayText = "Anda akan melakukan transfer dana"
            setTts(speechText, displayText)
        }
    }

    /**
     * Formats and sets no activity data to Widget
     */
    private fun setNoActivityDataWidget() {
        val appwidgetTypeTitleText = context.getString((R.string.widget_no_data))
        val distanceInKm = 0F
        val durationInMin = 0L

        setDataToWidget(
            transferMode,
            value,
            currency,
            moneyTransferOriginName,
            moneyTransferDestinationName,
            moneyTransferOriginProvidername,
            moneyTransferDestinationProvidername
        )


        if (hasBii) {
            // formats speech and display text for Assistant
            // https://developers.google.com/assistant/app/widgets#library
            val speechText =
                "Anda akan melakukan transfer dana sebesar $value rupiah kepada $moneyTransferDestinationName"
            val displayText = "Anda akan melakukan transfer dana"
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

