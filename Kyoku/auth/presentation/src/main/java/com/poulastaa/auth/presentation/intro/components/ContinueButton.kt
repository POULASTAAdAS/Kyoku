package com.poulastaa.auth.presentation.intro.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.ui.R


@Composable
internal fun ContinueButton(
    modifier: Modifier,
    isEmailAuthLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .heightIn(max = 30.dp)
                    .aspectRatio(1f)
                    .alpha(if (isEmailAuthLoading) 1f else 0f)
            )

            Text(
                text = stringResource(R.string.continue_text),
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier
                    .alpha(if (isEmailAuthLoading) 0f else 1f)
            )
        }
    }
}