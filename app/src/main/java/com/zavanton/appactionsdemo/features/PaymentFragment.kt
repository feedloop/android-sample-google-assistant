package com.zavanton.appactionsdemo.features

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zavanton.appactionsdemo.MainActivity
import com.zavanton.appactionsdemo.PinViewActivity
import com.zavanton.appactionsdemo.R
import com.zavanton.appactionsdemo.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    companion object {

        private const val MODE = "MODE"
        private const val VALUE = "VALUE"
        private const val CURRENCY = "CURRENCY"
        private const val ORIGIN = "ORIGIN"
        private const val DESTINATION = "DESTINATION"
        private const val ORIGIN_PROVIDER = "ORIGIN_PROVIDER"
        private const val DESTINATION_PROVIDER = "DESTINATION_PROVIDER"

        fun newInstance(
            transferMode: String,
            transferValue: String,
            transferCurrency: String,
            transferOrigin: String,
            transferDestination: String,
            transferOriginProvider: String,
            transferDestinationProvider: String
        ): PaymentFragment {
            return PaymentFragment().also { fragment ->
                fragment.arguments = Bundle().apply {
                    putString(MODE, transferMode)
                    putString(VALUE, transferValue)
                    putString(CURRENCY, transferCurrency)
                    putString(ORIGIN, transferOrigin)
                    putString(DESTINATION, transferDestination)
                    putString(ORIGIN_PROVIDER, transferOriginProvider)
                    putString(DESTINATION_PROVIDER, transferDestinationProvider)
                }
            }
        }
    }

    private lateinit var binding: FragmentPaymentBinding

    private val amount by lazy { arguments?.getString(VALUE).orEmpty() }
    private val mode by lazy { arguments?.getString(MODE).orEmpty() }
    private val currency by lazy { arguments?.getString(CURRENCY).orEmpty() }
    private val origin by lazy { arguments?.getString(ORIGIN).orEmpty() }
    private val destination by lazy { arguments?.getString(DESTINATION).orEmpty() }
    private val originProvider by lazy { arguments?.getString(ORIGIN_PROVIDER).orEmpty() }
    private val destinationProvider by lazy { arguments?.getString(DESTINATION_PROVIDER).orEmpty() }

    private var destinationFinal: String = ""
    private var destinationProviderFinal: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationFinal = if (destination == "") {
            "Aditya Putra"
        } else destination

        binding.appwidgetTypeTitle.text = getString(R.string.desk_payment, destinationFinal)
        binding.tvName1.text = "$destinationFinal (BRI)"
        binding.tvName2.text = "$destinationFinal (Mandiri)"
        binding.tvName3.text = "$destinationFinal (BNI)"

        binding.click1.setOnClickListener {
            destinationProviderFinal = binding.tvNoRek1.text.toString()
            if (amount == "") {
                intentInputPayment()
            } else intentPayment()
        }
        binding.click2.setOnClickListener {
            destinationProviderFinal = binding.tvNoRek2.text.toString()
            if (amount == "") {
                intentInputPayment()
            } else intentPayment()
        }
        binding.click3.setOnClickListener {
            destinationProviderFinal = binding.tvNoRek3.text.toString()
            if (amount == "") {
                intentInputPayment()
            } else intentPayment()
        }
    }

    private fun intentPayment() {
        activity?.let {
            val intent = Intent(it, ConfirmPaymentActivity::class.java)
            intent.putExtra("amount", amount)
            intent.putExtra("mode", mode)
            intent.putExtra("currency", currency)
            intent.putExtra("origin", origin)
            intent.putExtra("destination", destinationFinal)
            intent.putExtra("originProvider", originProvider)
            intent.putExtra("destinationProvider", destinationProviderFinal)
            it.startActivity(intent)
        }
    }

    private fun intentInputPayment() {
        activity?.let {
            val intent = Intent(it, InputPaymentActivity::class.java)
            intent.putExtra("amount", amount)
            intent.putExtra("mode", mode)
            intent.putExtra("currency", currency)
            intent.putExtra("origin", origin)
            intent.putExtra("destination", destinationFinal)
            intent.putExtra("originProvider", originProvider)
            intent.putExtra("destinationProvider", destinationProviderFinal)
            it.startActivity(intent)
        }
    }
}
