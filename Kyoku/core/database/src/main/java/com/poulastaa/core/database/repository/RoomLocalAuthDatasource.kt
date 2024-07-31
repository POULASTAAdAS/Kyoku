package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.domain.auth.LocalAuthDatasource
import javax.inject.Inject

class RoomLocalAuthDatasource @Inject constructor(
    private val homeDao: HomeDao,
) : LocalAuthDatasource