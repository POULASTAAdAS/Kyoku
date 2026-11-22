package com.poulastaa.kyoku

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.poulastaa.auth.ui.components.GoogleAuthWrapper
import com.poulastaa.auth.ui.components.registerGoogleAuthWrapper
import com.poulastaa.auth.viewmodels.forgot_password.ForgotPasswordViewmodel
import com.poulastaa.auth.viewmodels.otp.OtpViewmodel
import com.poulastaa.auth.viewmodels.reset_password.ResetPasswordViewmodel
import com.poulastaa.auth.viewmodels.sign_in.SignInViewmodel
import com.poulastaa.auth.viewmodels.sign_up.SignUpViewmodel
import com.poulastaa.common.viewmodels.root.RootViewmodel
import com.poulastaa.kyoku.di.initKoin
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.component.KoinComponent
import org.koin.core.waitAllStartJobs
import org.koin.mp.KoinPlatform

// TODO: THIS APPROACH IS NOT THE RIGHT ONE. PUTTING EVERYTHING AT ONCE, VIEWMODEL MUST BE INITIALIZED AT WHERE IT'S NEEDED.
//  We should use a different approach to provide viewmodels to SwiftUI.

fun initKoinIos() {
    initKoin()
}

fun registerGoogleAuth(wrapper: GoogleAuthWrapper) {
    registerGoogleAuthWrapper(wrapper)
}

/** The wrapper passed to [registerGoogleAuth]; SwiftUI screens start Google auth through it. */
fun googleAuthWrapper(): GoogleAuthWrapper = KoinPlatform.getKoin().get()

/**
 * Gives Swift typed access to the shared viewmodels (Swift cannot call Koin's reified `get()`).
 * Each instance owns one [ViewModelStore], so create one per SwiftUI screen and call [clear]
 * when that screen goes away to cancel the viewmodels' `viewModelScope`.
 */
class IosViewModelProvider : KoinComponent {
    private val viewModelStore = ViewModelStore()

    @OptIn(KoinExperimentalAPI::class)
    private val viewModelProvider: ViewModelProvider by lazy {
        // viewModelModule is loaded with lazyModules(); wait for it before the first lookup.
        getKoin().waitAllStartJobs()

        ViewModelProvider.create(
            store = viewModelStore,
            factory = viewModelFactory {
                initializer { getKoin().get<RootViewmodel>() }
                initializer { getKoin().get<SignInViewmodel>() }
                initializer { getKoin().get<SignUpViewmodel>() }
                initializer { getKoin().get<ForgotPasswordViewmodel>() }
                initializer { getKoin().get<ResetPasswordViewmodel>() }
                initializer { getKoin().get<OtpViewmodel>() }
            },
        )
    }

    fun rootViewmodel(): RootViewmodel = viewModelProvider[RootViewmodel::class]

    fun signInViewmodel(): SignInViewmodel = viewModelProvider[SignInViewmodel::class]

    fun signUpViewmodel(): SignUpViewmodel = viewModelProvider[SignUpViewmodel::class]

    fun forgotPasswordViewmodel(): ForgotPasswordViewmodel =
        viewModelProvider[ForgotPasswordViewmodel::class]

    fun otpViewmodel(): OtpViewmodel = viewModelProvider[OtpViewmodel::class]

    fun resetPasswordViewmodel(): ResetPasswordViewmodel = viewModelProvider[ResetPasswordViewmodel::class]

    fun clear() {
        viewModelStore.clear()
    }
}
