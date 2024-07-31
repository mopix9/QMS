package com.pankti.webservicewithretrofit.data.network

import com.pankti.webservicewithretrofit.domain.entities.AudioFile
import com.pankti.webservicewithretrofit.domain.entities.CommentDataModel
import com.pankti.webservicewithretrofit.domain.entities.GetNum
import com.pankti.webservicewithretrofit.domain.entities.PostDataModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAPI {

    // without any query params
    @GET("/posts")
    suspend fun getPosts(): List<PostDataModel>

   /* @GET("/rest-3")
    suspend fun getNumber(): List<GetNum>*/

// real api  شرکت

    @GET("/qms/GetVoice")
    suspend fun downloadAudioFile(): AudioFile

    @GET("/qms/GetTurn")
    suspend fun getNumber(): List<GetNum>

//    پروژه من
/*
    @GET("/samples/qms/GetVoice/")
    suspend fun downloadAudioFile(): AudioFile

    @GET("/samples/qms/GetTurn/")
    suspend fun getNumber(): List<GetNum>*/

/*    @GET("/rest-5")
    suspend fun getNumber(): List<GetNum>*/

/*
    @GET("/rest-4")
    suspend fun getNumber(@Header("Authorization") token: String): List<GetNum>

    interface TokenApiService {
        @GET("token")
        suspend fun getToken(): TokenResponse
    }
*/

    @GET("/rest-2")
    suspend fun getOneNumber(): GetNum

    // with path param , it will be added like : /posts/1
    @GET("/posts/{id}")
    suspend fun getPost(@Path("id") id: String): PostDataModel


    @GET("/posts/{id}/comments")
    fun getCommentsForPost(@Path("id") id: String): Call<List<CommentDataModel>>

    // with query param , it will be added like : /comments?postId=1
    @GET("/comments")
    fun getCommentsForPostId(@Query("postId") postId: String): Call<List<CommentDataModel>>

    @POST("/posts")
    fun getPostWithPost(): Call<List<PostDataModel>>

    @DELETE("/posts/{id}")
    fun deletePost(@Path("id") id: String): Call<PostDataModel>

}