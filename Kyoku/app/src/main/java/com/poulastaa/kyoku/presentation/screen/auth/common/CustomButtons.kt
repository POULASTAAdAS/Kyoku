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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun CustomOkButton(
    text: String,
    modifier: Modifier,
    loading: Boolean,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
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
            .height(53.dp),
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
                    .padding(52.dp),
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
fun CustomButton(
    modifier: Modifier = Modifier,
    text: Int,
    defaultText: String = "S E N D",
    isEnabled: Boolean,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
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


@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = MaterialTheme.dimens.large2),
        enabled = !isLoading,
        shape = MaterialTheme.shapes.large
    ) {
        if (isLoading)
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary
            )
        else
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(MaterialTheme.dimens.large2) // 52.dp
                        .background(
                            color = Color.White,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .clip(MaterialTheme.shapes.extraLarge)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge
                        ),
                    contentScale = ContentScale.Inside
                )

                Text(
                    text = text,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = MaterialTheme.dimens.large1), // 32.dp
                    textAlign = TextAlign.Center
                )
            }
    }
}


@Preview
@Composable
private fun Preview() {
    Column {
        CustomOkButton(text = "Continue", modifier = Modifier.fillMaxWidth(), loading = false) {

        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))

        CustomButton(text = 20, isEnabled = true) {

        }
    }
}