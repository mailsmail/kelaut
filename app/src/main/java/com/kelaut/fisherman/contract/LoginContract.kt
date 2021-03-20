package com.kelaut.fisherman.contract

import com.kelaut.fisherman.util.Status

interface LoginContract {

    enum class Field {
        EMAIL,
        PASSWORD
    }

    interface View {
        fun navigateToHome()
        fun navigateToRegister()
        fun onInvalidInput(inputField: Field, errType: Status)
        fun showProgressBar()
        fun hideProgressBar()
        fun showToastStatus(status: Status)
    }

    interface Presenter {
        fun isInputValid(email: String, password: String): Boolean
        fun login(email: String, password: String)
        fun onLoginSuccess()
        fun onLoginFailure(error: Status)
    }
}