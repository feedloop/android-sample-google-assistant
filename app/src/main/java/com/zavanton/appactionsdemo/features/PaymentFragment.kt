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
        val amountFinal = if (amount == "") {
            "0"
        } else amount

        val originFinal: String
        if (origin == "") {
            originFinal = "-"
            binding.txtFromRek.text = "-"
        } else originFinal = origin

        val destinationFinal: String
        if (destination == "") {
            destinationFinal = "-"
            binding.txtToRek.text = "-"
        } else destinationFinal = destination

        binding.tvAmount.text = amountFinal
        binding.tvAmount2.text = amountFinal
        binding.tvAmount3.text = amountFinal
//        binding.tvMode.text = getString(R.string.transfer_mode, mode)
        binding.tvCurrency.text = currency
        binding.tvOrigin.text = originFinal
        binding.tvDestination.text = destinationFinal
//        binding.tvOriginProvider.text = getString(R.string.transfer_origin_provider, originProvider)
//        binding.tvDestinationProvider.text = getString(R.string.transfer_destination_provider, destinationProvider)

        binding.btnConf.setOnClickListener {
            activity?.let {
                val intent = Intent(it, PinViewActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.btnCancel.setOnClickListener {
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }
    }
}
