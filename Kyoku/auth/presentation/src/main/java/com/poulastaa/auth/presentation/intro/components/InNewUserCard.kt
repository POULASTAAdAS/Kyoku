package com.poulastaa.auth.presentation.intro.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poulastaa.auth.presentation.intro.IntroUiAction
import com.poulastaa.core.presentation.ui.R
import com.poulastaa.core.presentation.ui.dimens

@Composable
internal fun InNewUserCard(
    haptic: HapticFeedback,
    onAction: (IntroUiAction.OnEmailSingUpClick) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.email_not_registered),
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = MaterialTheme.shapes.extraSmall,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    onAction(IntroUiAction.OnEmailSingUpClick)
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.small1)
                        .fillMaxWidth(.6f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.create_new_account),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}