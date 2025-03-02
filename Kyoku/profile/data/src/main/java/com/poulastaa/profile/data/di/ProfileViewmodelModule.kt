package com.poulastaa.profile.data.di

import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.domain.repository.LocalProfileDatasource
import com.poulastaa.profile.data.repository.OfflineFirstProfileRepository
import com.poulastaa.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object ProfileViewmodelModule {
    @Provides
    @ViewModelScoped
    fun provideProfileRepository(
        ds: DatastoreRepository,
        local: LocalProfileDatasource,
    ): ProfileRepository = OfflineFirstProfileRepository(ds, local)
}