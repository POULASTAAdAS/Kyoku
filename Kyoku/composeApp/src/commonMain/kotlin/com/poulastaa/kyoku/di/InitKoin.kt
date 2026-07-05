package com.poulastaa.kyoku.di

import org.koin.core.KoinApplication
import org.koin.core.lazyModules
import org.koin.core.context.startKoin as startKoinContext

fun initKoin(config: (KoinApplication.() -> Unit)? = null) {
    SharedApplication.startKoin(config)
}

private fun SharedApplication.startKoin(config: (KoinApplication.() -> Unit)?) {
    startKoinContext {
        config?.invoke(this)
        modules(dependencyModule)
        lazyModules(viewModelModule)
    }
}
