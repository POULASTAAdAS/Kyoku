package com.poulastaa.common.ui.di

import com.poulastaa.common.ui.root.RootViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::RootViewmodel)
}
