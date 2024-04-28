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
import androidx.compose.runtime.LaunchedEffect
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
    private val items = uriList.map {
        MediaItem.Builder()
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .setMimeType(MimeTypes.APPLICATION_ID3)
            .setUri(it)
            .setLiveConfiguration(
                MediaItem
                    .LiveConfiguration
                    .Builder()
                    .setMaxPlaybackSpeed(1.02f)
                    .build()
            )
            .build()
    }.toMutableList()

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
                                "Cookie" to "GOOGLE_USER_SESSION=c57c5485d1bef39f2e6e20ae5a114" +
                                        "5f5%2Fe66b5c75995d8915d5fc82233544b58b9a077865b282a09" +
                                        "37c789a751ad8c5d30de088c288fb6db27bf599733545086a%3A8" +
                                        "5fff52fda20d39a1ca42c2ae1cd12b49ee86d15669f410c57b1d3" +
                                        "7e765158f5"
                            )
                        )
                    }


                val hslMediaSource = HlsMediaSource
                    .Factory(dataSourceFactory)
                    .setExtractorFactory( // fuck this line of code
                        DefaultHlsExtractorFactory(
                            DefaultTsPayloadReaderFactory.FLAG_IGNORE_H264_STREAM,
                            true
                        )
                    )
                    .setAllowChunklessPreparation(true)

                val player = ExoPlayer.Builder(context)
                    .setMediaSourceFactory(hslMediaSource)
                    .build()


                player.addMediaItems(items)

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

val uriList = listOf(
    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Badmaashi_Simmie/Badmaashi_Simmie_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Badrang_Mein_Satranga_Hain_Ye_Ishq_Re/Badrang_Mein_Satranga_Hain_Ye_Ishq_Re_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Chute_Ya_Na_Chute/Chute_Ya_Na_Chute_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Dil_Mera_Dil/Dil_Mera_Dil_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Dil_Tere_Pyaar_Mein_Jogi_Ho_Gaya/Dil_Tere_Pyaar_Mein_Jogi_Ho_Gaya_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Gam_Khushiya/Gam_Khushiya_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Hai_Chand_Par_Daag_Paar_Tujhme_Ek_Bhi/Hai_Chand_Par_Daag_Paar_Tujhme_Ek_Bhi_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Hua_Main/Hua_Main_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Ijazat_(Slowed_Reverb)/Ijazat_(Slowed_Reverb)_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Izhar_Hua_Hume_Bhi_Pyaar_Hua/Izhar_Hua_Hume_Bhi_Pyaar_Hua_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?playlist=Kaise_Hua/Kaise_Hua_master.m3u8",
//    "http://kyoku.poulastaa.online:8081/api/authorised/playSong/master?master=Confusion_Pagglait/Confusion_Pagglait_master.m3u8"
)

