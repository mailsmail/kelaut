package com.kelaut.fisherman.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelaut.fisherman.R
import com.kelaut.fisherman.contract.LoginContract
import com.kelaut.fisherman.presenter.LoginPresenter
import com.kelaut.fisherman.util.Status
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginPresenter = LoginPresenter(this)

        btn_login.setOnClickListener {
            loginPresenter.login(et_email.text.toString(), et_password.text.toString())
        }

        tv_register.setOnClickListener {
            navigateToRegister()
        }
    }

    override fun navigateToHome() {
        startActivity(Intent(this, BaseActivity::class.java))
    }

    override fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onInvalidInput(inputField: LoginContract.Field, errType: Status) {
        when (inputField) {
            LoginContract.Field.EMAIL -> {
                var errMsg = ""
                if (errType == Status.ErrEmptyField) {
                    errMsg = getString(R.string.empty_email)
                } else if (errType == Status.ErrWrongFormat) {
                    errMsg = getString(R.string.invalid_format_email)
                } else if (errType == Status.ErrCredential) {
                    errMsg = getString(R.string.invalid_email)
                }
                til_email.error = errMsg
            }
            LoginContract.Field.PASSWORD -> {
                var errMsg = ""
                if (errType == Status.ErrEmptyField) {
                    errMsg = getString(R.string.empty_password)
                } else if ( errType == Status.ErrCredential) {
                    errMsg = getString(R.string.invalid_password)
                }
                til_password.error = errMsg
            }
        }
    }

    override fun showProgressBar() {
        pb_login.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pb_login.visibility = View.GONE
    }

    override fun showToastStatus(status: Status) {
        var message = ""
        when (status) {
            Status.ErrCredential -> message = getString(R.string.failed_login)
            Status.Success -> message = getString(R.string.success_login)
            else -> message = ""
        }

        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}