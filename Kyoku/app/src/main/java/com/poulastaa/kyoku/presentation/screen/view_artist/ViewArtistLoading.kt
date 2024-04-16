package com.poulastaa.kyoku.presentation.screen.view_artist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.kyoku.ui.theme.TestThem

@Composable
fun ViewArtistLoading(
    paddingValues: PaddingValues
) {

}

@Preview
@Composable
private fun Preview() {
    TestThem {
        ViewArtistLoading(PaddingValues())
    }
}