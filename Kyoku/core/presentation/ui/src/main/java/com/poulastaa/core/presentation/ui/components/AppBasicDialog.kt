package com.poulastaa.core.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBasicDialog(
    title: String,
    flipColor: Boolean = false,
    conformText: String? = null,
    cancelText: String? = null,
    conformIcon: ImageVector? = null,
    cancelIcon: ImageVector? = null,
    onConformClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(MaterialTheme.dimens.large1))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (flipColor) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.errorContainer,
                            contentColor = if (flipColor) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.error
                        )
                    ) {
                        cancelIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null
                            )
                        } ?: Text(text = cancelText ?: stringResource(R.string.no))
                    }

                    Button(
                        onClick = onConformClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (flipColor) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primary,
                            contentColor = if (flipColor) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.background
                        )
                    ) {
                        conformIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null
                            )
                        } ?: Text(text = conformText ?: stringResource(R.string.yes))
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
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
                    )
            ) {
                AppBasicDialog(
                    title = stringResource(R.string.cancel_saving_album),
                    flipColor = true,
                    onConformClick = {},
                    onDismissRequest = {}
                )
            }
        }
    }
}