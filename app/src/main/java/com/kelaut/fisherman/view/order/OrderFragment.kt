package com.kelaut.fisherman.view.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kelaut.fisherman.R

class OrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_order, container, false)

        return view
    }

    companion object {
        fun newInstance(): OrderFragment {
            val fragment = OrderFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}