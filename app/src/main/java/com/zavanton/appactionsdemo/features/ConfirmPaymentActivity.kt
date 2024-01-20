package com.zavanton.appactionsdemo.features

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zavanton.appactionsdemo.Params
import com.zavanton.appactionsdemo.PinViewActivity
import com.zavanton.appactionsdemo.R
import com.zavanton.appactionsdemo.databinding.ActivityConfirmPaymentBinding

class ConfirmPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmPaymentBinding
    private var amount = ""
    private var mode = ""
    private var currency = ""
    private var origin = ""
    private var destination = ""
    private var originProvider = ""
    private var destinationProvider = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_payment)
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

        val amountFinal = if (amount == "") {
            "0"
        } else amount

        val originFinal: String = if (origin == "") {
            "Rahayuning"
        } else origin

        val destinationFinal: String = if (destination == "") {
            "Aditya Putra"
        } else destination

        binding.txtBalance.text = Params.BALANCE

        binding.tvAmount.text = amountFinal
        binding.tvAmount2.text = amountFinal
        binding.tvAmount3.text = amountFinal

        binding.tvCurrency.text = currency
        binding.tvOrigin.text = originFinal
        binding.tvDestination.text = destinationFinal
        binding.txtToRek.text = destinationProvider

        binding.btnConf.setOnClickListener {
            startActivity(Intent(this, PinViewActivity::class.java))
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

}
