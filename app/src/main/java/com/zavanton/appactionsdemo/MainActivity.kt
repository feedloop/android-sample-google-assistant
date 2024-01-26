package com.zavanton.appactionsdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.data.source.remote.response.GetTokenResponse
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.AssistActionBuilder
import com.zavanton.appactionsdemo.databinding.ActivityMainBinding
import com.zavanton.appactionsdemo.features.HomeFragment
import com.zavanton.appactionsdemo.features.PaymentFragment
import com.zavanton.appactionsdemo.viewModel.GetBalanceViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: GetBalanceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        handleIntent(intent)

        viewModel.getToken.observe(this, getTokenObserver)
        viewModel.getBalance.observe(this, getBalanceObserver)
        setup()
    }

    private val getTokenObserver =
        Observer<Resource<GetTokenResponse>> { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.loading.isVisible = false
                    Log.e("dataToken",resource.errorCode.toString())
                    Log.e("dataToken",resource.message.toString())
                }
                is Resource.Loading -> {
                    binding.loading.isVisible = true
                }
                is Resource.Success -> {
                    binding.loading.isVisible = false
                   try {
                       Log.e("dataToken",resource.data.toString())
                       Log.e("dataToken",resource.data?.accessToken.toString())
                   }catch (e:Exception){

                   }

                    binding.tv.text = resource.data?.accessToken.toString()
                    if (resource.data?.accessToken!=null){
                        viewModel.getBalance(resource.data?.accessToken.toString())
                    }
                }
            }
        }

    private val getBalanceObserver =
        Observer<Resource<GetBalanceResponse>> { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.loading.isVisible = false
                }
                is Resource.Loading -> {
                    binding.loading.isVisible = true
                }
                is Resource.Success -> {
                    binding.loading.isVisible = false
                   try {
                       Log.e("dataToken",resource.data.toString())
                   }catch (e:Exception){

                   }
                    binding.tv.text = resource.data?.accountInfos?.get(0)?.availableBalance?.value.toString()
                }
            }
        }

    fun setup() {
        binding.logo.setOnClickListener {
            viewModel.getToken()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            handleDeepLink(intent)
        } else {
            Log.d("zavanton", "zavanton - no deep link, show some UI")
        }
    }

    private fun handleDeepLink(intent: Intent) {
        Log.d("zavanton", "zavanton - intent.data: ${intent.data}")
        var isActionHandled = true

        when (intent.data?.path) {
            Deeplink.PAYMENT -> {
                val transferMode = intent.data?.getQueryParameter(Params.TRANSFER_MODE).orEmpty()
                val transferValue = intent.data?.getQueryParameter(Params.TRANSFER_VALUE).orEmpty()
                val transferCurrency =
                    intent.data?.getQueryParameter(Params.TRANSFER_CURRENCY).orEmpty()
                val transferOrigin =
                    intent.data?.getQueryParameter(Params.TRANSFER_ORIGIN).orEmpty()
                val transferDestination =
                    intent.data?.getQueryParameter(Params.TRANSFER_DESTINATION).orEmpty()
                val transferOriginProvider =
                    intent.data?.getQueryParameter(Params.TRANSFER_ORIGIN_PROVIDER).orEmpty()
                val transferDestinationProvider =
                    intent.data?.getQueryParameter(Params.TRANSFER_DESTINATION_PROVIDER).orEmpty()

                startFragment(
                    PaymentFragment.newInstance(
                        transferMode, transferValue,
                        transferCurrency, transferOrigin,
                        transferDestination, transferOriginProvider, transferDestinationProvider
                    )
                )
            }

            else -> {
                startFragment(HomeFragment.newInstance())
                isActionHandled = false
            }
        }
        notifyGoogleAssistant(isActionHandled)
    }

    private fun startFragment(fragment: Fragment) {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun notifyGoogleAssistant(isActionHandled: Boolean) {
        intent.getStringExtra(Actions.ACTION_TOKEN_EXTRA)?.let { actionToken ->
            val actionStatus = if (isActionHandled) {
                Action.Builder.STATUS_TYPE_COMPLETED
            } else {
                Action.Builder.STATUS_TYPE_FAILED
            }
            val action = AssistActionBuilder()
                .setActionToken(actionToken)
                .setActionStatus(actionStatus)
                .build()

            // Send the end action to the Firebase app indexing.
            FirebaseUserActions.getInstance().end(action)
        }
    }
}