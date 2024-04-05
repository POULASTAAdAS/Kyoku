package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBottomSheet(
    sheetState: SheetState,
    isBottomSheetLoading: Boolean,
    onClick: (HomeUiEvent) -> Unit,
    cancelClick: () -> Unit
) {

}

