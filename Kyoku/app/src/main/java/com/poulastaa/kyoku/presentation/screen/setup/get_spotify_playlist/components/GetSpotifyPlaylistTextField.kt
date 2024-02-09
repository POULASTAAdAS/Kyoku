package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R

@Composable
fun GetSpotifyPlaylistTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    isError: Boolean,
    supportingText: String
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier,
        isError = isError,
        supportingText = { Text(text = supportingText) },
        label = {
            Text(text = "Past Link")
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.link),
                contentDescription = null
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,

            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.inversePrimary,

            cursorColor = MaterialTheme.colorScheme.inversePrimary,

            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
        )
    )
}

@Preview
@Composable
private fun Preview() {
    GetSpotifyPlaylistTextField(
        text = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        isError = false,
        supportingText = ""
    )
}