package com.poulastaa.play.data.work

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.poulastaa.play.domain.SyncLibraryScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncLibraryWorkerScheduler @Inject constructor(
    private val context: Context,
    private val applicationScope: CoroutineScope
) : SyncLibraryScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(interval: Duration) {
        applicationScope.launch {
            val album = async { updateAlbumWorker(interval) }
            val playlist = async { updatePlaylistWorker(interval) }
            val artist = async { updateArtistWorker(interval) }

            album.await()
            playlist.await()
            artist.await()
        }
    }

    private suspend fun updateAlbumWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.ALBUM_SYNC.name)
                .get()
                .isNotEmpty()
        }

        Log.d("called", "isSyncScheduled : $isSyncScheduled") // todo delete

        if (isSyncScheduled) return

        val workReq = PeriodicWorkRequestBuilder<UpdateAlbumsWorker>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.MILLISECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        ).addTag(WorkType.ALBUM_SYNC.name)
            .build()

        workManager.enqueue(workReq).await()
    }

    private suspend fun updatePlaylistWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.PLAYLIST_SYNC.name)
                .get()
                .isNotEmpty()
        }

        if (isSyncScheduled) return

        val workReq = PeriodicWorkRequestBuilder<UpdatePlaylistWorker>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.MILLISECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        )
            .addTag(WorkType.PLAYLIST_SYNC.name)
            .build()


        applicationScope.launch {
            workManager.enqueue(workReq).await()
        }.join()
    }

    private suspend fun updateArtistWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.ARTIST_SYNC.name)
                .get()
                .isNotEmpty()
        }

        if (isSyncScheduled) return

        val workReq = PeriodicWorkRequestBuilder<UpdateArtistWorker>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.MILLISECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        )
            .addTag(WorkType.ARTIST_SYNC.name)
            .build()


        applicationScope.launch {
            workManager.enqueue(workReq).await()
        }.join()
    }

    override suspend fun cancelAllSyncs() {
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }

    private enum class WorkType {
        ALBUM_SYNC,
        PLAYLIST_SYNC,
        ARTIST_SYNC,
    }
}