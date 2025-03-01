package com.poulastaa.settings.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.model.UiUser

@Composable
internal fun ProfileImageCard(
    modifier: Modifier,
    state: UiUser,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        SubcomposeAsyncImage(
            model = state.profilePic,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            error = {
                ProfilePlaceHolder()
            },
            loading = {
                ProfilePlaceHolder()
            }
        )
    }
}