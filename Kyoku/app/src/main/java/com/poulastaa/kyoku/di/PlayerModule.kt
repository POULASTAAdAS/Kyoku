package com.poulastaa.kyoku.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.DefaultHlsExtractorFactory
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import androidx.media3.session.MediaSession
import com.poulastaa.kyoku.domain.player.notification.AudioNotificationManager
import com.poulastaa.kyoku.domain.player.service.AudioServiceHandler
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun provideDataSource(
        ds: DataStoreOperation
    ): DataSource.Factory = DefaultHttpDataSource.Factory()
        .apply {
            CoroutineScope(Dispatchers.IO).launch {
                ds.readTokenOrCookie().collect {
                    val type = if (it.startsWith("B")) "Authorization" else "Cookie"

                    setDefaultRequestProperties(
                        mapOf(type to it)
                    )
                }
            }
        }

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun provideHLSMediaSourceFactory(dataSourceFactory: DataSource.Factory): HlsMediaSource.Factory =
        HlsMediaSource
            .Factory(dataSourceFactory)
            .setExtractorFactory( // fuck this line of code
                DefaultHlsExtractorFactory(
                    DefaultTsPayloadReaderFactory.FLAG_IGNORE_H264_STREAM,
                    true
                )
            )
            .setAllowChunklessPreparation(true)

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun provideExoPlayer(
        hlsMediaSourceFactory: HlsMediaSource.Factory,
        @ApplicationContext context: Context
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(hlsMediaSourceFactory)
        .build()

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): AudioNotificationManager = AudioNotificationManager(context, player)

    @Provides
    @Singleton
    fun provideServiceHandler(player: ExoPlayer): AudioServiceHandler = AudioServiceHandler(player)
}