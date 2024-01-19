package com.zavanton.appactionsdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.zavanton.appactionsdemo.databinding.ActivityPinviewBinding
import kotlinx.coroutines.*

class PinViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPinviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pinview)
    }

    override fun onResume() {
        super.onResume()

        binding.pinDotView.setOnCompletedListener { pin ->
            if (pin == "123456") {
                startActivity(Intent(this, SuccessActivity::class.java))
            } else {
                GlobalScope.launch {
                    delay(500)
                    withContext(Dispatchers.Main) {
                        binding.pinDotView.showErrorAnimation(clearPin = true)
                    }
                }
            }
        }

        binding.pinDotView.setOnBiometricsButtonClickedListener {
            Snackbar.make(binding.pinDotView, "BIOMETRICS", Snackbar.LENGTH_SHORT).show()
        }

        binding.pinDotView.setOnForgotButtonClickedListener {
            Snackbar.make(binding.pinDotView, "FORGOT", Snackbar.LENGTH_SHORT).show()
        }
    }
}