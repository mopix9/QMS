package com.pankti.webservicewithretrofit.data.network

class ServiceResponse<T> (
    var data :List<T>? = null,
    var status:String?= null,
    var message:String?= null,
    var totalCount:Long? = null
        )