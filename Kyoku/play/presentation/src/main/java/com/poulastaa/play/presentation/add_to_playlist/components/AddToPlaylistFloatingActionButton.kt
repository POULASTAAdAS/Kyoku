package com.poulastaa.play.presentation.add_to_playlist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R

@Composable
fun AddToPlaylistFloatingActionButton(
    isMakingApiCall: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isMakingApiCall,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(.2f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.save),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(if (isMakingApiCall) 0f else 1f),
            )

            CircularProgressIndicator(
                strokeWidth = 1.5.dp,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(if (isMakingApiCall) 1f else 0f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}