package com.thgcode.randomcat.base

import androidx.lifecycle.ViewModel
import com.thgcode.randomcat.injection.component.DaggerViewModelInjector
import com.thgcode.randomcat.injection.component.ViewModelInjector
import com.thgcode.randomcat.injection.module.NetworkModule
import com.thgcode.randomcat.ui.CatViewModel

abstract class BaseViewModel : ViewModel() {

    val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is CatViewModel -> injector.inject(this)
        }
    }

}