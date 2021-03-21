package com.kelaut.fisherman.view.transaction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kelaut.fisherman.R
import com.kelaut.fisherman.adapter.TransactionListAdapter
import com.kelaut.fisherman.model.Transaction

class TransactionFragment : Fragment() {

    private val TAG = "TransactionFragment"
    private lateinit var adapter: TransactionListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_transaction, container, false)

        loadTransactions()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rv_transaction_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        return view
    }

    private fun loadTransactions() {
        val fishermanId = FirebaseAuth.getInstance().currentUser?.uid
        if (fishermanId != null) {
            Transaction.fetchTransactionsByFisherman(fishermanId) { transactions ->
                if (transactions.size > 0) showTransactions(transactions)
            }
        }
    }

    private fun showTransactions(transactions: ArrayList<Transaction>) {
        adapter = TransactionListAdapter(transactions) { transaction ->
            Log.d(TAG, "Transaction item clicked: ${transaction.Id}")

            val intent = Intent(activity, TransactionDetailsActivity::class.java)
            intent.putExtra("TRANSACTION", transaction)
            activity?.startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): TransactionFragment {
            val fragment = TransactionFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}