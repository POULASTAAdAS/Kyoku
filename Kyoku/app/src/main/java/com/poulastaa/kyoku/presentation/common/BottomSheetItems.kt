package com.poulastaa.kyoku.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun ClickableItemWithDrawableImage(
    text: String,
    @DrawableRes
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = text,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                letterSpacing = 1.sp
            )
        }
    }
}


@Composable
fun ClickableItemWithVectorImage(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = text,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                letterSpacing = 1.sp
            )
        }
    }
}