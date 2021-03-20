package com.kelaut.fisherman.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelaut.fisherman.R
import com.kelaut.fisherman.contract.RegisterContract
import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.presenter.RegisterPresenter
import com.kelaut.fisherman.util.Status
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var registerPresenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerPresenter = RegisterPresenter(this)

        btn_signup.setOnClickListener{
            val fisherman = Fisherman(
                et_email.text.toString(),
                et_full_name.text.toString(),
                et_email.text.toString(),
                et_phone_number.text.toString(),
                et_password.text.toString()
            )

            if (et_password.text.toString() == et_password_validation.text.toString()) {
                registerPresenter.register(fisherman)
            } else {
                // Whacky code below
                onInvalidInput(RegisterContract.Field.PASSWORD, Status.ErrNotMatch)
                onInvalidInput(RegisterContract.Field.PASSWORD_VALIDATION, Status.ErrNotMatch)
            }
        }

        tv_login.setOnClickListener {
            Log.d("Register", "Click tv_login")
            navigateToLogin()
        }
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onInvalidInput(inputField: RegisterContract.Field, status: Status) {
        when (inputField) {
            RegisterContract.Field.FULL_NAME -> {
                var errMsg = ""
                if (status == Status.ErrEmptyField) {
                    errMsg = getString(R.string.empty_full_name)
                }
                til_full_name.error = errMsg
            }
            RegisterContract.Field.EMAIL -> {
                var errMsg = ""
                if (status == Status.ErrEmptyField) {
                    errMsg = getString(R.string.empty_email)
                } else if (status == Status.ErrWrongFormat) {
                    errMsg = getString(R.string.invalid_format_email)
                }
                til_email.error = errMsg
            }
            RegisterContract.Field.PHONE_NUMBER -> {
                var errMsg = ""
                if (status == Status.ErrEmptyField) {
                    errMsg = getString(R.string.empty_phone_number)
                }
                til_phone_number.error = errMsg
            }
            RegisterContract.Field.PASSWORD -> {
                var errMsg = ""
                if (status == Status.ErrEmptyField) {
                    errMsg = getString(R.string.empty_password)
                } else if (status == Status.ErrTooShort) {
                    errMsg = getString(R.string.short_password)
                } else if (status == Status.ErrNotMatch) {
                    errMsg = getString(R.string.not_match_password)
                }
                til_password.error = errMsg
            }
            RegisterContract.Field.PASSWORD_VALIDATION -> {
                var errMsg = ""
                if (status == Status.ErrNotMatch) {
                    errMsg = getString(R.string.not_match_password)
                }
                til_password_validation.error = errMsg
            }
        }
    }

    override fun showProgressBar() {
        pb_registration.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pb_registration.visibility = View.GONE
    }

    override fun showToastStatus(status: Status) {
        var message = ""
        when (status) {
            Status.ErrCreatingUser -> message = getString(R.string.failed_create_user)
            Status.ErrSavingData -> message = getString(R.string.failed_saving_data)
            Status.Success -> message = getString(R.string.success_registration)
        }

        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

}