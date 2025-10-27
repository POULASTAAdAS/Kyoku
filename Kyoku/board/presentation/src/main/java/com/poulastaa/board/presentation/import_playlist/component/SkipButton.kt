package com.poulastaa.board.presentation.import_playlist.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.R

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.background,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        )
    ) {
        Text(
            text = stringResource(R.string.skip),
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        SkipButton(modifier = Modifier.fillMaxWidth(.5f)) { }
    }
}