package com.example.musicstreamingapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.ParcelFileDescriptor
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
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import com.example.musicstreamingapp.temp.Api
import com.example.musicstreamingapp.ui.theme.MusicStreamingAppTheme
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.FileOutputStream
import kotlin.io.path.absolutePathString

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.105:8080/") // Update with your server's base URL
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(Api::class.java)


        setContent {
            MusicStreamingAppTheme {
                val context = LocalContext.current

                val player = SimpleExoPlayer.Builder(context).build()
//                    ExoPlayer.Builder(context)
//                        .setMediaSourceFactory(
//                            DefaultMediaSourceFactory(context)
//                                .setLiveTargetOffsetMs(5000)
//                        )
//                        .build()

                val call: Call<ResponseBody> = apiService.getSong()

                call.enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val responseBody: ResponseBody? = response.body()

                            responseBody?.let {
                                Log.d("called", "responsebody $it")

                                val inputStream = it.byteStream()

                                val tempFile = kotlin.io.path.createTempFile(suffix = ".mp3")
                                val outputStream = FileOutputStream(tempFile.toString())

                                inputStream.use { input ->
                                    outputStream.use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                MediaPlayer().apply {
                                    setDataSource(tempFile.absolutePathString())
                                    prepareAsync()

                                    setOnPreparedListener { player ->
                                        player.start()
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("called", "onFailure")
                        }
                    }
                )



                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Button(onClick = {
                        if (player.isPlaying) player.pause()
                        else {
                            player.play()
                            Log.d("clicked", "dwjaiojdi")
                        }
                    }) {
                        Text(text = "button")
                    }
                }
            }
        }
    }
}