package com.poulastaa.profile.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.profile.presentation.ProfileUiAction

@Composable
internal fun GoToLibrary(
    modifier: Modifier,
    onAction: (ProfileUiAction.OnNavigateToGallery) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp
        ),
        onClick = {
            onAction(ProfileUiAction.OnNavigateToGallery)
        }
    ) {
        Text(
            text = stringResource(R.string.go_to_library),
            modifier = Modifier
                .padding(MaterialTheme.dimens.medium1)
                .padding(horizontal = MaterialTheme.dimens.medium1)
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold
        )
    }
}