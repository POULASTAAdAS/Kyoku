package com.poulastaa.play.data.work

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
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
    private val applicationScope: CoroutineScope,
) : SyncLibraryScheduler {
    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(interval: Duration) {
        applicationScope.launch {
            val album = async { updateAlbumWorker(interval) }
            val playlist = async { updatePlaylistWorker(interval) }
            val artist = async { updateArtistWorker(interval) }
            val favourite = async { updateFavouriteWorker(interval) }

            album.await()
            playlist.await()
            artist.await()
            favourite.await()

            async { updatePlaylistSongWorker(interval) }.await()
        }
    }

    private suspend fun updateAlbumWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.ALBUM_SYNC.name)
                .get()
                .any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.RUNNING ||
                            it.state == WorkInfo.State.SUCCEEDED
                }
        }


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
            timeUnit = TimeUnit.SECONDS
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
                .any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.RUNNING ||
                            it.state == WorkInfo.State.SUCCEEDED
                }
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
            timeUnit = TimeUnit.SECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        ).addTag(WorkType.PLAYLIST_SYNC.name)
            .build()


        applicationScope.launch {
            workManager.enqueue(workReq).await()
        }.join()
    }

    private suspend fun updatePlaylistSongWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.PLAYLIST_SONG_SYNC.name)
                .get()
                .any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.RUNNING ||
                            it.state == WorkInfo.State.SUCCEEDED
                }
        }

        if (isSyncScheduled) return

        val workReq = PeriodicWorkRequestBuilder<UpdatePlaylistSongWorker>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.SECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        ).addTag(WorkType.PLAYLIST_SYNC.name)
            .build()

        applicationScope.launch {
            workManager.enqueue(workReq).await()
        }.join()
    }

    private suspend fun updateArtistWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.ARTIST_SYNC.name)
                .get()
                .any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.RUNNING ||
                            it.state == WorkInfo.State.SUCCEEDED
                }
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
            timeUnit = TimeUnit.SECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        ).addTag(WorkType.ARTIST_SYNC.name)
            .build()


        applicationScope.launch {
            workManager.enqueue(workReq).await()
        }.join()
    }

    private suspend fun updateFavouriteWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager.getWorkInfosByTag(WorkType.ARTIST_SYNC.name)
                .get()
                .any {
                    it.state == WorkInfo.State.ENQUEUED ||
                            it.state == WorkInfo.State.RUNNING ||
                            it.state == WorkInfo.State.SUCCEEDED
                }
        }

        if (isSyncScheduled) return

        val workReq = PeriodicWorkRequestBuilder<UpdateFavouriteWorker>(
            repeatInterval = interval.toJavaDuration()
        ).setConstraints(
            constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.SECONDS
        ).setInitialDelay(
            duration = 30,
            timeUnit = TimeUnit.MINUTES
        ).addTag(WorkType.ARTIST_SYNC.name)
            .build()


        applicationScope.launch {
            workManager.enqueue(workReq).await()
        }.join()
    }

    override suspend fun cancelAllSyncs() {
        applicationScope.launch {
            val remove = async {
                WorkManager.getInstance(context)
                    .cancelAllWork()
                    .await()
            }

            val prune = async {
                WorkManager.getInstance(context)
                    .pruneWork()
                    .await()
            }

            remove.await()
            prune.await()
        }
    }

    private enum class WorkType {
        ALBUM_SYNC,
        PLAYLIST_SYNC,
        PLAYLIST_SONG_SYNC,
        ARTIST_SYNC,
    }
}