package com.kelaut.fisherman.view.transaction

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.model.Transaction
import com.kelaut.fisherman.model.User
import com.kelaut.fisherman.util.Constant
import kotlinx.android.synthetic.main.activity_transaction_details.*
import kotlinx.android.synthetic.main.dialog_transaction_confirmation.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailsActivity : AppCompatActivity() {

    private val TAG = "TransactionDetailsActivity"

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_transaction_details)

        val transaction = intent.extras?.get("TRANSACTION") as? Transaction
        if (transaction != null) {
            bindTransactionData(transaction)
        }

        btn_accept_transaction.setOnClickListener {
            if (transaction != null) {
                showDialogConfirmation(transaction.Id)
            }
        }
    }

    private fun showDialogConfirmation(transactionId: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_transaction_confirmation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val accept = dialog.findViewById<Button>(R.id.btn_transaction_accept)
        val cancel = dialog.findViewById<Button>(R.id.btn_transaction_cancel)

        accept.setOnClickListener {
            db.collection(Constant.TRANSACTION_COLLECTION).document(transactionId)
                    .update(Constant.TRANSACTION_PROGRESS, Constant.TRANSACTION_APPROVED)
                    .addOnSuccessListener {
                        dialog.dismiss()
                        finish()
                    }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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

            if (transaction.progress == Constant.TRANSACTION_APPROVED
                    || transaction.progress == Constant.TRANSACTION_DONE) {
                btn_accept_transaction.isEnabled = false
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