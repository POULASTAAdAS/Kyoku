package com.poulastaa.auth.network.di

import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.network.repository.OkHttpIntroDatasource
import com.poulastaa.core.network.domain.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthNetworkModule {
    @Provides
    @ViewModelScoped
    fun provideAuthIntoRemoteDatasource(
        repo: ApiRepository
    ): IntroRemoteDatasource = OkHttpIntroDatasource(repo)
}