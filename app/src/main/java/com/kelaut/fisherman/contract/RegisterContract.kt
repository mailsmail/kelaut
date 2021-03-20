package com.kelaut.fisherman.contract

import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.util.Status

interface RegisterContract {

    enum class Field {
        FULL_NAME,
        EMAIL,
        PHONE_NUMBER,
        PASSWORD,
        PASSWORD_VALIDATION
    }

    interface View {
        fun navigateToLogin()
        fun onInvalidInput(inputField: Field, errType: Status)
        fun showProgressBar()
        fun hideProgressBar()
        fun showToastStatus(status: Status)
    }

    interface Presenter {
        fun isInputValid(fisherman: Fisherman): Boolean
        fun register(fisherman: Fisherman)
        fun onRegisterSuccess()
        fun onRegisterFailure(error: Status)
    }
}