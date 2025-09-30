package com.poulastaa.auth.presentation.intro.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.ui.GoogleIcon
import com.poulastaa.core.presentation.ui.R

@Composable
internal fun ContinueWithGoogleCard(
    isGoogleAuthLoading: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 56.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        onClick = onClick
    ) {
        AnimatedContent(
            isGoogleAuthLoading
        ) {
            when (it) {
                true -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.background,
                    )
                }

                false -> Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.tertiary)
                    ) {
                        Image(
                            painter = GoogleIcon,
                            contentDescription = stringResource(R.string.continue_with_google),
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.continue_with_google),
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.background
                    )

                    Spacer(Modifier.weight(1f))

                    Spacer(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .alpha(0f)
                    )
                }
            }
        }
    }
}