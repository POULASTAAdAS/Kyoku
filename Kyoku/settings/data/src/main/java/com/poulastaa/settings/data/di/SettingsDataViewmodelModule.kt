package com.poulastaa.settings.data.di

import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalSettingsDatasource
import com.poulastaa.settings.data.repository.OfflineFirstSettingsRepository
import com.poulastaa.settings.domain.repository.RemoteSettingDatasource
import com.poulastaa.settings.domain.repository.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(ViewModelComponent::class)
internal object SettingsDataViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(
        ds: DatastoreRepository,
        remote: RemoteSettingDatasource,
        local: LocalSettingsDatasource,
        scope: CoroutineScope,
    ): SettingRepository = OfflineFirstSettingsRepository(ds, remote, local, scope)
}