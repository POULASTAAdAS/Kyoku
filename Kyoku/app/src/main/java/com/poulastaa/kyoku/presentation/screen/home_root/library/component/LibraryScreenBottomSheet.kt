package com.poulastaa.kyoku.presentation.screen.home_root.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiEvent
import com.poulastaa.kyoku.data.model.screens.library.PinnedData
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType
import com.poulastaa.kyoku.ui.theme.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreenBottomSheet(
    sheetState: SheetState,
    pinnedData: PinnedData,
    onClick: (LibraryUiEvent.BottomSheetItemClick) -> Unit,
    cancelClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = cancelClick,
        sheetState = sheetState,
        properties = ModalBottomSheetDefaults.properties(
            isFocusable = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.medium1),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pinnedData.name.uppercase(),
                textAlign = TextAlign.Start,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            OutlinedButton(
                onClick = {
                    onClick.invoke(
                        LibraryUiEvent.BottomSheetItemClick.AddClick(
                            type = pinnedData.type,
                            name = pinnedData.name
                        )
                    )
                },
                enabled = !pinnedData.isPinned
            ) {
                Icon(
                    modifier = Modifier
                        .rotate(45f),
                    painter = painterResource(id = R.drawable.pin),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                Text(text = "Add to pinned")
            }

            OutlinedButton(
                onClick = {
                    onClick.invoke(LibraryUiEvent.BottomSheetItemClick.RemoveClick)
                },
                enabled = pinnedData.isPinned
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                Text(text = "Remove from pinned")
            }

            OutlinedButton(
                onClick = {
                    onClick.invoke(LibraryUiEvent.BottomSheetItemClick.DeleteClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))

                if (pinnedData.type == PinnedDataType.FAVOURITE) Text(text = "Delete [${pinnedData.name.uppercase()}]")
                else Text(text = "Delete ${pinnedData.type.title}  [${pinnedData.name.uppercase()}]")
            }
        }
    }
}
