package com.example.core.data.source.local

import com.example.core.data.source.local.shared_preferences.ApiPreference
import com.example.core.data.source.local.shared_preferences.UserPreference
import com.example.core.domain.model.User
import com.example.core.domain.model.UserLocation
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import javax.crypto.SecretKey

class LocalDataSource(
    private val apiPreference: ApiPreference,
    private val userPreference: UserPreference
) {
    fun saveAccessToken(accesToken: String) {
        apiPreference.setTokenApi(accesToken)
    }

    fun getAccessToken(): String? = apiPreference.getTokeApi()

    fun saveDataUser(user: User) = userPreference.setUser(user)

    fun getDataUser(): User? = userPreference.getUser()

    fun setUserLocation(userLocation: UserLocation) {
        userPreference.setUserLocation(userLocation)
    }

    fun getUserLocation(): UserLocation? = userPreference.getUserLocation()

    fun userPreferences() = userPreference

    fun setSecretKey(data: SecretKey){
        userPreference.setSecretKey(data.toString())
    }

    fun setInitializationVector(data: ByteArray){
        userPreference.setInitializationVector(data.toString())
    }

    fun getSecretKey(): SecretKey {
        val key = userPreference.getSecretKey()
        val bytesKey = android.util.Base64.decode(key, android.util.Base64.DEFAULT)
        val oisKey = ObjectInputStream(ByteArrayInputStream(bytesKey))
        return oisKey.readObject() as SecretKey
    }

    fun getInitializationVector(): ByteArray {
        val initialisationVector = userPreference.getInitializationVector()
        val bytesIV = android.util.Base64.decode(initialisationVector, android.util.Base64.DEFAULT)
        val oisIV = ObjectInputStream(ByteArrayInputStream(bytesIV))
        return oisIV.readObject() as ByteArray
    }
}