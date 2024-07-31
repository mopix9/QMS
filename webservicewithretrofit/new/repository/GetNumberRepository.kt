package com.pankti.webservicewithretrofit.new.repository

import com.pankti.webservicewithretrofit.data.network.ServiceResponse
import com.pankti.webservicewithretrofit.new.api.NumberApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GetNumberRepository @Inject constructor(private val api: NumberApi)  {
    suspend fun GetNumber() {
        try {
            api.getNumber()
        } catch (e: Exception) {
            ServiceResponse(status = "EXCEPTION", message = e.message)
        }
    }

}