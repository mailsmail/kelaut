package com.kelaut.fisherman.view.service

import android.app.Activity
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
import kotlinx.android.synthetic.main.activity_service_details.tv_service_location
import kotlinx.android.synthetic.main.activity_service_details.tv_service_name

class ServiceDetailsActivity : AppCompatActivity() {

    private val TAG = "ServiceDetailsActivity"
    private val LAUNCH_EDIT_ACTIVITY = 1

    private val db = FirebaseFirestore.getInstance()
    private lateinit var service: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        val serviceId = intent.extras?.get("SERVICE_ID") as? String
        if (serviceId != null) {
            loadServiceData(serviceId)
        }

        btn_back.setOnClickListener {
            finish()
        }

        btn_edit.setOnClickListener {
            val intent = Intent(this@ServiceDetailsActivity, ServiceEditActivity::class.java)
            intent.putExtra("SERVICE", service)
            startActivityForResult(intent, LAUNCH_EDIT_ACTIVITY)
        }

        sw_service_availability.setOnCheckedChangeListener { _, isChecked ->
            db.collection(Constant.SERVICE_COLLECTION).document(service.Id ?: "")
                    .update(Constant.SERVICE_AVAILABILITY, isChecked)
                    .addOnSuccessListener {
                        var msg = ""
                        if (isChecked) {
                            msg = "Layanan ${service.name} berhasil diaktifkan"
                        } else {
                            msg = "Layanan ${service.name} berhasil diberhentikan"
                        }

                        val toast: Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
                        toast.show()

                        Log.d(TAG, "Service availability successfully updated to ${isChecked}")
                    }.addOnFailureListener {
                        Log.d(TAG, "Failed to update service availability to ${isChecked}")
                    }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_EDIT_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                loadServiceData(service.Id)
            }
        }
    }

    private fun loadServiceData(serviceId: String) {
        Service.fetchServiceByServiceId(serviceId) { _service ->
            if (_service != null) {
                service = _service
                bindServiceData(_service)
            }
        }
    }

    private fun bindServiceData(_service: Service) {
        tv_service_name.text = _service.name
        tv_service_desc.text = _service.description
        tv_service_type.text = _service.type

        sw_service_availability.isChecked = _service.available

        tv_service_price.text = getString(
                R.string.service_price_format,
                String.format("%,d", _service.price))
        tv_service_price_desc.text = _service.priceDescription

        tv_service_location.text = getString(
                R.string.service_location_format,
                _service.location.district,
                _service.location.city,
                _service.location.province)
        tv_service_location_details.text = _service.location.detail

        tv_service_schedule.text = _service.operationalSchedule
        tv_service_additional.text = _service.additionalService
        tv_service_rating.text = _service.rating.toString()

        var opsCountString = _service.operationalCount.toString()
        if (_service.operationalCount >= 1000) {
            val opsCount = _service.operationalCount / 1000
            opsCountString = "${opsCount}K"
        }
        tv_service_operational_count.text = getString(
                R.string.service_operational_count_format,
                opsCountString)

        if (_service.imageURL != "") {
            Glide.with(this).load(_service.imageURL).into(iv_service_image)
        }
    }

}