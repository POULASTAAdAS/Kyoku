package com.poulastaa.auth.ui.components

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

actual val googleAuthModule: Module = module {
    single<GoogleAuthWrapper> { IosGoogleAuthWrapper() }
}

fun registerGoogleAuthWrapper(interop: GoogleAuthWrapper) {
    KoinPlatform.getKoin().declare<GoogleAuthWrapper>(interop)
}

private class IosGoogleAuthWrapper : GoogleAuthWrapper {
    override var onResult: ((GoogleAuthResult) -> Unit)? = null

    override fun startGoogleAuth() {
        onResult?.invoke(GoogleAuthResult.Canceled)
    }
}
