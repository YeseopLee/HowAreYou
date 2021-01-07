package com.example.howareyou.di

import com.example.howareyou.network.ServiceApi
import com.example.howareyou.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideAuthRepo(serviceApi: ServiceApi) = AuthRepositoryImpl(serviceApi)
}