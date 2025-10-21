package com.poulastaa.auth.network.di

import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRemoteDatasource
import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.domain.otp.OtpValidationRemoteDataSource
import com.poulastaa.auth.domain.update_password.ResetPasswordRemoteDatasource
import com.poulastaa.auth.network.repository.OTPValidationRemoteDatasource
import com.poulastaa.auth.network.repository.OkHttpEmailSingUiDatasource
import com.poulastaa.auth.network.repository.OkHttpForgotPasswordDatasource
import com.poulastaa.auth.network.repository.OkHttpIntroDatasource
import com.poulastaa.auth.network.repository.OkHttpResetPasswordDatasource
import com.poulastaa.core.network.domain.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthNetworkModule {
    @Provides
    @ViewModelScoped
    fun provideIntoRemoteDatasource(
        repo: ApiRepository,
    ): IntroRemoteDatasource = OkHttpIntroDatasource(repo)

    @Provides
    @ViewModelScoped
    fun provideOkHttpEmailSingUiDatasource(
        repo: ApiRepository,
    ): EmailSingUpRemoteDatasource = OkHttpEmailSingUiDatasource(repo)

    @Provides
    @ViewModelScoped
    fun provideOkHttpForgotPasswordDatasource(
        repo: ApiRepository,
    ): ForgotPasswordRemoteDatasource = OkHttpForgotPasswordDatasource(repo)

    @Provides
    @ViewModelScoped
    fun provideOTPValidationRemoteDatasource(
        repo: ApiRepository,
    ): OtpValidationRemoteDataSource = OTPValidationRemoteDatasource(repo)

    @Provides
    @ViewModelScoped
    fun provideOkHttpResetPasswordDatasource(
        repo: ApiRepository,
    ): ResetPasswordRemoteDatasource = OkHttpResetPasswordDatasource(repo)
}