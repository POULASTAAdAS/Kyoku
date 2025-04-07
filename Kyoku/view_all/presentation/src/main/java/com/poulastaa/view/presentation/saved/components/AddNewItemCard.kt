package com.poulastaa.view.presentation.saved.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.poulastaa.view.presentation.saved.ViewSavedUiAction
import com.poulastaa.view.presentation.saved.ViewSavedUiItemType

@Composable
internal fun AddNewItemCard(
    modifier: Modifier = Modifier,
    isEditEnabled: Boolean,
    type: ViewSavedUiItemType,
    onAction: (ViewSavedUiAction.OnAddNewItemClick) -> Unit,
) {
    AnimatedVisibility(isEditEnabled.not()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = modifier,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                border = BorderStroke(
                    width = 1.4.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = CircleShape,
                onClick = {
                    onAction(ViewSavedUiAction.OnAddNewItemClick)
                }
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (type) {
                            ViewSavedUiItemType.ARTIST -> stringResource(R.string.add_new_artist)
                            ViewSavedUiItemType.PLAYLIST -> stringResource(R.string.add_new_playlist)
                            ViewSavedUiItemType.ALBUM -> stringResource(R.string.add_new_album)
                            ViewSavedUiItemType.NONE -> throw IllegalArgumentException(
                                "Wrong ViewSavedUiItem Type"
                            )
                        },
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(MaterialTheme.dimens.small2 + MaterialTheme.dimens.small1)
                    )
                }
            }
        }
    }
}