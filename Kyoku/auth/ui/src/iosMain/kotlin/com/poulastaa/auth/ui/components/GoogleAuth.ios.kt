package com.poulastaa.auth.ui.components

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

// needed to satisfy Koin dependency injection
actual val googleAuthModule: Module = module {}

/**
 * call from [MainViewController]
 */
fun registerGoogleAuthWrapper(interop: GoogleAuthWrapper) {
    KoinPlatform.getKoin().declare<GoogleAuthWrapper>(interop)
}