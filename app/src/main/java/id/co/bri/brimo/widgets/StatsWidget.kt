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
import android.util.Log
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
class StatsWidget(
    private val context: Context,
    private val appWidgetManager: AppWidgetManager,
    private val appWidgetId: Int,
    layout: Int,
) {
    private val views = RemoteViews(context.packageName, layout)
    private val repository = FitRepository.getInstance(context)
    private val hasBii: Boolean
    private val isFallbackIntent: Boolean
    private val aboutExerciseName: String
    private val exerciseType: FitActivity.Type

    init {
        val optionsBundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val bii = optionsBundle.getString(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_BII)
        hasBii = !bii.isNullOrBlank()
        val params = optionsBundle.getBundle(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_PARAMS)
        if (params != null) {
            isFallbackIntent = params.isEmpty
            if (isFallbackIntent) {
                aboutExerciseName = context.resources.getString(R.string.activity_unknown)
            } else {
                aboutExerciseName = params.get("forServiceName") as String
            }
        } else {
            isFallbackIntent = false
            aboutExerciseName = context.resources.getString(R.string.activity_unknown)
        }
        exerciseType = FitActivity.Type.find(aboutExerciseName)

        Log.e("Brimo data aboutExerciseName", aboutExerciseName)
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
            R.id.appwidgetDistance,"Rp 1.500.000"
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
        activityStat: FitActivity,
    ) {
        // formats date of activity
        val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMM d")
        val formattedDate = DateFormat.format(datePattern, activityStat.date)


        // formats duration, distance and exercise name for widget
        val durationInMin = TimeUnit.MILLISECONDS.toMinutes(activityStat.durationMs)
        val distanceInKm = (activityStat.distanceMeters / 1000).toFloat()
        val activityExerciseType = activityStat.type.toString()
        val activityExerciseTypeFormatted = activityExerciseType.lowercase()


        setDataToWidget(BiiIntents.Data.SALDO)

        if (hasBii) {
            // formats speech and display text for Assistant
            // https://developers.google.com/assistant/app/widgets#tts
            val speechText = "Berikut informasi saldo anda pada tanggal $formattedDate. adalah sebesar ${BiiIntents.Data.SALDO} Rupiah."
            val displayText = "Berikut informasi saldo anda pada tanggal $formattedDate"
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
        val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMM d")
        val formattedDate = DateFormat.format(datePattern, Calendar.getInstance())

        setDataToWidget(BiiIntents.Data.SALDO)
        val speechText = "Berikut informasi saldo anda pada tanggal $formattedDate. adalah sebesar ${BiiIntents.Data.SALDO} Rupiah."
        val displayText = "Berikut informasi saldo anda pada tanggal $formattedDate"
        setTts(speechText, displayText)

//        if (hasBii) {
//            // formats speech and display text for Assistant
//            // https://developers.google.com/assistant/app/widgets#library
//            val speechText =
//                context.getString(R.string.widget_no_activity_speech, aboutExerciseName)
//            val displayText =
//                context.getString(R.string.widget_no_activity_text)
//
//            setTts(speechText, displayText)
//        }
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
        val activityData = repository.getLastActivities(1, exerciseType)

        activityData.observeOnce { activitiesStat ->
            if (activitiesStat.isNotEmpty()) {
                formatDataAndSetWidget(activitiesStat[0])
                updateWidget()
            } else {
                setNoActivityDataWidget()
                updateWidget()
            }
        }
    }


    /**
     * Create and observe the last activity LiveData.
     */
    private fun observeAndUpdateLastExercise() {
        val activityData = repository.getLastActivities(1)

        activityData.observeOnce { activitiesStat ->
            if (activitiesStat.isNotEmpty()) {
                formatDataAndSetWidget(activitiesStat[0])
                updateWidget()
            } else {
                setNoActivityDataWidget()
                updateWidget()
            }
        }
    }

}

