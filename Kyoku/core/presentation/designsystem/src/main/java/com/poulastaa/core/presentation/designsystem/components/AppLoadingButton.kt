package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun AppLoadingButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    fontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    fontWeight: FontWeight = FontWeight.Medium,
    loadingColor: Color = MaterialTheme.colorScheme.primary,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ),
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = loadingColor,
                modifier = Modifier.alpha(if (isLoading) 1f else 0f)
            )

            Text(
                text = text,
                fontWeight = fontWeight,
                fontSize = fontSize,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            AppLoadingButton(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                text = stringResource(R.string.continue_text),
                isLoading = false,
                onClick = {}
            )
        }
    }
}