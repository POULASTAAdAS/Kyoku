package com.poulastaa.kyoku.presentation.screen.home_root.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.kyoku.data.model.screens.home.HomeUiData
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun HomeScreenContentOldUser(
    paddingValues: PaddingValues,
    isSmallPhone: Boolean,
    data: HomeUiData,
    isCookie: Boolean,
    headerValue: String,
    isInternetError: Boolean,
    errorMessage: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            start = MaterialTheme.dimens.medium1,
            end = MaterialTheme.dimens.medium1
        )
    ) {
        item {
            AnimatedVisibility(
                visible = isInternetError,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut()
            ) {
                val temp = remember {
                    isInternetError
                }

                if (temp) {
                    CustomToast(
                        message = errorMessage,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

//        item {
//            Row {
//                Column {
//                    HomeScreenCardWithText(
//                        modifier = Modifier
//                            .fillMaxWidth(.5f)
//                            .height(100.dp),
//                        name = data.playlist[0],
//                        imageUrl =,
//                        authHeader =,
//                        isCookie =
//                    ) {
//
//                    }
//                }
//                Column {
//
//                }
//            }
//        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    TestThem {
        HomeScreenContentOldUser(
            paddingValues = PaddingValues(),
            isSmallPhone = false,
            data = HomeUiData(),
            isCookie = false,
            headerValue = "",
            isInternetError = false,
            errorMessage = ""
        )
    }
}