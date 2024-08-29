package com.poulastaa.core.presentation.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SadIcon
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun ExpandedErrorScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = MaterialTheme.dimens.medium3)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.4f)
        ) {
            Icon(
                imageVector = SadIcon,
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.primary.copy(.5f)
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.large2))

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

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 560
)
@Preview(
    widthDp = 840,
    heightDp = 560
)
@Composable
private fun Preview() {
    AppThem {
        ExpandedErrorScreen()
    }
}