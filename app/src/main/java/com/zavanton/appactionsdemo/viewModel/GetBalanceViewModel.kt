package com.zavanton.appactionsdemo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.GetTokenResponse
import com.example.core.data.source.remote.response.GetBalanceResponse
import com.example.core.domain.usecase.IUserUsecase
import kotlinx.coroutines.launch

class GetBalanceViewModel(
    private val userUseCase: IUserUsecase
) : ViewModel() {

    private val _getToken = MutableLiveData<Resource<GetTokenResponse>>()
    val getToken: LiveData<Resource<GetTokenResponse>> = _getToken

    private val _getBalance = MutableLiveData<Resource<GetBalanceResponse>>()
    val getBalance: LiveData<Resource<GetBalanceResponse>> = _getBalance

    fun getToken() {
        viewModelScope.launch {
            userUseCase.getToken(
            ).collect {
                _getToken.postValue(it)
            }
        }
    }

    fun getBalance(
        token: String,
    ) {
        viewModelScope.launch {
            userUseCase.getBalance(
                token = token, accountNo = "111231271284142"
            ).collect {
                _getBalance.postValue(it)
            }
        }
    }

}