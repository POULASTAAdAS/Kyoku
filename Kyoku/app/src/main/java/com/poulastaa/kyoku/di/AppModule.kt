package com.poulastaa.kyoku.di

import android.app.Application
import com.poulastaa.core.presentation.ThemeManager
import com.poulastaa.kyoku.Kyoku
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCoroutineScope(application: Application) = (application as Kyoku).applicationScope()

    @Provides
    @Singleton
    fun provideThemeManager(): ThemeManager {
        val themeManager = ThemeManager()
        ThemeManager.setInstance(themeManager)
        return themeManager
    }
}