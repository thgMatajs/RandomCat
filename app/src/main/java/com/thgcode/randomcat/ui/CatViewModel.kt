package com.thgcode.randomcat.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.thgcode.randomcat.R
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
    val loadingCat: MutableLiveData<Cat> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val clickListener = View.OnClickListener { loadRandomCat() }

    init {
        loadRandomCat()
    }

    fun loadRandomCat() {
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
        errorMessage.value = null
    }

    private fun onRetrieveRandomCatFinish() {
    }

    private fun onRetrieveRandomCatSuccess(result: Cat) {
        loadingCat.value = result
        loadingVisibility.value = View.GONE

    }

    private fun onRetrieveRandomCatError() {
        errorMessage.value = R.string.cat_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}