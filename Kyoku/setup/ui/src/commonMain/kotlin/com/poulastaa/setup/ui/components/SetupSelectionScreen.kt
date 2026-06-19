package com.poulastaa.setup.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.poulastaa.common.ui.components.AppOutlinedTextField
import com.poulastaa.common.ui.components.ColumnSpacer
import com.poulastaa.common.ui.components.ElevatedDefaultButton
import com.poulastaa.common.ui.components.SmallClearButton
import com.poulastaa.common.ui.components.top_bar.DefaultTopBar
import com.poulastaa.common.ui.design_system.dimens

@Composable
fun SetupSelectionScreen(
    title: String,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    searchLabel: String,
    searchLeadingIcon: ImageVector? = null,
    isMinimumSelectionMet: Boolean,
    minimumSelectionText: String,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    confirmButtonText: String = "Continue",
    columns: GridCells = GridCells.Fixed(3),
    lazyGridContent: LazyGridScope.() -> Unit,
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
                onBackClick = onBackClick,
                titleContent = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        },
        modifier = modifier
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = it.calculateTopPadding())
            ) {
                AppOutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.layout.contentPadding),
                    label = searchLabel,
                    leadingIcon = searchLeadingIcon,
                    trailingContent = {
                        SmallClearButton(
                            visible = searchText.isNotEmpty(),
                            oClick = { onSearchTextChange("") }
                        )
                    }
                )

                AnimatedVisibility(visible = isMinimumSelectionMet.not()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        ColumnSpacer(MaterialTheme.dimens.spacing.large)

                        Text(
                            text = minimumSelectionText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    LazyVerticalGrid(
                        columns = columns,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.extraSmall),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.extraSmall),
                        overscrollEffect = null
                    ) {
                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            Spacer(
                                Modifier.fillMaxWidth().height(MaterialTheme.dimens.spacing.large)
                            )
                        }

                        lazyGridContent()

                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            Spacer(
                                Modifier.fillMaxWidth().navigationBarsPadding()
                            )
                        }

                        item(
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            Spacer(
                                Modifier.fillMaxWidth()
                                    .height(MaterialTheme.dimens.spacing.extraLarge)
                            )
                        }
                    }

                    Spacer(
                        Modifier.fillMaxWidth()
                            .height(MaterialTheme.dimens.spacing.large)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        MaterialTheme.colorScheme.background.copy(0.8f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = MaterialTheme.dimens.spacing.small),
                visible = isMinimumSelectionMet,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom) + slideInVertically { it },
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom) + slideOutVertically { it }
            ) {
                ElevatedDefaultButton(
                    width = 0.7f,
                    onClick = onConfirmClick,
                    colors = CardDefaults.elevatedCardColors(
                        contentColor = MaterialTheme.colorScheme.background,
                    ),
                ) {
                    Box(
                        Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(MaterialTheme.dimens.spacing.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmButtonText,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
