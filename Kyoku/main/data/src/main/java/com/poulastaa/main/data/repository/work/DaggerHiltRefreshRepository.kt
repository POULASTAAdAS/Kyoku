package com.poulastaa.main.data.repository.work

import com.poulastaa.main.domain.repository.work.RefreshRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

internal class DaggerHiltRefreshRepository @Inject constructor(
    private val scope: CoroutineScope,
) : RefreshRepository {

}