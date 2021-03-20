package com.kelaut.fisherman.presenter

import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kelaut.fisherman.contract.LoginContract
import com.kelaut.fisherman.util.Status

class LoginPresenter(_view: LoginContract.View) : AppCompatActivity(), LoginContract.Presenter {

    private val TAG = "Login"
    private val view: LoginContract.View = _view
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        if (auth.currentUser != null) {
            Log.d(TAG, "init:auth.currentUser = " + auth.currentUser.email)
            view.navigateToHome()
        }
    }

    override fun isInputValid(email: String, password: String): Boolean {
        var isValid = true

        // email validation
        if (email.isEmpty()) {
            view.onInvalidInput(LoginContract.Field.EMAIL, Status.ErrEmptyField)
            isValid = isValid && false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.onInvalidInput(LoginContract.Field.EMAIL, Status.ErrWrongFormat)
            isValid = isValid && false
        }

        // password validation
        if (password.isEmpty()) {
            view.onInvalidInput(LoginContract.Field.PASSWORD, Status.ErrEmptyField)
            isValid = isValid && false
        }

        return isValid
    }

    override fun login(email: String, password: String) {
        if (isInputValid(email, password)) {
            view.showProgressBar()

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        onLoginSuccess()
                    }.addOnFailureListener {
                        onLoginFailure(Status.ErrCredential)
                    }
        }
    }

    override fun onLoginSuccess() {
        view.hideProgressBar()
        view.showToastStatus(Status.Success)
        view.navigateToHome()
    }

    override fun onLoginFailure(error: Status) {
        view.hideProgressBar()
        view.showToastStatus(Status.ErrCredential)
        view.onInvalidInput(LoginContract.Field.EMAIL, Status.ErrCredential)
        view.onInvalidInput(LoginContract.Field.PASSWORD, Status.ErrCredential)
    }
}