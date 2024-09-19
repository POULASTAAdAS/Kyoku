package com.poulastaa.play.data.di

import android.content.Context
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.DefaultHlsExtractorFactory
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import androidx.media3.session.MediaSession
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.player.KyokuPlayer
import com.poulastaa.play.data.player.PlayerDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {
    @Provides
    @Singleton
    fun provideDatasource(
        ds: DataStoreRepository,
    ): DataSource.Factory = DefaultHttpDataSource.Factory().apply {
        CoroutineScope(Dispatchers.IO).launch {
            ds.readTokenOrCookie().collectLatest { token ->
                val type = if (token.startsWith("B")) "Authorization" else "Cookie"

                setDefaultRequestProperties(
                    mapOf(type to token)
                )
            }
        }
    }

    @Provides
    @Singleton
    fun provideHLSMediaSourceFactory(datasource: DataSource.Factory): HlsMediaSource.Factory =
        HlsMediaSource
            .Factory(datasource)
            .setExtractorFactory(
                DefaultHlsExtractorFactory(
                    DefaultTsPayloadReaderFactory.FLAG_IGNORE_H264_STREAM,
                    true
                )
            )
            .setAllowChunklessPreparation(true)

    @Provides
    @Singleton
    fun provideExoPlayer(
        factory: HlsMediaSource.Factory,
        @ApplicationContext context: Context,
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(factory)
        .build()

    @Provides
    @Singleton
    fun provideMediaSession(
        player: ExoPlayer,
        @ApplicationContext context: Context,
    ): MediaSession = MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun providePlayerDatasource(player: ExoPlayer): KyokuPlayer = PlayerDatasource(player = player)
}