package com.poulastaa.play.data

import android.content.Context
import com.poulastaa.core.domain.repository.player.PlayerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OnlineFirstPlayerRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PlayerRepository {
    override suspend fun loadBackgroundColor(url: String) {

    }
}