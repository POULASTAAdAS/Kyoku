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
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Module
@InstallIn(ViewModelComponent::class)
object PlayerModule {

    @Provides
    @ViewModelScoped
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
    @ViewModelScoped
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
    @ViewModelScoped
    @OptIn(UnstableApi::class)
    fun provideExoPlayer(
        hlsMediaSourceFactory: HlsMediaSource.Factory,
        @ApplicationContext context: Context
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(hlsMediaSourceFactory)
        .build()
}