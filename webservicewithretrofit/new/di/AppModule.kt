package com.pankti.webservicewithretrofit.new.di

import com.pankti.webservicewithretrofit.new.api.NumberApi
import com.pankti.webservicewithretrofit.new.repository.GetNumberRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesSliderRepository(api: NumberApi) = GetNumberRepository (api)











}