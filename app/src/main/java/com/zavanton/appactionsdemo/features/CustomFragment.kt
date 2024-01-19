package com.zavanton.appactionsdemo.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zavanton.appactionsdemo.R
import com.zavanton.appactionsdemo.databinding.FragmentCustomBinding

class CustomFragment : Fragment() {

    companion object {
        private const val SERVICE = "SERVICE"

        fun newInstance(serviceName: String): CustomFragment =
            CustomFragment().also {
                it.arguments = Bundle().apply {
                    putString(SERVICE, serviceName)
                }
            }
    }

    private val serviceName by lazy { arguments?.getString(SERVICE).orEmpty() }

    private lateinit var binding: FragmentCustomBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvService.text = getString(R.string.service_name, serviceName)
    }
}
