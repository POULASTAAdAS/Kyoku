package com.poulastaa.main.data.repository.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.poulastaa.main.domain.repository.work.SyncLibraryScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.toJavaDuration

internal class SyncLibraryWorker @Inject constructor(
    context: Context,
    private val scope: CoroutineScope,
) : SyncLibraryScheduler {
    private enum class WorkType {
        ALBUM_SYNC,
        PLAYLIST_SYNC,
        ARTIST_SYNC,
        FAVOURITE_SYNC
    }

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(interval: Duration) {
        scope.launch {
            val album = async { syncWorker<SyncAlbumWorker>(interval, WorkType.ALBUM_SYNC) }
            val playlist = async {
                syncWorker<SyncPlaylistWorker>(interval, WorkType.PLAYLIST_SYNC)
            }
            val artist = async { syncWorker<SyncArtistWorker>(interval, WorkType.ARTIST_SYNC) }
            val favourite = async {
                syncWorker<SyncFavouriteSongWorker>(interval, WorkType.FAVOURITE_SYNC)
            }

            listOf(
                album,
                playlist,
                artist,
                favourite
            ).awaitAll()
        }
    }

    override suspend fun cancelAllSyncs() {
        scope.launch {
            val remove = async {
                workManager.cancelAllWork().await()
            }

            val prune = async {
                workManager.pruneWork().await()
            }

            listOf(
                remove,
                prune
            ).awaitAll()
        }
    }

    private suspend inline fun <reified T : ListenableWorker> syncWorker(
        interval: Duration,
        workType: WorkType,
    ) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(workType.name)
                .get().any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.RUNNING ||
                            it.state == WorkInfo.State.SUCCEEDED
                }
        }

        if (isSyncScheduled) return

        val req = PeriodicWorkRequestBuilder<T>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            backoffDelay = 1,
            timeUnit = TimeUnit.MINUTES
        ).setInitialDelay(
            duration = 1,
            timeUnit = TimeUnit.MINUTES
        ).addTag(workType.name)
            .build()

        workManager.enqueue(req).await()
    }
}