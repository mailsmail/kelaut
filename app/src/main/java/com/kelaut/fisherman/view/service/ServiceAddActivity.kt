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
import com.kelaut.fisherman.R
import com.kelaut.fisherman.contract.service.CommonServiceContract
import com.kelaut.fisherman.model.Location
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.presenter.service.ServiceAddPresenter
import com.kelaut.fisherman.util.Constant
import com.kelaut.fisherman.util.Status
import kotlinx.android.synthetic.main.service_form.*
import kotlinx.android.synthetic.main.service_form.pb_registration

class ServiceAddActivity : AppCompatActivity(), CommonServiceContract.View {

    private lateinit var presenter: ServiceAddPresenter
    private var service: Service = Service()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_add)

        presenter = ServiceAddPresenter(this)

        iv_service_image.setOnClickListener {
            showImageChooser()
        }

        btn_service_save.setOnClickListener {
            createService()
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
            Status.Success -> message = getString(R.string.success_create_service)
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

    private fun createService() {
        service.name = et_service_name.text.toString()
        service.type = et_service_type.text.toString()
        service.description = et_service_desc.text.toString()
        service.additionalService = et_service_additional.text.toString()
        service.price = et_service_price.text.toString().toInt()
        service.priceDescription = et_service_price_desc.text.toString()
        service.location = Location(
                et_service_location_district.text.toString(),
                et_service_location_city.text.toString(),
                et_service_location_province.text.toString(),
                et_service_location_detail.text.toString()
        )
        service.operationalSchedule = et_service_schedule.text.toString()

        presenter.submit(service)
    }
}