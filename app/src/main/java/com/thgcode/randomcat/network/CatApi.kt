package com.thgcode.randomcat.network

import com.thgcode.randomcat.model.Cat
import io.reactivex.Observable
import retrofit2.http.GET

interface CatApi {

    @GET("/meow")
    fun getRandomCat(): Observable<Cat>
}