package com.kelaut.fisherman.view.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.model.Transaction
import com.kelaut.fisherman.model.User
import kotlinx.android.synthetic.main.activity_transaction_details.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailsActivity : AppCompatActivity() {

    private val TAG = "TransactionDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_transaction_details)

        val transaction = intent.extras?.get("TRANSACTION") as? Transaction
        if (transaction != null) {
            bindTransactionData(transaction)
        }
    }

    private fun bindTransactionData(transaction: Transaction) {
        User.fetchUser(transaction.userId) { user ->
            if (user != null) {
                tv_user_name.text = user.userName
                tv_user_phone.text = user.phoneNumber

                if (user.profileImageUrl != "") {
                    Glide.with(this)
                            .load(user.profileImageUrl)
                            .into(iv_user_profile_pic)
                }
            }

            tv_service_name.text = transaction.serviceName
            tv_service_location.text = getString(R.string.service_location_format,
                    transaction.serviceLocation.district,
                    transaction.serviceLocation.city,
                    transaction.serviceLocation.province)

            if (transaction.serviceImageUrl != "") {
                Glide.with(this)
                        .load(transaction.serviceImageUrl)
                        .into(iv_service_image)
            }

            val formatDate = SimpleDateFormat(getString(R.string.date_format_complete), Locale.US)
            tv_transaction_created_at.text = formatDate.format(transaction.createAt)
            tv_transaction_use_at.text = formatDate.format(transaction.useAt)

            tv_transaction_status.text = transaction.progress
            tv_transaction_note.text = transaction.note
            tv_transaction_person_count.text = getString(R.string.transaction_person_count_format,
                    transaction.personCount)

            val totalPrice = transaction.personCount * transaction.servicePrice
            tv_transaction_price_total.text = getString(R.string.transaction_price_format,
                    String.format("%,d", totalPrice))
        }
    }

}