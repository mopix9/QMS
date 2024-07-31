package com.pankti.webservicewithretrofit.domain.usecase

import com.pankti.webservicewithretrofit.data.network.ServiceResponse
import com.pankti.webservicewithretrofit.domain.entities.AudioFile
import com.pankti.webservicewithretrofit.domain.entities.GetNum
import com.pankti.webservicewithretrofit.domain.entities.PostDataModel
import com.pankti.webservicewithretrofit.domain.repo.NetworkDataRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response


class NetworkUseCase(var repo: NetworkDataRepository) {
    suspend fun getPosts(): Flow<List<PostDataModel>> = repo.getPosts()
    suspend fun getPost(id: String): Flow<PostDataModel> = repo.getPost(id)
    suspend fun getNumber(): Flow<List<GetNum>> = repo.getNumber()
    suspend fun getOneNumber():Result<GetNum> = repo.getOneNumber()
    suspend fun downloadAudio():AudioFile = repo.downloadAudio()

}