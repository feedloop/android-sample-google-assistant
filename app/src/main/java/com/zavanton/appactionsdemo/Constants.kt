package com.zavanton.appactionsdemo

object Deeplink {
    const val INVOICE = "/invoice"
    const val CARD_LIST = "/card-list"
    const val ACCOUNT_LIST = "/account-list"
    const val SEARCH = "/search"
    const val PAYMENT = "/payment"
    const val FINANCIAL_SERVICES = "/services"
}

object Actions {
    const val ACTION_TOKEN_EXTRA = "actions.fulfillment.extra.ACTION_TOKEN"
}

object Params {
    const val QUERY = "query"

    const val TRANSFER_MODE = "transferMode"
    const val TRANSFER_VALUE = "value"
    const val TRANSFER_CURRENCY = "currency"
    const val TRANSFER_ORIGIN = "moneyTransferOriginName"
    const val TRANSFER_DESTINATION = "moneyTransferDestinationName"
    const val TRANSFER_ORIGIN_PROVIDER = "moneyTransferOriginProvidername"
    const val TRANSFER_DESTINATION_PROVIDER = "moneyTransferDestinationProvidername"

    const val SERVICE_NAME = "service"

    const val BALANCE = "1.500.000"
}
