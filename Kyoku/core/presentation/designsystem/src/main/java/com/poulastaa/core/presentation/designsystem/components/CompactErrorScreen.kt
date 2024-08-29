package com.poulastaa.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SadIcon
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun CompactErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = MaterialTheme.dimens.medium3)
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = SadIcon,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1f)
                .padding(MaterialTheme.dimens.large1),
            tint = MaterialTheme.colorScheme.primary.copy(.5f)
        )

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.extraSmall,
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = stringResource(id = R.string.error_something_went_wrong),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(MaterialTheme.dimens.medium1),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}