package com.poulastaa.play.presentation.add_as_playlist

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    sheetState: SheetState,
    exploreType: ExploreType,
    closeBottomSheet: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = closeBottomSheet,
        sheetState = sheetState,
        shape = MaterialTheme.shapes.medium
    ) {
        AddAsPlaylistRootScreen(
            modifier = Modifier
                .imePadding()
                .padding(bottom = MaterialTheme.dimens.medium1)
                .padding(horizontal = MaterialTheme.dimens.medium1),
            exploreType = exploreType,
            navigateBack = closeBottomSheet
        )
    }
}

