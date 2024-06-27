package com.poulastaa.auth.presentation.email.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun AuthContinueButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean,
    enable: Boolean = true,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    letterSpacing: TextUnit = 2.sp,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(.3f),
            disabledContentColor = MaterialTheme.colorScheme.background.copy(.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
        ),
        enabled = enable,
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(40.dp)
                    .padding(MaterialTheme.dimens.small1)
                    .alpha(if (isLoading) 1f else 0f),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = text,
                fontWeight = fontWeight,
                fontSize = fontSize,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                textAlign = TextAlign.Center,
                letterSpacing = letterSpacing
            )
        }
    }
}

@Preview
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.dimens.medium1)
        ) {
            AuthContinueButton(
                text = stringResource(id = R.string.continue_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.small1),
                isLoading = true,
                enable = false
            ) {

            }
        }
    }
}