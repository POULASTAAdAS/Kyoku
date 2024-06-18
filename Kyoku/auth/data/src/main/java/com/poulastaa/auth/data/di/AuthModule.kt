package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.UserDataValidator
import com.poulastaa.auth.domain.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {
    @Provides
    @ViewModelScoped
    fun provideValidator(): Validator = UserDataValidator()
}