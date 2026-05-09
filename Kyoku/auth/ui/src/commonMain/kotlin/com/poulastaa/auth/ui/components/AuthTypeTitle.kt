package com.poulastaa.auth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.design_system.dimens


@Composable
fun AuthTypeTitle(titleStart: String, titleEnd: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                MaterialTheme.typography.displayLarge.copy(fontStyle = FontStyle.Italic)
                    .toSpanStyle()
            ) {
                append(titleStart)
            }
            withStyle(
                MaterialTheme.typography.displaySmall.copy(fontStyle = FontStyle.Italic)
                    .toSpanStyle()
            ) {
                append(titleEnd)
            }
        },
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.primary
    )

    ColumnSpacer(MaterialTheme.dimens.spacing.hairline)

    Spacer(
        Modifier.width(MaterialTheme.typography.displayLarge.fontSize.value.dp)
            .clip(CircleShape)
            .height(MaterialTheme.dimens.spacing.hairline)
            .background(MaterialTheme.colorScheme.primary)
    )
}