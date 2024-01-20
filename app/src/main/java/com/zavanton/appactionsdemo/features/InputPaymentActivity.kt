package com.zavanton.appactionsdemo.features

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zavanton.appactionsdemo.MainActivity
import com.zavanton.appactionsdemo.PinViewActivity
import com.zavanton.appactionsdemo.R
import com.zavanton.appactionsdemo.databinding.ActivityConfirmPaymentBinding
import com.zavanton.appactionsdemo.databinding.ActivityInputPaymentBinding
import com.zavanton.appactionsdemo.databinding.ActivityPinviewBinding
import com.zavanton.appactionsdemo.databinding.FragmentPaymentBinding

class InputPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputPaymentBinding
    private var amount = ""
    private var amountFinal = ""
    private var mode = ""
    private var currency = ""
    private var origin = ""
    private var destination = ""
    private var originProvider = ""
    private var destinationProvider = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_payment)
        init()
    }

    private fun init() {
        amount = intent.getStringExtra("amount").toString()
        mode = intent.getStringExtra("mode").toString()
        currency = intent.getStringExtra("currency").toString()
        origin = intent.getStringExtra("origin").toString()
        destination = intent.getStringExtra("destination").toString()
        originProvider = intent.getStringExtra("originProvider").toString()
        destinationProvider = intent.getStringExtra("destinationProvider").toString()

        binding.textFieldInput.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                amountFinal = s.toString()
            }
        })

        binding.textFieldInput.editText?.requestFocus()
        binding.btnConf.setOnClickListener {
            intentPayment()
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun intentPayment() {
        this.let {
            val intent = Intent(it, ConfirmPaymentActivity::class.java)
            intent.putExtra("amount", amountFinal)
            intent.putExtra("mode", mode)
            intent.putExtra("currency", currency)
            intent.putExtra("origin", origin)
            intent.putExtra("destination", destination)
            intent.putExtra("originProvider", originProvider)
            intent.putExtra("destinationProvider", destinationProvider)
            it.startActivity(intent)
        }
    }

}
