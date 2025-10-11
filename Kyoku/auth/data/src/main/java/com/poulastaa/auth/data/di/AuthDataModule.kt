package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.OnlineFirstIntroRepository
import com.poulastaa.auth.data.usercase.AuthFieldValidator
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.IntroRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthDataModule {
    @Provides
    @ViewModelScoped
    fun provideAuthFieldValidator(): AuthValidator = AuthFieldValidator()

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstIntroRepository(): IntroRepository = OnlineFirstIntroRepository()
}