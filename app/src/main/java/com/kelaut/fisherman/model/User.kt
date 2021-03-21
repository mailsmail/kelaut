package com.kelaut.fisherman.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.kelaut.fisherman.util.Constant

data class User(
        @get:Exclude var Id: String = "",
        val userName: String = "",
        val fullName: String = "",
        val email: String = "",
        val address: String = "",
        val phoneNumber: String = "",
        val profileImageUrl: String = "",
        val password: String = ""
) {

    companion object {

        private val TAG = "UserModel"
        private val db = FirebaseFirestore.getInstance()

        fun fetchUser(id: String, onUserFetched: (User?) -> Unit) {
            db.collection(Constant.USER_COLLECTION).document(id).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val user: User? = document.toObject(User::class.java)
                            user?.Id = document.id
                            onUserFetched(user)
                        }
                    }
        }
    }
}