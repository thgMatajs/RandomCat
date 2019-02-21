package com.thgcode.randomcat.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.thgcode.randomcat.base.BaseViewModel
import com.thgcode.randomcat.model.Cat
import com.thgcode.randomcat.network.CatApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CatViewModel : BaseViewModel() {

    @Inject
    lateinit var catApi: CatApi

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val loadingCat: MutableLiveData<String> = MutableLiveData()

    private fun loadRandomCat() {
        subscription = catApi.getRandomCat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveRandomCatStart() }
            .doOnTerminate { onRetrieveRandomCatFinish() }
            .subscribe(
                { result -> onRetrieveRandomCatSuccess(result) },
                { onRetrieveRandomCatError() }
            )
    }

    private fun onRetrieveRandomCatStart() {
        loadingVisibility.value = View.VISIBLE
    }

    private fun onRetrieveRandomCatFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrieveRandomCatSuccess(result: Cat) {
        loadingCat.value = result.file
    }

    private fun onRetrieveRandomCatError() {

    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}