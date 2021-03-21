package com.kelaut.fisherman.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.kelaut.fisherman.util.Constant
import kotlinx.coroutines.tasks.await

data class Fisherman(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val imageUrl: String = ""
) {

    companion object {

        private val TAG = "FishermanModel"
        private val db = FirebaseFirestore.getInstance()

        fun fetchFisherman(id: String, onFishermanFetched: (Fisherman?) -> Unit) {
            db.collection(Constant.FISHERMAN_COLLECTION).document(id).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val fisherman = document.toObject(Fisherman::class.java)
                            onFishermanFetched(fisherman)
                        }
                    }
        }

        suspend fun getFisherman(id: String) : Fisherman? {
            val snapshot = try {
                db.collection(Constant.FISHERMAN_COLLECTION).document(id).get().await()
            } catch (e: FirebaseFirestoreException) {
                Log.d(TAG, "Failed to get document ", e)
                null
            }

            return snapshot?.toObject(Fisherman::class.java)
        }

    }
}
