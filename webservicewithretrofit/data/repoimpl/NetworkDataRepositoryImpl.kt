package com.pankti.webservicewithretrofit.data.repoimpl

import com.pankti.webservicewithretrofit.data.network.RetrofitAPI
import com.pankti.webservicewithretrofit.domain.entities.AudioFile
import com.pankti.webservicewithretrofit.domain.entities.GetNum
import com.pankti.webservicewithretrofit.domain.entities.PostDataModel
import com.pankti.webservicewithretrofit.domain.repo.NetworkDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class NetworkDataRepositoryImpl @Inject constructor(private val api: RetrofitAPI) : NetworkDataRepository {

    override suspend fun getPosts(): Flow<List<PostDataModel>> {
        return flow {
            emit(api.getPosts())
        }
    }

/*    override suspend fun getNum():ServiceResponse<GetNum>  {
        return  {
            emit(api.getNumber())
        }
    }*/


    override suspend fun getPost(id: String): Flow<PostDataModel> {
        return flow {
            emit(api.getPost(id))
        }
    }

//get number in list
   override suspend fun getNumber(): Flow<List<GetNum> >{
        return flow {
            emit(api.getNumber())
        }
    }

    override suspend fun downloadAudio(): AudioFile {
        return api.downloadAudioFile()
    }


// to take just a number without list
    override suspend fun getOneNumber(): Result<GetNum> {
        return try {
            val response = api.getOneNumber()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    /*
        override suspend fun getOneNumber(): Flow<GetNum> {
            return flow {
                emit(api.getOneNumber())
            }
        }*/




}