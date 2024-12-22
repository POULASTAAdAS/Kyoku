package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.OnlineFirstAuthRepository
import com.poulastaa.auth.data.UserDataValidator
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.RemoteAuthDataSource
import com.poulastaa.core.domain.DatastoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import java.net.CookieManager

@Module
@InstallIn(ViewModelComponent::class)
object AuthDataModule {
    @Provides
    @ViewModelScoped
    fun provideAuthValidator(): AuthValidator = UserDataValidator()

    @Provides
    @ViewModelScoped
    fun provideAuthNetworkRepository(
        ds: DatastoreRepository,
        cookieManager: CookieManager,
        remote: RemoteAuthDataSource,
    ): AuthRepository = OnlineFirstAuthRepository(
        ds = ds,
        remote = remote,
        cookieManager = cookieManager
    )
}