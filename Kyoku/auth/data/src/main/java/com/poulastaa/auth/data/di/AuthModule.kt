package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.AuthRepositoryImpl
import com.poulastaa.auth.data.UserDataValidator
import com.poulastaa.auth.domain.Validator
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.auth.AuthRepository
import com.poulastaa.core.domain.auth.LocalAuthDatasource
import com.poulastaa.core.domain.auth.RemoteAuthDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import javax.inject.Named

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
        application: CoroutineScope,
    ): AuthRepository = AuthRepositoryImpl(
        remote = remote,
        local = local,
        ds = ds,
        application = application
    )
}