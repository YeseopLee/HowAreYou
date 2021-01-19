package com.example.howareyou.di

import com.example.howareyou.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepoBindModule {

    @Binds
    abstract fun bindAuth(authRepo: AuthRepositoryImpl) : AuthRepository

    @Binds
    abstract fun bindHome(homeRepo: HomeRepositoryImpl) : HomeRepository

    @Binds
    abstract fun bindDetail(detailRepo: DetailRepositoryImpl) : DetailRepository

    @Binds
    abstract fun bindNoti(notiRepo: NotiRepositoryImpl) : NotiRepository
}