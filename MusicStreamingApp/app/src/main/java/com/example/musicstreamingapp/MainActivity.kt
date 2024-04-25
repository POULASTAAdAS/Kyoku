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
import androidx.media3.datasource.HttpDataSource
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

                val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource
                    .Factory().apply {
                        this.setDefaultRequestProperties(
                            mapOf(
                                "Cookie" to "na i am idiot"
                            )
                        )
                    }


                // http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?master=Confusion_Pagglait/Confusion_Pagglait_master.m3u8
                // http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Bekhayali_Kabir_Singh/Bekhayali_Kabir_Singh_master.m3u8

                val hslMediaSource = HlsMediaSource
                    .Factory(dataSourceFactory)
                    .setExtractorFactory( // fuck this line of code
                        DefaultHlsExtractorFactory(
                            DefaultTsPayloadReaderFactory.FLAG_IGNORE_H264_STREAM,
                            true
                        )
                    )
                    .setAllowChunklessPreparation(true)

                val item = MediaItem.Builder()
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .setMimeType(MimeTypes.APPLICATION_ID3)
                    .setUri("http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?master=Bekhayali_Kabir_Singh/Bekhayali_Kabir_Singh_master.m3u8")
                    .setLiveConfiguration(
                        MediaItem
                            .LiveConfiguration
                            .Builder()
                            .setMaxPlaybackSpeed(1.02f)
                            .build()
                    )
                    .build()

                val player = ExoPlayer.Builder(context)
                    .setMediaSourceFactory(hslMediaSource)
                    .build()


                player.addMediaItem(item)

                player.prepare()
                player.play()

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