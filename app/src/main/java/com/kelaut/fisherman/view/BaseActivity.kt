package com.kelaut.fisherman.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kelaut.fisherman.view.transaction.TransactionFragment
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.view.profile.ProfileActivity
import com.kelaut.fisherman.view.service.ServiceFragment
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {

    private val serviceFragment = ServiceFragment.newInstance()
    private val transactionFragment = TransactionFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val menu: Menu = navigation_container.menu
        selectedMenu(menu.getItem(0))

        navigation_container.setOnNavigationItemSelectedListener {
            item: MenuItem -> selectedMenu(item)
            false
        }

        loadFisherman()

        profile_pic_container.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun selectedMenu(menuItem: MenuItem) {
        menuItem.isChecked = true
        when (menuItem.itemId) {
            R.id.navigation_service -> {
                Log.d("BaseActivity", "navigation_service selected")
                addFragments(serviceFragment)
            }
            R.id.navigation_transaction -> {
                Log.d("BaseActivity", "navigation_transaction selected")
                addFragments(transactionFragment)
            }
        }
    }

    private fun addFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    private fun loadFisherman() {
        val auth = FirebaseAuth.getInstance()
        Fisherman.fetchFisherman(auth.currentUser?.uid ?: "") { fisherman ->
            tv_fisherman_name.text = fisherman?.fullName
            if (fisherman?.imageUrl != "") {
                Glide.with(this)
                        .load(fisherman?.imageUrl)
                        .into(iv_fisherman_profile_pic)
            }
        }
    }

}