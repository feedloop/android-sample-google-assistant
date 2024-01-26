package com.example.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int = -1,
    val nik: String = "",
    val namaLengkap: String = "",
    val nomorSTR: String = "",
    val nomorTelfon: String = "",
    val healthCentreParentId: String = "",
    val locationId: String = "",
    val idRole: String = "-1",
    val tipeFaskes: String = "-1",
    val desa: String = ""
) : Parcelable

enum class Role(val id: String) {
    NON_NAKES("0"),
    BIDAN("1"),
    DOKTER("2"),
    PERAWAT("3"),
    KADER("14")
}


data class UserLocation(
    @SerializedName("district_id")
    var district_id: String? = "",
    @SerializedName("city_id")
    var city_id: String? = "",
    @SerializedName("province_id")
    var province_id: String? = ""
)
