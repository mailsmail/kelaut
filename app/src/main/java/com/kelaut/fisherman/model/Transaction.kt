package com.kelaut.fisherman.model

import android.util.Log
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.kelaut.fisherman.util.Constant
import java.io.Serializable
import java.util.*

data class Transaction (
    @get:Exclude var Id: String = "",
    val userId: String = "",

    val fishermanId: String = "",

    val serviceId: String = "",
    val serviceName: String = "",
    val servicePrice: Int = 0,
    val servicePriceDesc: String = "",
    val serviceImageUrl: String = "",
    val serviceLocation: Location = Location(),

    val useAt: Date = Calendar.getInstance().time,
    val personCount: Int = 0,
    val note: String = "",

    var isDone: Boolean = false,
    var progress: String = Constant.SUBMITED,

    val createAt: Date = Calendar.getInstance().time,
    val doneAt: Date = Calendar.getInstance().time
) : Serializable {

    companion object {

        private val TAG = "TransactionModel"

        fun fetchTransactionsByFisherman(fishermanId: String, onTransactionsFetched: (ArrayList<Transaction>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(Constant.TRANSACTION_COLLECTION)
                    .whereEqualTo(Constant.TRANSACTION_FISHERMAN_ID, fishermanId)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            var transactions = ArrayList<Transaction>()
                            for (document in documents) {
                                val transaction: Transaction = document.toObject(Transaction::class.java)
                                transaction.Id = document.id
                                transactions.add(transaction)
                            }
                            onTransactionsFetched(transactions)
                        }
                    }.addOnFailureListener {
                        Log.d(TAG, "Failed to fetch transaction with fishermanId ${fishermanId}")
                    }
        }
    }
}