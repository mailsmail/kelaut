package com.kelaut.fisherman.presenter.service

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kelaut.fisherman.contract.service.CommonServiceContract
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.util.Constant
import com.kelaut.fisherman.util.Status

class ServiceEditPresenter(_view: CommonServiceContract.View) : CommonServiceContract.Presenter  {
    private val TAG = "EditServicePresenter"
    private val view: CommonServiceContract.View = _view

    override fun isInputValid(service: Service): Boolean {
        return CommonServicePresenter.isInputValid(service, view)
    }

    override fun submit(service: Service) {
        if (isInputValid(service)) {
            Log.d(TAG, "Input is valid")
            view.showProgressBar()

            val db = FirebaseFirestore.getInstance()
            val ref = db.collection(Constant.SERVICE_COLLECTION).document(service.Id)

            ref.update(
                Constant.SERVICE_IMG_URL, service.imageURL,
                Constant.SERVICE_NAME, service.name,
                Constant.SERVICE_TYPE, service.type,
                Constant.SERVICE_DESC, service.description,
                Constant.SERVICE_ADDON, service.additionalService,
                Constant.SERVICE_PRICE, service.price,
                Constant.SERVICE_PRICE_DESC, service.priceDescription,
                Constant.SERVICE_LOC, service.location,
                Constant.SERVICE_SCHEDULE, service.operationalSchedule,
                Constant.SERVICE_UPDATED, service.updateAt
            ).addOnSuccessListener {
                onSubmitSuccess()
            }.addOnFailureListener {
                onSubmitFailure(Status.ErrSavingData)
            }
        }
    }

    override fun onSubmitSuccess() {
        Log.d(TAG, "Successfully edit service.")
        view.hideProgressBar()
        view.showToastStatus(Status.Success)
        view.close()
    }

    override fun onSubmitFailure(error: Status) {
        Log.d(TAG, "Failed to edit service.")
        view.hideProgressBar()
        view.showToastStatus(error)
    }

    fun uploadImage(imageUri: Uri?, onImageUploaded: (String) -> Unit) {
        view.showProgressBar()
        val storageRef = FirebaseStorage.getInstance()
                .getReference("serviceImage/" + System.currentTimeMillis() + ".jpg")

        storageRef.putFile(imageUri!!)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    view.hideProgressBar()

                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        onImageUploaded(downloadUri!!.toString())
                    } else {
                        view.showToastStatus(Status.ErrUploadImage)
                    }
                }
    }


}