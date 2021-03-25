package com.kelaut.fisherman.view.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kelaut.fisherman.R
import com.kelaut.fisherman.adapter.ServiceListAdapter
import com.kelaut.fisherman.model.Service
import kotlinx.android.synthetic.main.fragment_service.*
import kotlinx.android.synthetic.main.fragment_service.view.*

class ServiceFragment : Fragment() {

    private val TAG = "ServiceFragment"
    private lateinit var adapter: ServiceListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_service, container, false)

        view.btn_add_service.setOnClickListener {
            startActivity(Intent(context, ServiceAddActivity::class.java))
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rv_service_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        return view
    }

    override fun onResume() {
        super.onResume()
        loadServices()
        Log.d(TAG, "On Resume is called")
    }

    private fun loadServices() {
        val fishermanId = FirebaseAuth.getInstance().currentUser?.uid
        if (fishermanId != null) {
            Service.fetchServicesByFisherman(fishermanId) { services ->
                if (services.size > 0) showServices(services)
            }
        }
    }

    private fun showServices(services: ArrayList<Service>) {
        adapter = ServiceListAdapter(services) { service ->
            Log.d(TAG, "Service item clicked: ${service.Id} - ${service.name}")

            val intent = Intent(activity, ServiceDetailsActivity::class.java)
            intent.putExtra("SERVICE_ID", service.Id)
            activity?.startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun showNewestServices() {
        // TODO: Implement later
    }

    private fun showPopularServices() {
        // TODO: Implement later
    }

    companion object {
        fun newInstance(): ServiceFragment {
            val fragment = ServiceFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}