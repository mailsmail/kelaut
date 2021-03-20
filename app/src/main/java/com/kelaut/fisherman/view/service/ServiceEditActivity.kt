package com.kelaut.fisherman.view.service

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kelaut.fisherman.R
import com.kelaut.fisherman.contract.service.CommonServiceContract
import com.kelaut.fisherman.model.Location
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.presenter.service.ServiceEditPresenter
import com.kelaut.fisherman.util.Constant
import com.kelaut.fisherman.util.Status
import kotlinx.android.synthetic.main.service_form.*

class ServiceEditActivity : AppCompatActivity(), CommonServiceContract.View {

    private val TAG = "ServiceEditActivity"
    private lateinit var presenter: ServiceEditPresenter
    private var service: Service = Service()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_edit)

        presenter = ServiceEditPresenter(this)

        val _service = intent.extras?.get("SERVICE") as? Service
        if (_service != null) {
            fillServiceData(_service)
        }

        iv_service_image.setOnClickListener {
            Log.d(TAG, "Image clicked")
            showImageChooser()
        }

        btn_service_save.setOnClickListener {
            if (_service != null) {
                if (service.imageURL != "") {
                    updateService(_service.Id, service.imageURL)
                } else {
                    updateService(_service.Id, _service.imageURL)
                }
            }
        }
    }

    override fun onInvalidInput(inputField: CommonServiceContract.Field, errType: Status) {
        var errMsg = ""
        if (errType == Status.ErrEmptyField) {
            errMsg = getString(R.string.empty_field)
        }

        when (inputField) {
            CommonServiceContract.Field.NAME -> til_service_name.error = errMsg
            CommonServiceContract.Field.TYPE -> til_service_type.error = errMsg
            CommonServiceContract.Field.DESC -> til_service_desc.error = errMsg
            CommonServiceContract.Field.PRICE -> til_service_price.error = errMsg
            CommonServiceContract.Field.LOC_DETAIL -> til_service_location_detail.error = errMsg
            CommonServiceContract.Field.LOC_DISTRICT -> til_service_location_district.error = errMsg
            CommonServiceContract.Field.LOC_CITY -> til_service_location_city.error = errMsg
            CommonServiceContract.Field.LOC_PROVINCE -> til_service_location_province.error = errMsg
            else -> {}
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
            Status.ErrSavingData -> message = getString(R.string.failed_saving_service)
            Status.Success -> message = getString(R.string.success_edit_service)
            else -> {}
        }

        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun close() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.CHOOSE_IMAGE && resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
            val serviceImageURI = data.data!!
            iv_service_image.setImageURI(serviceImageURI)

            presenter.uploadImage(serviceImageURI) { imageUrl ->
                service.imageURL = imageUrl
            }
        }
    }

    private fun showImageChooser() {
        if (isPermissionGranted()) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, Constant.CHOOSE_IMAGE)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fillServiceData(service: Service) {
        et_service_name.setText(service.name)
        et_service_type.setText(service.type)
        et_service_desc.setText(service.description)
        et_service_additional.setText(service.additionalService)
        et_service_price.setText(service.price.toString())
        et_service_price_desc.setText(service.priceDescription)
        et_service_location_district.setText(service.location.district)
        et_service_location_city.setText(service.location.city)
        et_service_location_province.setText(service.location.province)
        et_service_location_detail.setText(service.location.detail)
        et_service_schedule.setText(service.operationalSchedule)

        if (service.imageURL != "") {
            Glide.with(this).load(service.imageURL).into(iv_service_image)
        }
    }

    private fun updateService(serviceId: String, serviceImageUri: String) {
        service.Id = serviceId
        service.name = et_service_name.text.toString().trim()
        service.type = et_service_type.text.toString().trim()
        service.description = et_service_desc.text.toString().trim()
        service.additionalService = et_service_additional.text.toString().trim()
        service.price = et_service_price.text.toString().trim().toInt()
        service.priceDescription = et_service_price_desc.text.toString().trim()
        service.location = Location(
            et_service_location_district.text.toString().trim(),
            et_service_location_city.text.toString().trim(),
            et_service_location_province.text.toString().trim(),
            et_service_location_detail.text.toString().trim()
        )
        service.operationalSchedule = et_service_schedule.text.toString().trim()
        service.imageURL = serviceImageUri

        presenter.submit(service)
    }

}