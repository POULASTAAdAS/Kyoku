package com.poulastaa.auth.ui.components

import com.poulastaa.auth.domain.PasswordCredentialStore
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

// needed to satisfy Koin dependency injection
actual val googleAuthModule: Module = module {
    single<PasswordCredentialStore> { IosPasswordCredentialStore }
}

/**
 * call from [MainViewController]
 */
fun registerGoogleAuthWrapper(interop: GoogleAuthWrapper) {
    KoinPlatform.getKoin().declare<GoogleAuthWrapper>(interop)
}