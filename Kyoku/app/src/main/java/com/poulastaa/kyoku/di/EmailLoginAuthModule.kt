package com.poulastaa.kyoku.di

import com.poulastaa.kyoku.domain.usecase.AuthUseCases
import com.poulastaa.kyoku.domain.usecase.ValidateConformPassword
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import com.poulastaa.kyoku.domain.usecase.ValidatePassword
import com.poulastaa.kyoku.domain.usecase.ValidateUserName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object EmailLoginAuthModule {
    @Provides
    @ViewModelScoped
    fun provideValidateEmail(): ValidateEmail = ValidateEmail()

    @Provides
    @ViewModelScoped
    fun provideValidatePassword(): ValidatePassword = ValidatePassword()

    @Provides
    @ViewModelScoped
    fun provideValidateUserName(): ValidateUserName = ValidateUserName()

    @Provides
    @ViewModelScoped
    fun provideValidateConformPassword(): ValidateConformPassword = ValidateConformPassword()


    @Provides
    @ViewModelScoped
    fun provideUserCases(
        validateEmail: ValidateEmail,
        validateUserName: ValidateUserName,
        validatePassword: ValidatePassword,
        validateConformPassword: ValidateConformPassword
    ): AuthUseCases = AuthUseCases(
        validateEmail = validateEmail,
        validatePassword = validatePassword,
        validateUserName = validateUserName,
        validateConformPassword = validateConformPassword
    )
}