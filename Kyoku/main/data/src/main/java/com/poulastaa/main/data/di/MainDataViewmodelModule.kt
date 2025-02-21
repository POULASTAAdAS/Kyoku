package com.poulastaa.main.data.di

import com.poulastaa.core.domain.repository.LocalHomeDatasource
import com.poulastaa.main.data.repository.OnlineFirstHomeRepository
import com.poulastaa.main.domain.repository.HomeRepository
import com.poulastaa.main.domain.repository.RemoteHomeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object MainDataViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideMainDataRepository(
        local: LocalHomeDatasource,
        remote: RemoteHomeDataSource,
        scope: CoroutineScope,
    ): HomeRepository = OnlineFirstHomeRepository(local, remote, scope)
}