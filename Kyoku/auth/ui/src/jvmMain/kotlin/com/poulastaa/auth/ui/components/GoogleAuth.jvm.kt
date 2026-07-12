package com.poulastaa.auth.ui.components

import org.koin.core.module.Module
import org.koin.dsl.module

actual val googleAuthModule: Module = module {
    single<GoogleAuthWrapper> { JvmGoogleAuthWrapper() }
}

private class JvmGoogleAuthWrapper : GoogleAuthWrapper {
    override var onResult: ((GoogleAuthResult) -> Unit)? = null

    override fun startGoogleAuth() {
        onResult?.invoke(GoogleAuthResult.Canceled)
    }
}
