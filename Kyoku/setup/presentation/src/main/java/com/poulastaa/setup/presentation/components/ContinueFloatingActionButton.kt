package com.poulastaa.setup.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ContinueFloatingActionButton(
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.padding(
            end = MaterialTheme.dimens.medium1,
            bottom = MaterialTheme.dimens.medium3
        ),
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.alpha(if (isLoading) 1f else 0f),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(id = R.string.continue_text),
                letterSpacing = 2.sp,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
            )
        }
    }
}