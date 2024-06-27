package com.poulastaa.setup.presentation.get_spotify_playlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    isMakingApiCall: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.add),
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.small3)
                    .alpha(if (isMakingApiCall) 0f else 1f),
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .alpha(if (isMakingApiCall) 1f else 0f),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round
            )
        }
    }
}