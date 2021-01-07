package com.example.howareyou.di

import com.example.howareyou.repository.AuthRepository
import com.example.howareyou.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepoBindModule {

    @Binds
    abstract fun bindAuth(authRepo: AuthRepositoryImpl) : AuthRepository

}