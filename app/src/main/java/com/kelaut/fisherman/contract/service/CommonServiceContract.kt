package com.kelaut.fisherman.contract.service

import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.util.Status

interface CommonServiceContract {

    enum class Field {
        NAME,
        TYPE,
        DESC,
        ADDITIONAL,
        PRICE,
        PRICE_DESC,
        LOC_DETAIL,
        LOC_DISTRICT,
        LOC_CITY,
        LOC_PROVINCE,
        SCHEDULE,
    }

    interface View {
        fun onInvalidInput(inputField: Field, errType: Status)
        fun showProgressBar()
        fun hideProgressBar()
        fun showToastStatus(status: Status)
        fun close()
    }

    interface Presenter {
        fun isInputValid(service: Service): Boolean
        fun submit(service: Service)
        fun onSubmitSuccess()
        fun onSubmitFailure(error: Status)
    }
}