package com.pankti.webservicewithretrofit.new.api

import com.pankti.webservicewithretrofit.data.network.ServiceResponse
import com.pankti.webservicewithretrofit.domain.entities.GetNum
import retrofit2.http.GET

interface NumberApi {
    @GET("/rest")
    suspend fun getNumber(): ServiceResponse<GetNum>
}