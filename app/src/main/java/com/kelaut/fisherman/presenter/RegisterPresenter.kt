package com.kelaut.fisherman.presenter

import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kelaut.fisherman.contract.RegisterContract
import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.util.Constant
import com.kelaut.fisherman.util.Status
import com.kelaut.fisherman.util.sha256

class RegisterPresenter(_view: RegisterContract.View) : RegisterContract.Presenter {

    private val view: RegisterContract.View = _view

    override fun isInputValid(fisherman: Fisherman): Boolean {
        var isValid = true

        // fullName validation
        if (fisherman.fullName.isEmpty()) {
            view.onInvalidInput(RegisterContract.Field.FULL_NAME, Status.ErrEmptyField)
            isValid = isValid && false
        }

        // email validation
        if (fisherman.email.isEmpty()) {
            view.onInvalidInput(RegisterContract.Field.EMAIL, Status.ErrEmptyField)
            isValid = isValid && false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(fisherman.email).matches()) {
            view.onInvalidInput(RegisterContract.Field.EMAIL, Status.ErrWrongFormat)
            isValid = isValid && false
        }

        // password validation
        if (fisherman.password.isEmpty()) {
            view.onInvalidInput(RegisterContract.Field.PASSWORD, Status.ErrEmptyField)
            isValid = isValid && false
        } else if (fisherman.password.length < Constant.PASSWORD_LENGTH) {
            view.onInvalidInput(RegisterContract.Field.PASSWORD, Status.ErrTooShort)
            isValid = isValid && false
        }

        // phoneNumber validation
        if (fisherman.phoneNumber.isEmpty()) {
            view.onInvalidInput(RegisterContract.Field.PHONE_NUMBER, Status.ErrEmptyField)
            isValid = isValid && false
        }

        return isValid
    }

    override fun register(fisherman: Fisherman) {
        if (isInputValid(fisherman)) {
            Log.d("RegisterPresenter", "Input is valid")
            view.showProgressBar()

            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(fisherman.email, fisherman.password)
                .addOnCompleteListener{
                    saveFishermanData(auth, fisherman)
                    // Add this so it will not automatically signed in after registration.
                    auth.signOut()
                }.addOnFailureListener{
                    onRegisterFailure(Status.ErrCreatingUser)
                }
        } else {
            Log.d("RegisterPresenter", "Input is invalid")
        }
    }

    override fun onRegisterSuccess() {
        view.hideProgressBar()
        view.showToastStatus(Status.Success)
        Log.d("RegisterPresenter", "Registration success")
    }

    override fun onRegisterFailure(error: Status) {
        view.hideProgressBar()
        view.showToastStatus(error)
        Log.d("RegisterPresenter", "Registration failed")
    }

    private fun saveFishermanData(auth: FirebaseAuth, fisherman: Fisherman) {
        val fishermanId = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection(Constant.FISHERMAN_COLLECTION)
            .document(fishermanId ?: "default")
            .set(fisherman.copy(password = fisherman.password.sha256()))
            .addOnSuccessListener {
                onRegisterSuccess()
            }.addOnFailureListener{
                onRegisterFailure(Status.ErrSavingData)
            }
    }

}