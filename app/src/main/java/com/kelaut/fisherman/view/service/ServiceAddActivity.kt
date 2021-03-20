package com.kelaut.fisherman.view.service

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kelaut.fisherman.R
import com.kelaut.fisherman.contract.service.CommonServiceContract
import com.kelaut.fisherman.model.Location
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.presenter.service.ServiceAddPresenter
import com.kelaut.fisherman.util.Status
import kotlinx.android.synthetic.main.service_form.*
import kotlinx.android.synthetic.main.service_form.pb_registration

class ServiceAddActivity : AppCompatActivity(), CommonServiceContract.View {

    private lateinit var presenter: ServiceAddPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_add)

        presenter = ServiceAddPresenter(this)

        btn_service_save.setOnClickListener {
            val service = Service(
                name = et_service_name.text.toString(),
                type = et_service_type.text.toString(),
                description = et_service_desc.text.toString(),
                additionalService = et_service_additional.text.toString(),
                price = et_service_price.text.toString().toInt(),
                priceDescription = et_service_price_desc.text.toString(),
                location = Location(
                    et_service_location_district.text.toString(),
                    et_service_location_city.text.toString(),
                    et_service_location_province.text.toString(),
                    et_service_location_detail.text.toString()
                ),
                operationalSchedule = et_service_schedule.text.toString()
            )

            presenter.submit(service)
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
}