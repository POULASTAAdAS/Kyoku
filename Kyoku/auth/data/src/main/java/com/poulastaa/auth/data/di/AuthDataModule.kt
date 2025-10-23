package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.OnlineFirstForgotPasswordRepository
import com.poulastaa.auth.data.repository.OnlineFirstOtpValidationRepository
import com.poulastaa.auth.data.repository.OnlineFirstResetPasswordRepository
import com.poulastaa.auth.data.repository.intro.OnlineFirstIntroRepository
import com.poulastaa.auth.data.repository.intro.PreferencesIntroDatasource
import com.poulastaa.auth.data.repository.singup.OnlineFirstEmailSignUpRepository
import com.poulastaa.auth.data.repository.singup.PreferencesSingUpDatasource
import com.poulastaa.auth.data.usercase.AuthFieldValidator
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.email_signup.EmailSingUpLocalDatasource
import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.email_signup.SingUpRepository
import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRemoteDatasource
import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRepository
import com.poulastaa.auth.domain.intro.IntroLocalDatasource
import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
import com.poulastaa.auth.domain.intro.IntroRepository
import com.poulastaa.auth.domain.otp.OtpValidationRemoteDataSource
import com.poulastaa.auth.domain.otp.OtpValidationRepository
import com.poulastaa.auth.domain.update_password.ResetPasswordRemoteDatasource
import com.poulastaa.auth.domain.update_password.ResetPasswordRepository
import com.poulastaa.core.domain.repository.PreferencesDatastoreRepository
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
    fun provideLocalIntroDatasource(
        ds: PreferencesDatastoreRepository,
    ): IntroLocalDatasource = PreferencesIntroDatasource(ds)

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstIntroRepository(
        remote: IntroRemoteDatasource,
        local: IntroLocalDatasource,
    ): IntroRepository = OnlineFirstIntroRepository(remote, local)

    @Provides
    @ViewModelScoped
    fun provideEmailSingUpLocalDatasource(
        ds: PreferencesDatastoreRepository,
    ): EmailSingUpLocalDatasource = PreferencesSingUpDatasource(ds)

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstEmailSignUpRepository(
        remote: EmailSingUpRemoteDatasource,
        local: EmailSingUpLocalDatasource,
    ): SingUpRepository = OnlineFirstEmailSignUpRepository(remote, local)

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstForgotPasswordRepository(
        remote: ForgotPasswordRemoteDatasource,
    ): ForgotPasswordRepository = OnlineFirstForgotPasswordRepository(remote)

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstOtpValidationRepository(
        remote: OtpValidationRemoteDataSource,
    ): OtpValidationRepository = OnlineFirstOtpValidationRepository(remote)

    @Provides
    @ViewModelScoped
    fun provideOnlineFirstResetPasswordRepository(
        remote: ResetPasswordRemoteDatasource,
    ): ResetPasswordRepository = OnlineFirstResetPasswordRepository(remote)
}