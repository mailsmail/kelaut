package com.kelaut.fisherman.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.kelaut.fisherman.view.order.OrderFragment
import com.kelaut.fisherman.R
import com.kelaut.fisherman.model.Fisherman
import com.kelaut.fisherman.view.service.ServiceFragment
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {

    private val serviceFragment = ServiceFragment.newInstance()
    private val orderFragment = OrderFragment.newInstance()

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
    }

    private fun selectedMenu(menuItem: MenuItem) {
        menuItem.isChecked = true
        when (menuItem.itemId) {
            R.id.navigation_service -> {
                Log.d("BaseActivity", "navigation_service selected")
                addFragments(serviceFragment)
            }
            R.id.navigation_order -> {
                Log.d("BaseActivity", "navigation_order selected")
                addFragments(orderFragment)
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
        }
    }

}