package com.poulastaa.core.data.di

import com.poulastaa.core.data.repository.OnlineFirstCreatePlaylistRepository
import com.poulastaa.core.domain.repository.CreatePlaylistRepository
import com.poulastaa.core.domain.repository.LocalCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.RemoteCreatePlaylistDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
internal object CoreDataModule {
    @Provides
    @ViewModelScoped
    fun provideCreatePlaylistRepository(
        remote: RemoteCreatePlaylistDatasource,
        local: LocalCreatePlaylistDatasource,
        scope: CoroutineScope,
    ): CreatePlaylistRepository = OnlineFirstCreatePlaylistRepository(
        remote = remote,
        local = local,
        scope = scope
    )
}