package com.kelaut.fisherman.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.view.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            Fisherman.fetchFisherman(auth.currentUser.uid) { fisherman ->
                if (fisherman != null) bindProfile(fisherman)
            }
        }

        btn_logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun bindProfile(fisherman: Fisherman) {
        if (fisherman.imageUrl != "") {
            Glide.with(this)
                    .load(fisherman.imageUrl)
                    .into(iv_fisherman_profile_pic)
        }

        tv_fisherman_name.text = fisherman.fullName
        tv_fisherman_phone_number.text = fisherman.phoneNumber
        tv_fisherman_email.text = fisherman.email
    }
}