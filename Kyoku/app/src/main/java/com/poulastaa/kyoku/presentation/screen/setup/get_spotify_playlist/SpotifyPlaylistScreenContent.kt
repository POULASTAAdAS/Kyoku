package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomOkButton
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomTextFiled

@Composable
fun SpotifyPlaylistScreenContent(
    paddingValues: PaddingValues,
    link: String,
    onValueChange: (String) -> Unit,
    supportingText: String,
    isError: Boolean,
    isLoading: Boolean,
    isFirstPlaylist: Boolean,
    onAddClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 15.dp,
                end = 15.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TopPart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            link = link,
            onValueChange = onValueChange,
            supportingText = supportingText,
            isError = isError,
            isFirstPlaylist = isFirstPlaylist,
            isLoading = isLoading,
            onAddClick = onAddClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .weight(7f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(
                items = returnFun(),
                key = {
                    it.id
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = it.image),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(text = it.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        EndPart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.8f),
            onClick = onSkipClick
        )
    }
}

@Composable
fun TopPart(
    modifier: Modifier,
    link: String,
    onValueChange: (String) -> Unit,
    supportingText: String,
    isError: Boolean,
    isFirstPlaylist: Boolean,
    isLoading: Boolean,
    onAddClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextFiled(
            value = link,
            onValueChange = onValueChange,
            onDone = {},
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            supportingText = supportingText,
            label = "Past Link",
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.link),
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onAddClick.invoke() }
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            CustomOkButton(
                text = if (isFirstPlaylist) "Add" else "Add another",
                modifier = Modifier.width(100.dp),
                loading = isLoading,
                shape = RoundedCornerShape(8.dp),
                onClick = onAddClick
            )
        }
    }
}

@Composable
fun MidPart(
    modifier: Modifier, // todo send data
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .border(
                width = 1.5.dp,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = content
    )
}

@Composable
fun EndPart(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "skip",
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {


    SpotifyPlaylistScreenContent(
        paddingValues = PaddingValues(15.dp),
        link = "",
        onValueChange = {},
        supportingText = "",
        isError = false,
        isLoading = false,
        isFirstPlaylist = true,
        onAddClick = { /*TODO*/ }) {

    }
}


class Item(
    val id: Int,
    val name: String,
    @DrawableRes val image: Int
)


fun returnFun(): List<Item> {
    val list = ArrayList<Item>()

    for (i in 1..30) {
        list.add(Item(id = i, name = "Item", image = R.drawable.google))
    }

    return list
}
