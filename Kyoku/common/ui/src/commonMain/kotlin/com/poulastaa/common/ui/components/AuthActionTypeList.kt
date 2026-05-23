package com.poulastaa.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.design_system.IconGoogle
import com.poulastaa.common.ui.design_system.IconShowMore
import com.poulastaa.common.ui.design_system.StringContinueWithGoogle
import com.poulastaa.common.ui.design_system.dimens

@Composable
fun AuthActionTypeList(
    buttonText: String,
    subTitle: String,
    onEmailAuthClick: () -> Unit,
    onGoogleAuthClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.small)
    ) {
        ElevatedGradientButton(
            width = 0.7f,
            onClick = onEmailAuthClick,
            colors = CardDefaults.elevatedCardColors(
                contentColor = MaterialTheme.colorScheme.background,
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small,
                    ).minimumInteractiveComponentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = buttonText, fontWeight = FontWeight.SemiBold)

                RowSpacer(MaterialTheme.dimens.spacing.small)

                Icon(imageVector = IconShowMore, contentDescription = buttonText)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                Modifier.height(1.5.dp).weight(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Text(
                text = subTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                Modifier.height(1.5.dp).weight(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        ElevatedGradientButton(
            width = 1f,
            onClick = onGoogleAuthClick,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small,
                    )
                    .minimumInteractiveComponentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .padding(MaterialTheme.dimens.spacing.hairline)
                        .background(
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = IconGoogle,
                        contentDescription = StringContinueWithGoogle,
                        modifier = Modifier.minimumInteractiveComponentSize(),
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = StringContinueWithGoogle,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.background,
                )

                Spacer(Modifier.weight(1f))

                // place holder
                Box(
                    Modifier.background(
                        Color.Transparent,
                        shape = MaterialTheme.shapes.small
                    ).alpha(0f)
                ) {
                    Icon(imageVector = IconGoogle, contentDescription = null)
                }
            }
        }
    }
}