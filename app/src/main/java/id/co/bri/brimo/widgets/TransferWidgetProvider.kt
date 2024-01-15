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

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import com.google.assistant.appactions.widgets.AppActionsWidgetExtension
import id.co.bri.brimo.R


/**
 * Implementation of App Widget functionality.
 */
class TransferWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {

            val currentWidget = TransferWidget(context,
                appWidgetManager,
                appWidgetId,
                R.layout.transfer_widget)

            currentWidget.updateAppWidget()
            var dropoffLocation="Unknown"
            // Extract the name and parameters of the BII from the widget options
            val optionsBundle = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val bii = optionsBundle.getString(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_BII) // "actions.intent.CREATE_TAXI_RESERVATION"
            val params = optionsBundle.getBundle(AppActionsWidgetExtension.EXTRA_APP_ACTIONS_PARAMS)
            if (params != null && params.containsKey("value")) {
                 dropoffLocation = params.getString("value").toString()

                Log.e("Brimo data dropoffLocation", dropoffLocation.toString())
                // Build your RemoteViews with the extracted BII parameter
                // ...
            }

            Log.e("Brimo data optionsBundle", optionsBundle.toString())
            Log.e("Brimo data bii", bii.toString())
            Log.e("Brimo data params", params.toString())
            Log.e("Brimo data dropoffLocation", dropoffLocation.toString())
        }
    }
}

