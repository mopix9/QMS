package com.pankti.webservicewithretrofit.domain.repo

import com.pankti.webservicewithretrofit.data.network.RetrofitAPI
import com.pankti.webservicewithretrofit.data.network.ServiceResponse
import com.pankti.webservicewithretrofit.domain.entities.AudioFile
import com.pankti.webservicewithretrofit.domain.entities.GetNum
import com.pankti.webservicewithretrofit.domain.entities.PostDataModel
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response


interface NetworkDataRepository {


    suspend fun getPosts(): Flow<List<PostDataModel>>
    suspend fun getPost(id: String): Flow<PostDataModel>
    suspend fun getNumber():Flow<List<GetNum>>
    suspend fun downloadAudio():AudioFile
    suspend fun getOneNumber():Result<GetNum>

}