package com.kelaut.fisherman.view.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.util.Constant
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.android.synthetic.main.activity_service_details.iv_service_image
import kotlinx.android.synthetic.main.service_form.*

class ServiceDetailsActivity : AppCompatActivity() {

    private val TAG = "ServiceDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_service_details)

        val service = intent.extras?.get("SERVICE") as? Service
        if (service != null) {
            bindServiceData(service)
        }

        btn_edit.setOnClickListener {
            val intent = Intent(this@ServiceDetailsActivity, ServiceEditActivity::class.java)
            intent.putExtra("SERVICE", service)
            startActivity(intent)
        }

        sw_service_availability.setOnCheckedChangeListener { _, isChecked ->
            val db = FirebaseFirestore.getInstance()
            db.collection(Constant.SERVICE_COLLECTION).document(service?.Id ?: "")
                .update(Constant.SERVICE_AVAILABILITY, isChecked)
                .addOnSuccessListener {
                    var msg = ""
                    if (isChecked) {
                        msg = "Layanan ${service?.name} berhasil diaktifkan"
                    } else {
                        msg = "Layanan ${service?.name} berhasil diberhentikan"
                    }

                    val toast: Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
                    toast.show()

                    Log.d(TAG, "Service availability successfully updated to ${isChecked}")
                }.addOnFailureListener{
                    Log.d(TAG, "Failed to update service availability to ${isChecked}")
                }
        }

    }

    private fun bindServiceData(service: Service) {
        tv_service_name.text = service.name
        tv_service_desc.text = service.description
        tv_service_type.text = service.type

        sw_service_availability.isChecked = service.isAvailable

        tv_service_price.text = getString(
                R.string.service_price_format,
                String.format("%,d", service.price))
        tv_service_price_desc.text = service.priceDescription

        tv_service_location.text = getString(
                R.string.service_location_format,
                service.location.district,
                service.location.city,
                service.location.province)
        tv_service_location_details.text = service.location.detail

        tv_service_schedule.text = service.operationalSchedule
        tv_service_additional.text = service.additionalService
        tv_service_rating.text = service.rating.toString()

        var opsCountString = service.operationalCount.toString()
        if (service.operationalCount >= 1000) {
            val opsCount = service.operationalCount / 1000
            opsCountString = "${opsCount}K"
        }
        tv_service_operational_count.text = getString(
                R.string.service_operational_count_format,
                opsCountString)

        if (service.imageURL != "") {
            Glide.with(this).load(service.imageURL).into(iv_service_image)
        }
    }

}