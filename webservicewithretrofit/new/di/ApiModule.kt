package com.pankti.webservicewithretrofit.new.di

import com.pankti.webservicewithretrofit.new.api.NumberApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
     fun provideApi():Retrofit{
        return Retrofit.Builder()
//            .baseUrl("https://10.0.3.2:8080")
//            .baseUrl("https://onlineshop.holosen.net:9090/swagger-ui/")
            .baseUrl("https://www.kpec-co.com/")
//            .client(UnsafeSSLConfig.unsafeOkHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideGetNumberApi(): NumberApi {
        return provideApi().create(NumberApi::class.java)
    }

}