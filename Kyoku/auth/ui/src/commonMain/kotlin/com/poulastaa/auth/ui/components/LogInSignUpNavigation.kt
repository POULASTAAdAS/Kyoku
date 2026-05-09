package com.poulastaa.auth.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle

@Composable
fun LogInSignUpNavigation(
    title: String,
    navigationType: String,
    onClick: () -> Unit,
) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        val annotatedString = buildAnnotatedString {
            withStyle(
                MaterialTheme.typography.bodyMedium.toSpanStyle()
                    .copy(color = MaterialTheme.colorScheme.onSurface)
            ) {
                append(title)
            }

            withLink(
                LinkAnnotation.Clickable(
                    tag = navigationType,
                    styles = TextLinkStyles(
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        ).toSpanStyle()
                    ),
                    linkInteractionListener = {
                        onClick()
                    }
                )
            ) {
                append(navigationType)
            }
        }

        Text(text = annotatedString)
    }
}