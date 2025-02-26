package com.poulastaa.main.data.di

import com.poulastaa.core.domain.repository.LocalHomeDatasource
import com.poulastaa.core.domain.repository.LocalLibraryDatasource
import com.poulastaa.main.data.repository.OfflineFirstLibraryDatasource
import com.poulastaa.main.data.repository.OnlineFirstHomeRepository
import com.poulastaa.main.domain.repository.HomeRepository
import com.poulastaa.main.domain.repository.LibraryRepository
import com.poulastaa.main.domain.repository.RemoteHomeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
internal object MainDataViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        local: LocalHomeDatasource,
        remote: RemoteHomeDataSource,
        scope: CoroutineScope,
    ): HomeRepository = OnlineFirstHomeRepository(local, remote, scope)

    @Provides
    @ViewModelScoped
    fun provideLibraryRepository(
        local: LocalLibraryDatasource,
//        remote: RemoteLibraryDatasource,
        scope: CoroutineScope,
    ): LibraryRepository = OfflineFirstLibraryDatasource(local, scope)
}