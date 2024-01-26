package com.zavanton.appactionsdemo.di

import com.zavanton.appactionsdemo.viewModel.GetBalanceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GetBalanceViewModel(get()) }
}