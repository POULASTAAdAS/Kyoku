package com.poulastaa.auth.data.di

import com.poulastaa.auth.data.repository.OnlineFirstEmailSignUpRepository
import com.poulastaa.auth.data.repository.OnlineFirstForgotPasswordRepository
import com.poulastaa.auth.data.repository.intro.OnlineFirstIntroRepository
import com.poulastaa.auth.data.repository.OnlineFirstOtpValidationRepository
import com.poulastaa.auth.data.repository.OnlineFirstResetPasswordRepository
import com.poulastaa.auth.data.repository.intro.PreferencesIntroDatasource
import com.poulastaa.auth.data.usercase.AuthFieldValidator
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.ForgotPasswordRepository
import com.poulastaa.auth.domain.intro.IntroRepository
import com.poulastaa.auth.domain.OtpValidationRepository
import com.poulastaa.auth.domain.ResetPasswordRepository
import com.poulastaa.auth.domain.SingUpRepository
import com.poulastaa.auth.domain.email_signup.EmailSingUpRemoteDatasource
import com.poulastaa.auth.domain.intro.IntroLocalDatasource
import com.poulastaa.auth.domain.intro.IntroRemoteDatasource
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
    fun provideOnlineFirstEmailSignUpRepository(
        remote: EmailSingUpRemoteDatasource,
    ): SingUpRepository = OnlineFirstEmailSignUpRepository(remote)

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