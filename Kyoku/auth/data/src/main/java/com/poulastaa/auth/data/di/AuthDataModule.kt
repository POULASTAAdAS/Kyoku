package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.OnlineFirstEmailSignUpRepository
import com.poulastaa.auth.data.repository.OnlineFirstForgotPasswordRepository
import com.poulastaa.auth.data.repository.OnlineFirstIntroRepository
import com.poulastaa.auth.data.repository.OnlineFirstOtpValidationRepository
import com.poulastaa.auth.data.repository.OnlineFirstResetPasswordRepository
import com.poulastaa.auth.data.usercase.AuthFieldValidator
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.ForgotPasswordRepository
import com.poulastaa.auth.domain.IntroRepository
import com.poulastaa.auth.domain.OtpValidationRepository
import com.poulastaa.auth.domain.ResetPasswordRepository
import com.poulastaa.auth.domain.SingUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthDataModule {
    @Provides
    @ViewModelScoped
    fun provideAuthFieldValidator(): AuthValidator = AuthFieldValidator()

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstIntroRepository(

    ): IntroRepository = OnlineFirstIntroRepository()

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstEmailSignUpRepository(

    ): SingUpRepository = OnlineFirstEmailSignUpRepository()

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstForgotPasswordRepository(

    ): ForgotPasswordRepository = OnlineFirstForgotPasswordRepository()

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstOtpValidationRepository(): OtpValidationRepository =
        OnlineFirstOtpValidationRepository()

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstResetPasswordRepository(): ResetPasswordRepository =
        OnlineFirstResetPasswordRepository()
}