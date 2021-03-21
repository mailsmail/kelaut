package com.kelaut.fisherman.presenter.service

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kelaut.fisherman.contract.service.CommonServiceContract
import com.kelaut.fisherman.model.Service
import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.util.Constant
import com.kelaut.fisherman.util.Status
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ServiceAddPresenter(_view: CommonServiceContract.View) : CommonServiceContract.Presenter {
    private val TAG = "AddServicePresenter"
    private val view: CommonServiceContract.View = _view

    override fun isInputValid(service: Service): Boolean {
        return CommonServicePresenter.isInputValid(service, view)
    }

    override fun submit(service: Service) {
        if (isInputValid(service)) {
            Log.d(TAG, "Input is valid")
            view.showProgressBar()

            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid

            if (userId != null) {
                GlobalScope.launch {
                    val fisherman = Fisherman.getFisherman(userId)

                    db.collection(Constant.SERVICE_COLLECTION)
                        .add(service.copy(
                            fishermanId = userId,
                            fishermanName = fisherman?.fullName ?: "",
                            phoneNumber = fisherman?.phoneNumber ?: "")
                        ).addOnSuccessListener {
                            onSubmitSuccess()
                        }.addOnFailureListener {
                            onSubmitFailure(Status.ErrSavingData)
                        }
                }
            }
        }
    }

    override fun onSubmitSuccess() {
        Log.d(TAG, "Successfully submit new service.")
        view.hideProgressBar()
        view.showToastStatus(Status.Success)
        view.close()
    }

    override fun onSubmitFailure(error: Status) {
        view.hideProgressBar()
        view.showToastStatus(error)
        Log.d(TAG, "Failed to submit new service.")
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