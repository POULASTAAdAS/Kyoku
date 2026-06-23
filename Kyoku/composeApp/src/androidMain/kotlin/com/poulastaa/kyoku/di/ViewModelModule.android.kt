package com.poulastaa.kyoku.di

import com.poulastaa.auth.ui.sign_in.SingInViewmodel
import com.poulastaa.auth.ui.sign_up.SignUpViewmodel
import com.poulastaa.common.ui.root.RootViewmodel
import org.koin.core.module.LazyModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.lazyModule

actual val viewModelModule: LazyModule = lazyModule {
    viewModelOf(::RootViewmodel)
    viewModelOf(::SingInViewmodel)
    viewModelOf(::SignUpViewmodel)
}
