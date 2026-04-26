package com.poulastaa.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle

@Composable
fun SignInScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    MaterialTheme.typography.displayMedium.copy(fontStyle = FontStyle.Italic)
                        .toSpanStyle()
                ) {
                    append("S")
                }
                withStyle(
                    MaterialTheme.typography.headlineMedium.copy(fontStyle = FontStyle.Italic)
                        .toSpanStyle()
                ) {
                    append("ign in.")
                }
            }
        )
    }
}