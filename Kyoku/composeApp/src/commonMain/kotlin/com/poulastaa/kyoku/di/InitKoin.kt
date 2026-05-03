package com.poulastaa.kyoku.di

import com.poulastaa.common.ui.di.appModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}
