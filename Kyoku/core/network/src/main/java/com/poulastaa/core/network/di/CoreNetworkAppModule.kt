package com.poulastaa.core.network.di

import coil3.intercept.Interceptor
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
import com.poulastaa.core.network.data.ImageAuthHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkAppModule {
    @Provides
    @Singleton
    @Named("ImageAuthHeaderInterceptor")
    fun provideImageAuthHeaderInterceptor(
        scope: CoroutineScope,
        ds: PreferencesDatastoreRepository,
    ): Interceptor = ImageAuthHeaderInterceptor(scope, ds)
}