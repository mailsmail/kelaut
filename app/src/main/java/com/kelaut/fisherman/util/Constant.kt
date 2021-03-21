package com.kelaut.fisherman.util

object Constant {
    const val PASSWORD_LENGTH = 8

    // Firestore collection name
    const val FISHERMAN_COLLECTION = "fisherman"
    const val TRANSACTION_COLLECTION = "transaction"
    const val SERVICE_COLLECTION = "service"
    const val USER_COLLECTION = "user"

    // Firestore field name
    // -- Service Collection
    const val SERVICE_NAME = "name"
    const val SERVICE_TYPE = "type"
    const val SERVICE_DESC = "description"
    const val SERVICE_FISHERMAN_ID = "fishermanId"
    const val SERVICE_FISHERMAN_NAME = "fishermanName"
    const val SERVICE_IMG_URL = "imageURL"
    const val SERVICE_ADDON = "additionalService"
    const val SERVICE_PRICE = "price"
    const val SERVICE_PRICE_DESC = "priceDescription"
    const val SERVICE_PHONE = "phoneNumber"
    const val SERVICE_LOC = "location"
    const val SERVICE_SCHEDULE = "operationalSchedule"
    const val SERVICE_AVAILABILITY = "isAvailable"
    const val SERVICE_RATING = "rating"
    const val SERVICE_OPS_COUNT = "operationalCount"
    const val SERVICE_CREATED = "createAt"
    const val SERVICE_UPDATED = "updateAt"

    // -- Transaction COllection
    const val TRANSACTION_FISHERMAN_ID = "fishermanId"

    // Image Chooser
    const val CHOOSE_IMAGE = 101

    // Progress
    val SUBMITED = "submited"
    val APPROVED = "approved"
    val DONE = "done"
}