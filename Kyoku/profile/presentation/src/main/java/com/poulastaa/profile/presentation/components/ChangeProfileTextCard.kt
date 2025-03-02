package com.poulastaa.profile.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.profile.presentation.ProfileUiAction

@Composable
internal fun ChangeProfileTextCard(onAction: (ProfileUiAction.OnEditProfileImage) -> Unit) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        border = BorderStroke(
            width = 2.5.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            onAction(ProfileUiAction.OnEditProfileImage)
        }
    ) {
        Text(
            text = "change profile image",
            modifier = Modifier
                .padding(MaterialTheme.dimens.medium1)
                .padding(horizontal = MaterialTheme.dimens.small2),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}