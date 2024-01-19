package com.zavanton.appactionsdemo.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zavanton.appactionsdemo.R

class InvoiceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_invoice, container, false)
    }

    companion object {
        fun newInstance() = InvoiceFragment()
    }
}
