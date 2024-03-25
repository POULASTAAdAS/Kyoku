package com.poulastaa.kyoku.presentation.screen.home_root.library

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomToast
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenImage
import com.poulastaa.kyoku.presentation.screen.home_root.library.component.LibraryScreenListItem
import com.poulastaa.kyoku.ui.theme.background
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    isSmallPhone: Boolean,
    isCookie: Boolean,
    headerValue: String,
    context: Context,
    paddingValues: PaddingValues,
) {
    LaunchedEffect(key1 = viewModel.state.isInternetAvailable) {
        viewModel.loadData(context)
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {

                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    if (!viewModel.state.data.all.isFavourite &&
        viewModel.state.data.all.playlist.isEmpty() &&
        viewModel.state.data.all.artist.isEmpty()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
            contentPadding = PaddingValues(
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f / 3)
                    ) {
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

                        // todo sort types :: playlist , artist , album
                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                    }
                }
            }

            // Toast
            item {
                AnimatedVisibility(
                    visible = viewModel.state.isInternetError,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1000)) + expandIn(),
                    exit = fadeOut(animationSpec = tween(durationMillis = 1000)) + shrinkOut()
                ) {
                    val temp = remember {
                        viewModel.state.isInternetError
                    }

                    if (temp) {
                        CustomToast(
                            message = viewModel.state.errorMessage,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Pinned",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Black,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }

            item {
                if (
                    !viewModel.state.data.pinned.isFavourite ||
                    viewModel.state.data.pinned.playlist.isEmpty() ||
                    viewModel.state.data.pinned.artist.isEmpty()
                ) {
                    Text(
                        text = "Long press to pin",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Light,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text( // todo place holder
                        text = "Data",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

            }

            if (viewModel.state.data.all.isFavourite) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

                    // todo favourite preview
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))
                }
            }

            if (viewModel.state.data.all.playlist.isNotEmpty()) {
                item {
                    Text(
                        text = "Playlist",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                }


                items(viewModel.state.data.all.playlist.size) {
                    LibraryScreenListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        isCookie = isCookie,
                        authHeader = headerValue,
                        name = viewModel.state.data.all.playlist[it].name,
                        imageUrls = viewModel.state.data.all.playlist[it].listOfUrl,
                        onClick = {

                        }
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                }
            }

            if (viewModel.state.data.all.artist.isNotEmpty()) {
                item {
                    Text(
                        text = "Artist",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                }

                items(viewModel.state.data.all.artist.size) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .size(120.dp),
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = background
                            )
                        ) {
                            HomeScreenImage(
                                isDarkThem = isSystemInDarkTheme(),
                                url = viewModel.state.data.all.artist[it].imageUrl,
                                isCookie = isCookie,
                                headerValue = headerValue,
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

                        Text(
                            text = viewModel.state.data.all.artist[it].name,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
                }
            }
        }
    }
}