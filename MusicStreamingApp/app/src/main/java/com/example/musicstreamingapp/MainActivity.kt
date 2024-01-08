package com.example.musicstreamingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.DefaultHlsExtractorFactory
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import com.example.musicstreamingapp.ui.theme.MusicStreamingAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MusicStreamingAppTheme {
                val context = LocalContext.current

                val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

                val hslMediaSource = HlsMediaSource
                    .Factory(dataSourceFactory)
                    .setExtractorFactory( // fuck this line of code
                        DefaultHlsExtractorFactory(
                            DefaultTsPayloadReaderFactory.FLAG_IGNORE_H264_STREAM,
                            true
                        )
                    )

                    .setAllowChunklessPreparation(true)
                    .createMediaSource(
                        MediaItem.Builder()
                            .setMimeType(MimeTypes.APPLICATION_M3U8)
                            .setMimeType(MimeTypes.APPLICATION_ID3)
                            .setUri("https://b857-103-44-172-243.ngrok-free.app/master.m3u8")
                            .setLiveConfiguration(
                                MediaItem
                                    .LiveConfiguration
                                    .Builder()
                                    .setMaxPlaybackSpeed(1.02f)
                                    .build()
                            )
                            .build()
                    )

                val player = ExoPlayer.Builder(context)
                    .setMediaSourceFactory(
                        DefaultMediaSourceFactory(context)
                            .setLiveTargetOffsetMs(5000)
                    )
                    .build()
                    .apply {
                        setMediaSource(hslMediaSource)
                        prepare()
                        play()
                    }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Button(onClick = {
                        if (player.isPlaying) player.pause()
                        else {
                            player.play()
                            Log.d("clicked", "play clicked")
                        }

                    }) {
                        Text(text = "button")
                    }
                }
            }
        }
    }
}