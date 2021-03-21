package com.kelaut.fisherman.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Transaction
import com.kelaut.fisherman.model.User
import kotlinx.android.synthetic.main.item_transaction.view.*
import java.text.SimpleDateFormat

class TransactionListAdapter(
        private val transactionList: ArrayList<Transaction>,
        private val itemClickListener: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val itemClickListener: (Transaction) -> Unit)
            : RecyclerView.ViewHolder(view) {

        fun bindTransaction(transaction: Transaction) {
            with(transaction) {
                User.fetchUser(transaction.userId) { user ->
                    itemView.tv_item_service_name.text = transaction.serviceName
                    itemView.tv_item_user_name.text = user?.userName
                    itemView.tv_item_person.text = transaction.personCount.toString()

                    var format = SimpleDateFormat("dd MMM yyy 'pukul' HH:mm")
                    itemView.tv_item_use_at.text = format.format(transaction.useAt)

                    format = SimpleDateFormat("dd/MM/yyy")
                    itemView.tv_item_created_at.text = format.format(transaction.createAt)

                    if (user?.profileImageUrl != "") {
                        Glide.with(itemView.context)
                                .load(user?.profileImageUrl)
                                .into(itemView.iv_user_profile_image)
                    }
                }

                itemView.setOnClickListener { itemClickListener(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindTransaction(transactionList[position])
    }

}