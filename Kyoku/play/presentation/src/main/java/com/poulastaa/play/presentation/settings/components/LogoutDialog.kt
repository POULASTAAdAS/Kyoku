package com.poulastaa.play.presentation.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun LogoutDialog(
    modifier: Modifier = Modifier,
    isLoginOut: Boolean,
    onConform: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
    ) {
        Card(
            modifier = modifier
        ) {
            if (isLoginOut) Column(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1)
            ) {
                CircularProgressIndicator()
            } else Column(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1)
            ) {
                Text(
                    text = stringResource(id = R.string.logout_text),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = MaterialTheme.colorScheme.primary.copy(.8f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                Row {
                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(.8f)
                        )
                    ) {
                        Text(text = stringResource(id = R.string.no).uppercase())
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onConform() },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(.8f)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.yes).uppercase(),
                            color = MaterialTheme.colorScheme.primary.copy(.8f)
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            LogoutDialog(
                onConform = {},
                isLoginOut = true
            ) {

            }
        }
    }
}