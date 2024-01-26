package com.zavanton.appactionsdemo.app

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.service.voice.VoiceInteractionService
import androidx.slice.SliceManager
import com.example.core.di.networkModule
import com.example.core.di.preferencesModule
import com.example.core.di.repositoryModule
import com.example.core.di.useCaseModule
import com.zavanton.appactionsdemo.di.viewModelModule
import com.zavanton.appactionsdemo.slices.AccountInfoProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@App)
            GlobalContext.loadKoinModules(networkModule)
            GlobalContext.loadKoinModules(repositoryModule)
            GlobalContext.loadKoinModules(preferencesModule)
            GlobalContext.loadKoinModules(useCaseModule)
            loadKoinModules(viewModelModule)
        }
        // Grant the assistant permission when the application is create, it's okay to grant it each time.
        grantAssistantPermissions()
    }

    /**
     * Grant slice permissions to the assistance in order to display slices without user input.
     *
     * Note: this is needed when using AndroidX SliceProvider.
     */
    private fun grantAssistantPermissions() {
        getAssistantPackage()?.let { assistantPackage ->
            val sliceProviderUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AccountInfoProvider.ACCOUNT_SLICE_AUTHORITY)
                .build()

            SliceManager.getInstance(this).grantSlicePermission(assistantPackage, sliceProviderUri)
        }
    }

    /**
     * Find the assistant package name
     */
    private fun getAssistantPackage(): String? {
        val resolveInfoList = packageManager?.queryIntentServices(
            Intent(VoiceInteractionService.SERVICE_INTERFACE), 0
        )
        return resolveInfoList?.firstOrNull()?.serviceInfo?.packageName
    }
}