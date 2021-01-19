package com.example.howareyou.di

import android.content.Context
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.repository.AuthRepositoryImpl
import com.example.howareyou.repository.DetailRepositoryImpl
import com.example.howareyou.repository.HomeRepositoryImpl
import com.example.howareyou.repository.NotiRepositoryImpl
import com.example.howareyou.views.detail.DetailCommentAdapter
import com.example.howareyou.views.detail.DetailViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideAuthRepo(serviceApi: ServiceApi) = AuthRepositoryImpl(serviceApi)

    @Singleton
    @Provides
    fun provideHomeRepo(serviceApi: ServiceApi) = HomeRepositoryImpl(serviceApi)

    @Singleton
    @Provides
    fun provideDetailRepo(serviceApi: ServiceApi) = DetailRepositoryImpl(serviceApi)

    @Singleton
    @Provides
    fun provideNotiRepo(serviceApi: ServiceApi) = NotiRepositoryImpl(serviceApi)

//    @Provides
//    fun provideDetailViewModel(context: Context) = DetailViewModel()

}