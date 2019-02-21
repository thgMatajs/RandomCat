package com.thgcode.randomcat.injection.component

import com.thgcode.randomcat.injection.module.NetworkModule
import com.thgcode.randomcat.ui.CatViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(catViewModel: CatViewModel)

    @Component.Builder
    interface Builder {

        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}