package com.pankti.webservicewithretrofit.new.di

import com.pankti.webservicewithretrofit.new.data.NumberFactResponse
import retrofit2.Response
import retrofit2.http.GET

interface NumbersApiService {
    @GET("/{number}/{type}")
    suspend fun getNumberFact(): Response<NumberFactResponse>
}