package com.poulastaa.view.data.di

import com.poulastaa.view.data.repository.OnlineFirstViewArtistRepository
import com.poulastaa.view.domain.repository.ViewArtistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideViewArtistRepository(): ViewArtistRepository = OnlineFirstViewArtistRepository()
}