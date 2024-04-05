package com.poulastaa.kyoku.presentation.screen.home_root.home.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.home.BottomSheetData
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType
import com.poulastaa.kyoku.data.model.screens.home.HomeUiEvent
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBottomSheet(
    sheetState: SheetState,
    isBottomSheetLoading: Boolean,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isCookie: Boolean,
    headerValue: String,
    data: BottomSheetData,
    onClick: (HomeUiEvent) -> Unit,
    cancelClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = cancelClick,
        sheetState = sheetState,
        properties = ModalBottomSheetDefaults.properties(
            isFocusable = false
        ),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        if (isBottomSheetLoading)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        else
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
                ) {
                    ItemImage(
                        isCookie = isCookie,
                        headerValue = headerValue,
                        isDarkThem = isDarkThem,
                        urls = data.urls
                    )

                    Text(
                        text = data.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }


                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

                when (data.type) {
                    HomeLongClickType.ALBUM_PREV -> {
                        ClickableItemWithDrawableImage(
                            text = "Play album",
                            icon = R.drawable.ic_play,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Add to Playlist",
                            icon = R.drawable.ic_add_to_playlist,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Add to Library Albums",
                            icon = R.drawable.ic_add_to_library,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Download Album",
                            icon = R.drawable.ic_download,
                            onClick = {

                            }
                        )
                    }

                    HomeLongClickType.ARTIST_MIX -> {
                        ClickableItemWithDrawableImage(
                            text = "Play Artist Mix",
                            icon = R.drawable.ic_play,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Add to Playlist",
                            icon = R.drawable.ic_add_to_playlist,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Download Artist Mix",
                            icon = R.drawable.ic_download,
                            onClick = {

                            }
                        )
                    }

                    HomeLongClickType.DAILY_MIX -> {
                        ClickableItemWithDrawableImage(
                            text = "Play Daily Mix",
                            icon = R.drawable.ic_play,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Add to Playlist",
                            icon = R.drawable.ic_add_to_playlist,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Download Daily Mix",
                            icon = R.drawable.ic_download,
                            onClick = {

                            }
                        )
                    }

                    HomeLongClickType.HISTORY_SONG -> {
                        ClickableItemWithDrawableImage(
                            text = "Play Song",
                            icon = R.drawable.ic_play,
                            onClick = {

                            }
                        )

                        ClickableItemWithVectorImage(
                            text = "Add to Favourite",
                            icon = Icons.Rounded.Favorite,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Add to Playlist",
                            icon = R.drawable.ic_add_to_playlist,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "View Artist",
                            icon = R.drawable.ic_filter_artist,
                            onClick = {

                            }
                        )

                        ClickableItemWithVectorImage(
                            text = "Remove From Listen History",
                            icon = Icons.Rounded.Clear,
                            onClick = {

                            }
                        )
                    }

                    HomeLongClickType.ARTIST_SONG -> {
                        ClickableItemWithDrawableImage(
                            text = "Play Song",
                            icon = R.drawable.ic_play,
                            onClick = {

                            }
                        )

                        ClickableItemWithVectorImage(
                            text = "Add to Favourite",
                            icon = Icons.Rounded.Favorite,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Add to Playlist",
                            icon = R.drawable.ic_add_to_playlist,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "View Artist",
                            icon = R.drawable.ic_filter_artist,
                            onClick = {

                            }
                        )

                        ClickableItemWithDrawableImage(
                            text = "Hide this song",
                            icon = R.drawable.ic_remove,
                            onClick = {

                            }
                        )
                    }
                }
            }
    }
}


@Composable
private fun ItemImage(
    isCookie: Boolean,
    headerValue: String,
    isDarkThem: Boolean,
    urls: List<String>
) {
    if (urls.size < 4) {
        CustomImageView(
            modifier = Modifier.size(60.dp),
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = headerValue,
            url = urls[0]
        )
    } else {
        Column(
            modifier = Modifier
                .size(60.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f / 2)
            ) {
                CustomImageView(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1f / 2),
                    isDarkThem = isDarkThem,
                    url = urls[0],
                    isCookie = isCookie,
                    headerValue = headerValue
                )

                CustomImageView(
                    modifier = Modifier
                        .fillMaxSize(),
                    isDarkThem = isDarkThem,
                    url = urls[1],
                    isCookie = isCookie,
                    headerValue = headerValue
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CustomImageView(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1f / 2),
                    isDarkThem = isDarkThem,
                    url = urls[2],
                    isCookie = isCookie,
                    headerValue = headerValue
                )
                CustomImageView(
                    modifier = Modifier
                        .fillMaxSize(),
                    isDarkThem = isDarkThem,
                    url = urls[3],
                    isCookie = isCookie,
                    headerValue = headerValue
                )
            }
        }
    }
}

@Composable
private fun ClickableItemWithDrawableImage(
    text: String,
    @DrawableRes
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = text,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                letterSpacing = 1.sp
            )
        }
    }
}


@Composable
private fun ClickableItemWithVectorImage(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small3),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = text,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                letterSpacing = 1.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    TestThem {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                }
            ) {
                Text(text = "show")
            }

            if (sheetState.isVisible) {
                HomeScreenBottomSheet(
                    sheetState = sheetState,
                    isBottomSheetLoading = false,
                    data = BottomSheetData(
                        name = "Tum Hi Ho",
                        urls = listOf("", "", "", ""),
                        type = HomeLongClickType.ARTIST_SONG
                    ),
                    isDarkThem = isSystemInDarkTheme(),
                    isCookie = false,
                    headerValue = "",
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }
                    }
                ) {
                    scope.launch {
                        sheetState.hide()
                    }
                }
            }
        }
    }
}