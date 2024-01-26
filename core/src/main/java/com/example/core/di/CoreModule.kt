package com.example.core.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.shared_preferences.ApiPreference
import com.example.core.data.source.local.shared_preferences.UserPreference
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiService
import com.example.core.data.source.repository.UserRepository
import com.example.core.domain.repository.IUserRepository
import com.example.core.domain.usecase.*
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.core.BuildConfig as base

val preferencesModule = module {
    single { getSharedPrefs(androidContext()) }
    single { ApiPreference(get()) }
    single { UserPreference(get()) }
}

fun getSharedPrefs(context: Context): SharedPreferences {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        try {
            val spec = KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            val masterKey = MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()

            EncryptedSharedPreferences
                .create(
                    context,
                    base.BASE_PREFS,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
        } catch (e: Exception) {
            context.getSharedPreferences(base.BASE_PREFS, Context.MODE_PRIVATE)
        }
    } else {
        context.getSharedPreferences(base.BASE_PREFS, Context.MODE_PRIVATE)
    }
}


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(base.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get(), get()) }
    single { LocalDataSource(get(), get()) }
    single<IUserRepository> { UserRepository(get(), get()) }
}

val useCaseModule = module {
    single<IUserUsecase> { UserUseCase(get()) }
}