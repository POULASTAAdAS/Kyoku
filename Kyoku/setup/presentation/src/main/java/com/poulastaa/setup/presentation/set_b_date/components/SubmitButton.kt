package com.poulastaa.setup.presentation.set_b_date.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            disabledContentColor = MaterialTheme.colorScheme.surfaceDim.copy(.5f),
            disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.primary.copy(.9f)
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            disabledElevation = 8.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.continue_text),
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.small3)
                    .alpha(if (isLoading) 0f else 1f),
                textAlign = TextAlign.Center,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1)
        ) {
            SubmitButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            ) {

            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            SubmitButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            ) {

            }
        }
    }
}