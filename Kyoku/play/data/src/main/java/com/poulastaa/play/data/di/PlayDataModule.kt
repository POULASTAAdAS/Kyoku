package com.poulastaa.play.data.di

import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.home.LocalHomeDatasource
import com.poulastaa.core.domain.home.RemoteHomeDatasource
import com.poulastaa.play.data.OnlineFirstHomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
object PlayDataModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        local: LocalHomeDatasource,
        remote: RemoteHomeDatasource,
        applicationScope: CoroutineScope,
    ): HomeRepository = OnlineFirstHomeRepository(
        local = local,
        remote = remote,
        application = applicationScope
    )
}