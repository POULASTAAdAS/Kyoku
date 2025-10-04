package com.poulastaa.auth.presentation.otp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
internal fun Info(
    heading: String,
    content: String,
    isClickable: Boolean = false,
    onclick: () -> Unit = {},
) {
    Text(
        text = content,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    )
    Text(
        text = heading,
        color = MaterialTheme.colorScheme.secondary,
        textDecoration = TextDecoration.Underline,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .offset(y = -MaterialTheme.typography.bodySmall.fontSize.value.dp / 2)
            .clickable(
                enabled = isClickable,
                onClick = onclick
            ),
    )
}
