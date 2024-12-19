package com.poulastaa.auth.presentation.intro.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.EmailIcon
import com.poulastaa.core.presentation.designsystem.GoogleIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun AppAuthButton(
    modifier: Modifier = Modifier,
    text: String,
    image: Painter? = null,
    isLoading: Boolean = false,
    icon: ImageVector = EmailIcon,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 0.dp
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (image != null) Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                ) else Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }

            AnimatedContent(
                targetState = isLoading,
                label = "google auth loading button"
            ) {
                when (it) {
                    true -> CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.background,
                        strokeWidth = 3.dp
                    )

                    false -> Text(
                        text = text,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.aspectRatio(1f))
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            Column {
                AppAuthButton(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.large1)
                        .fillMaxWidth()
                        .height(45.dp),
                    image = GoogleIcon,
                    text = stringResource(R.string.email_sign_in),
                    onClick = {}
                )

                AppAuthButton(
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.large1)
                        .fillMaxWidth()
                        .height(45.dp),
                    text = stringResource(R.string.email_sign_in),
                    onClick = {}
                )
            }
        }
    }
}