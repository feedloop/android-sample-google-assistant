package com.example.core.data.source.local.shared_preferences

import android.content.SharedPreferences
import com.example.core.domain.model.User
import com.example.core.domain.model.UserLocation

class UserPreference(private val sharedPreferences: SharedPreferences) {

    fun setUser(user: User) {
        sharedPreferences.edit().apply {
            putInt(DATA_ID_USER, user.id)
            putString(DATA_ID_ROLE, user.idRole)
            putString(DATA_TYPE_FASKES, user.tipeFaskes)
            putString(DATA_NIK, user.nik)
            putString(DATA_FULLNAME, user.namaLengkap)
            putString(HEALTHCENTREPARENTID, user.healthCentreParentId)
            putString(LOCATION_ID, user.locationId)
            putString(DATA_NOMOR_HP, user.nomorTelfon)

            if (user.nomorSTR.isNotEmpty()) {
                putString(DATA_STR, user.nomorSTR)
            }

            apply()
        }
    }

    fun setUserLocation(userLocation: UserLocation) {
        sharedPreferences.edit().apply {
            putString(PROVINCE_ID, userLocation.province_id)
            putString(CITY_ID, userLocation.city_id)
            putString(DISTRICT_ID, userLocation.district_id)

            apply()
        }
    }

    fun getUserLocation(): UserLocation {
        with(sharedPreferences) {
            return UserLocation(
                province_id = getString(PROVINCE_ID, ""),
                city_id = getString(CITY_ID, ""),
                district_id = getString(DISTRICT_ID, "")
            )
        }
    }

    fun setSecretKey(data: String) {
        sharedPreferences.edit().apply {
            putString(SecretKey, data)
            apply()
        }
    }

    fun setInitializationVector(data: String) {
        sharedPreferences.edit().apply {
            putString(InitializationVector, data)
            apply()
        }
    }

    fun getSecretKey(): String? {
        return sharedPreferences.getString(SecretKey, "")
    }

    fun getInitializationVector(): String? {
        return sharedPreferences.getString(InitializationVector, "")
    }


    fun getPreferenceValue(key: String): String? {
        return sharedPreferences.getString(key, "default")
    }

    fun addPreferenceData(key: String, value: String) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }

    }

    fun getUser(): User? =
        with(sharedPreferences) {
            val user = User(
                id = getInt(DATA_ID_USER, -1),
                nik = getString(DATA_NIK, "") ?: "",
                namaLengkap = getString(DATA_FULLNAME, "") ?: "",
                healthCentreParentId = getString(HEALTHCENTREPARENTID, null) ?: "",
                locationId = getString(LOCATION_ID, null) ?: "",
                nomorSTR = getString(DATA_STR, "") ?: "",
                nomorTelfon = getString(DATA_NOMOR_HP, "") ?: "",
                idRole = getString(DATA_ID_ROLE, "") ?: "",
                tipeFaskes = getString(DATA_TYPE_FASKES, "") ?: "",
            )

            if (isValidDataUser(user)) user
            else null
        }

    private fun isValidDataUser(user: User): Boolean =
        (user.id != -1) &&
//                user.nik.isNotEmpty() &&
                user.namaLengkap.isNotEmpty() &&
//                user.nomorTelfon.isNotEmpty() &&
                user.idRole.isNotEmpty()

    companion object {
        private const val DATA_ID_USER = "data_id_user"
        private const val DATA_ID_ROLE = "data_id_role"
        private const val DATA_TYPE_FASKES = "data_type_faskes"
        private const val DATA_NIK = "data_nik"
        private const val DATA_FULLNAME = "data_fullname"
        private const val HEALTHCENTREPARENTID = "healthCentreParentId"
        private const val LOCATION_ID = "location_id"
        private const val DATA_STR = "data_str"
        private const val DATA_NOMOR_HP = "data_no_hp"
        private const val DATA_ID_PUSKESMAS = "data_id_puskesmas"
        private const val DATA_NAMA_PUSKESMAS = "data_nama_puskesmas"
        private const val DATA_KODE_PUSKESMAS = "data_kode_puskesmas"
        private const val DATA_LOCATION_PUSKESMAS = "data_location_puskesmas"
        private const val PROVINCE_ID = "data_province_id"
        private const val CITY_ID = "data_city_id"
        private const val DISTRICT_ID = "data_district_id"
        private const val SecretKey = "SecretKey_"
        private const val InitializationVector = "initializationVector_"
    }
}