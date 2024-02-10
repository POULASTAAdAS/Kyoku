package com.poulastaa.kyoku.presentation.screen.auth.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R

@Composable
fun CustomOkButton(
    text: String,
    modifier: Modifier,
    loading: Boolean,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = shape
            )
            .height(52.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun CustomAuthButton(
    modifier: Modifier,
    @DrawableRes
    icon: Int,
    text: String,
    loading: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(52.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            if (loading)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            else
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(32.dp)
                            ),
                        contentScale = ContentScale.Inside,
                        painter = painterResource(id = icon),
                        contentDescription = null,
                    )

                    Text(
                        modifier = Modifier
                            .padding(end = 32.dp)
                            .fillMaxWidth(),
                        text = text,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
        }
    }
}


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: Int,
    defaultText: String = "S E N D",
    isEnabled: Boolean,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    color: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        enabled = isEnabled,
        colors = color
    ) {
        if (isEnabled) Text(
            text = defaultText,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            textAlign = TextAlign.Center
        )
        else Text(
            text = "$text",
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Column {
        CustomOkButton(text = "Continue", modifier = Modifier.fillMaxWidth(), loading = false) {

        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomAuthButton(
            modifier = Modifier.fillMaxWidth(),
            icon = R.drawable.google,
            text = "Continue With Google",
            loading = false
        ) {

        }

        Spacer(modifier = Modifier.height(20.dp))

        CustomButton(text = 20, isEnabled = true) {

        }
    }
}