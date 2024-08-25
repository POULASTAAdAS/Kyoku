package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.AuthRepositoryImpl
import com.poulastaa.auth.data.UserDataValidator
import com.poulastaa.auth.domain.Validator
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.auth.AuthRepository
import com.poulastaa.core.domain.repository.auth.LocalAuthDatasource
import com.poulastaa.core.domain.repository.auth.RemoteAuthDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import java.net.CookieManager

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
    @Provides
    @ViewModelScoped
    fun provideValidator(): Validator = UserDataValidator()

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(
        remote: RemoteAuthDatasource,
        local: LocalAuthDatasource,
        ds: DataStoreRepository,
        cookieManager: CookieManager,
        application: CoroutineScope,
    ): AuthRepository = AuthRepositoryImpl(
        remote = remote,
        local = local,
        ds = ds,
        application = application,
        cookieManager = cookieManager
    )
}