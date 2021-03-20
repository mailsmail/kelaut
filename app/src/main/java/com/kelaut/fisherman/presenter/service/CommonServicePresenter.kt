package com.kelaut.fisherman.presenter.service

import com.kelaut.fisherman.contract.service.CommonServiceContract
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.util.Status

class CommonServicePresenter {

    companion object {

        fun isInputValid(service: Service, view: CommonServiceContract.View): Boolean {
            var isValid = true

            // service name validation
            if (service.name.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.NAME, Status.ErrEmptyField)
                isValid = isValid && false
            }

            // service type validation
            if (service.type.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.TYPE, Status.ErrEmptyField)
                isValid = isValid && false
            }

            // service description validation
            if (service.description.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.DESC, Status.ErrEmptyField)
                isValid = isValid && false
            }

            // service price validation
            if (service.price.equals(0)) {
                view.onInvalidInput(CommonServiceContract.Field.ADDITIONAL, Status.ErrZeroValue)
                isValid = isValid && false
            }

            // service location validation
            if (service.location.detail.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.LOC_DETAIL, Status.ErrEmptyField)
                isValid = isValid && false
            }
            if (service.location.district.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.LOC_DISTRICT, Status.ErrEmptyField)
                isValid = isValid && false
            }
            if (service.location.city.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.LOC_CITY, Status.ErrEmptyField)
                isValid = isValid && false
            }
            if (service.location.province.isEmpty()) {
                view.onInvalidInput(CommonServiceContract.Field.LOC_PROVINCE, Status.ErrEmptyField)
                isValid = isValid && false
            }

            return isValid
        }
    }

}