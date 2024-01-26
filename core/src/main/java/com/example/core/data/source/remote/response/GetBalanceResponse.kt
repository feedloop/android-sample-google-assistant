package com.example.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class GetBalanceResponse(
    @SerializedName("accountInfos")
    val accountInfos: List<AccountInfo>,
    @SerializedName("accountNo")
    val accountNo: String,
    @SerializedName("additionalInfo")
    val additionalInfo: AdditionalInfo,
    @SerializedName("name")
    val name: String,
    @SerializedName("responseCode")
    val responseCode: String,
    @SerializedName("responseMessage")
    val responseMessage: String
) {
    data class AccountInfo(
        @SerializedName("availableBalance")
        val availableBalance: AvailableBalance,
        @SerializedName("holdAmount")
        val holdAmount: HoldAmount,
        @SerializedName("ledgerBalance")
        val ledgerBalance: LedgerBalance,
        @SerializedName("status")
        val status: String
    ) {
        data class AvailableBalance(
            @SerializedName("currency")
            val currency: String,
            @SerializedName("value")
            val value: String
        )

        data class HoldAmount(
            @SerializedName("currency")
            val currency: String,
            @SerializedName("value")
            val value: String
        )

        data class LedgerBalance(
            @SerializedName("currency")
            val currency: String,
            @SerializedName("value")
            val value: String
        )
    }

    data class AdditionalInfo(
        @SerializedName("accountType")
        val accountType: String,
        @SerializedName("productCode")
        val productCode: String
    )
}