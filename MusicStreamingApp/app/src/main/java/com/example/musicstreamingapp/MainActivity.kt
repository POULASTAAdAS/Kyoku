package com.example.musicstreamingapp

import android.content.res.Configuration
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.DefaultHlsExtractorFactory
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.extractor.ts.DefaultTsPayloadReaderFactory
import androidx.media3.ui.PlayerView
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
                                "Cookie" to "GOOGLE_USER_SESSION=2b5649a053eb80c7134fd724c5ab1895%" +
                                        "2F759c9eb3eafa73931e55bfc63041b235521378c1edfaf84a84f8e61" +
                                        "f26da6d832a34a079c723be69ac4179eeac186742%3A85fff52fda20d" +
                                        "39a1ca42c2ae1cd12b49ee86d15669f410c57b1d37e765158f5"
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(player)
                }
            }
        }
    }
}


@OptIn(UnstableApi::class)
@Composable
fun Content(
    player: ExoPlayer
) {
    LaunchedEffect(key1 = player) {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                Log.d("playing", isPlaying.toString())
            }


        })
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                trackColor = MaterialTheme.colorScheme.primary,
                progress = .3f
            )

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CustomIconButton(icon = Icons.Rounded.KeyboardArrowLeft) {
                    player.seekToPrevious()
                }

                IconButton(
                    onClick = {
                        if (player.isPlaying) player.pause() else player.play()
                    },
                    modifier = Modifier.size(80.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(80.dp),
                        imageVector = if (player.isPlaying) Icons.Rounded.Clear else Icons.Rounded.PlayArrow,
                        contentDescription = null
                    )
                }

                CustomIconButton(icon = Icons.Rounded.KeyboardArrowRight) {
                    player.seekToNext()
                }
            }
        }
    }
}

@Composable
fun CustomIconButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(
            modifier = Modifier.size(80.dp),
            imageVector = icon,
            contentDescription = null
        )
    }
}

enum class ClickType {
    PLAY,
    PAUSE
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    MusicStreamingAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                trackColor = MaterialTheme.colorScheme.primary,
                progress = .3f
            )

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CustomIconButton(icon = Icons.Rounded.KeyboardArrowLeft) {
                }

                IconButton(
                    onClick = {
                    },
                    modifier = Modifier.size(80.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(80.dp),
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null
                    )
                }

                CustomIconButton(icon = Icons.Rounded.KeyboardArrowRight) {
                }
            }
        }
    }
}