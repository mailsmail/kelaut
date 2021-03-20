package com.kelaut.fisherman.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Service
import kotlinx.android.synthetic.main.item_service.view.*
import kotlinx.android.synthetic.main.service_form.*

class ServiceListAdapter(
        private val serviceList: ArrayList<Service>,
        private val itemClickListener: (Service) -> Unit
) : RecyclerView.Adapter<ServiceListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val itemClickListener: (Service) -> Unit)
        : RecyclerView.ViewHolder(view) {

        fun bindService(service: Service) {
            with(service) {
                itemView.tv_item_name.text = service.name
                itemView.tv_item_rating.text = "${service.rating}"
                itemView.tv_item_operational_count.text = "${service.operationalCount}"

                if (service.imageURL != "") {
                    Glide.with(itemView.context)
                        .load(service.imageURL)
                        .into(itemView.iv_service_image)
                }

                itemView.setOnClickListener { itemClickListener(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int = serviceList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindService(serviceList[position])
    }

}