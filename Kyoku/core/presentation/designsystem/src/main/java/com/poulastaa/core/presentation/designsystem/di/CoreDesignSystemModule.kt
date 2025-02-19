package com.poulastaa.core.presentation.designsystem.di

import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ThemChanger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDesignSystemModule {
    @Provides
    @Singleton
    fun provideImageCacheReq(
        ds: DatastoreRepository,
    ): CacheImageReq {
        CacheImageReq.init(ds)
        return CacheImageReq()
    }

    @Provides
    @Singleton
    fun provideThemChangerViewmodel(
        ds: DatastoreRepository,
    ): ThemChanger {
        ThemChanger.init(ds)
        return ThemChanger()
    }
}