package com.poulastaa.settings.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.ShowMoreIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun ThemPicker(modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 0.dp
        ),
        onClick = {
            // todo change theme
        }
    ) {
       Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(
                       vertical = MaterialTheme.dimens.small3,
                       horizontal = MaterialTheme.dimens.medium1
                   ),
               verticalAlignment = Alignment.CenterVertically,
           ) {
               Text(
                   text = stringResource(R.string.color_type_1),
                   color = MaterialTheme.colorScheme.primary,
                   fontWeight = FontWeight.Bold,
                   fontSize = MaterialTheme.typography.headlineSmall.fontSize
               )

               Spacer(Modifier.weight(1f))

               Icon(
                   imageVector = ShowMoreIcon,
                   contentDescription = null,
                   tint = MaterialTheme.colorScheme.primary,
                   modifier = Modifier.size(30.dp)
               )
           }
       }
    }
}