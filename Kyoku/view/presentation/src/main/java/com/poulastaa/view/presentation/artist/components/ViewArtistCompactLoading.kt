package com.poulastaa.view.presentation.artist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistCompactLoading(modifier: Modifier) {
    var cardHeight by remember { mutableStateOf(0.dp) }
    var controllerHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.45f)
                    .onSizeChanged {
                        with(density) {
                            cardHeight = it.height.toDp()
                        }
                    },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect(MaterialTheme.colorScheme.primary)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = cardHeight)
                    .padding(top = (controllerHeight / 2).dp)
                    .padding(top = MaterialTheme.dimens.small3)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MaterialTheme.dimens.medium1)
            ) {
                repeat(10) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(78.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.aspectRatio(1f),
                            shape = MaterialTheme.shapes.extraSmall,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect(MaterialTheme.colorScheme.primary)
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(.6f)
                                    .height(24.dp),
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .shimmerEffect(MaterialTheme.colorScheme.primary)
                                )
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(.4f)
                                    .height(16.dp),
                                shape = MaterialTheme.shapes.extraSmall,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 1.dp
                                )
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .shimmerEffect(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }

                        Spacer(Modifier.weight(1f))

                        Icon(
                            imageVector = ThreeDotIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(.5f)
                        )
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = cardHeight)
                    .onSizeChanged {
                        controllerHeight = (it.height / 2)
                    }
                    .offset {
                        IntOffset(y = -controllerHeight, x = 0)
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.primaryContainer,
                                Color.Transparent,
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1)
                ) {
                    Column {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(.4f)
                                .height(35.dp),
                            shape = MaterialTheme.shapes.extraSmall,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )
                        }

                        Spacer(Modifier.height(MaterialTheme.dimens.small1))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(.2f)
                                .height(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = MaterialTheme.shapes.extraSmall,
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(.6f)
                                .height(46.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Card(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.small2))

                        Card(
                            modifier = Modifier.size(65.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            )
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmerEffect()
                            )
                        }
                    }
                }
            }

            TopAppBar(
                title = {},
                navigationIcon = {
                    Card(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(.3f)
                            .fillMaxHeight(.7f),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium1)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistCompactLoading(
                Modifier
                    .background(
                        brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
            )
        }
    }
}