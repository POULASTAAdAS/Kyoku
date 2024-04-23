package com.poulastaa.kyoku.presentation.screen.home_root

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.poulastaa.kyoku.utils.PaletteGenerator

@Stable
data class PlayerSong(
    val id: Long = -1,
    val url: String = "",
    val masterPlaylist: String = "",
    val title: String = "",
    val artist: List<String> = listOf(""),
    val album: String = "",
    val year: String = "",
    val totalTime: String = ""
)


@Composable
fun Player(
    playerSong: PlayerSong,
    isDarkThem: Boolean,
    isCookie: Boolean,
    header: String,
    context: Context
) {
    LaunchedEffect(key1 = playerSong) {
        try {
            val bitmap = PaletteGenerator.convertImageUrlToBitMap(
                isDarkThem = isDarkThem,
                url = playerSong.url,
                isCookie = isCookie,
                header = header,
                context = context
            )
        } catch (_: Exception) {
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color.Blue
        ),
        shapes = MaterialTheme.shapes,
        typography = MaterialTheme.typography
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
        ) {

        }
    }
}