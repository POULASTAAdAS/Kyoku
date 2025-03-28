package com.poulastaa.explore.data.di

import com.poulastaa.core.domain.repository.LocalAllFromArtistDatasource
import com.poulastaa.explore.data.repository.OnlineFirstAllFromArtistRepository
import com.poulastaa.explore.domain.repository.AllFromArtistRepository
import com.poulastaa.explore.domain.repository.RemoteAllFromArtistDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object ExploreViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideAllFromArtistRepository(
        remote: RemoteAllFromArtistDatasource,
        local: LocalAllFromArtistDatasource,
    ): AllFromArtistRepository = OnlineFirstAllFromArtistRepository(remote, local)
}