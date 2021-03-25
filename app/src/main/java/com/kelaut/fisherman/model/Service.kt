package com.kelaut.fisherman.model

import android.util.Log
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.kelaut.fisherman.util.Constant
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class Service (
        @get:Exclude var Id: String = "",
        var fishermanId: String = "",
        var fishermanName: String = "",
        var name: String = "",
        var type: String = "",
        var description: String = "",
        var imageURL: String = "",
        var additionalService: String = "",

        var price: Int = 0,
        var priceDescription: String = "",

        var phoneNumber: String = "",
        var location: Location = Location(),

        var operationalSchedule: String = "",
        var available: Boolean = true,
        var rating: Double = 0.0,
        var operationalCount: Int = 0,

        var createAt: Date = Calendar.getInstance().time,
        var updateAt: Date = Calendar.getInstance().time
) : Serializable {

    companion object {

        private val TAG = "ServiceModel"

        fun fetchServicesByFisherman(fishermanId: String, onServicesFetched: (ArrayList<Service>) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(Constant.SERVICE_COLLECTION)
                    .whereEqualTo(Constant.SERVICE_FISHERMAN_ID, fishermanId)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents != null) {
                            var services = ArrayList<Service>()
                            for (document in documents) {
                                val service: Service = document.toObject(Service::class.java)
                                service.Id = document.id
                                services.add(service)
                            }
                            onServicesFetched(services)
                        }
                    }.addOnFailureListener {
                        Log.d(TAG, "Failed to fetch services with fishermanId ${fishermanId}")
                    }
        }

        fun fetchServiceByServiceId(serviceId: String, onServicesFetched: (Service?) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            db.collection(Constant.SERVICE_COLLECTION).document(serviceId).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val service = document.toObject(Service::class.java)
                            service?.Id = serviceId
                            onServicesFetched(service)
                        }
                    }
        }
    }
}