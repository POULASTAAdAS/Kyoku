package com.poulastaa.explore.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreenWrapper(
    modifier: Modifier = Modifier,
    scroll: TopAppBarScrollBehavior,
    loadingType: LoadingType,
    title: String,
    isSearchOpen: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onToggleSearch: () -> Unit,
    loadingContent: @Composable (PaddingValues) -> Unit,
    errorContent: @Composable (PaddingValues, LoadingType.Error) -> Unit,
    content: LazyListScope.() -> Unit,
    navigateBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (loadingType is LoadingType.Content) AppSearchBar(
                modifier = modifier,
                scroll = scroll,
                title = title,
                isSearchOpen = isSearchOpen,
                query = query,
                onQueryChange = onQueryChange,
                navigateBack = navigateBack,
                onSearch = {
                    focusManager.clearFocus()
                }
            )
        }
    ) { paddingValues ->
        when (loadingType) {
            LoadingType.Loading -> loadingContent(paddingValues)
            is LoadingType.Error -> errorContent(paddingValues, loadingType)
            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(paddingValues)
                    .nestedScroll(scroll.nestedScrollConnection),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                item {
                    DummySearch(
                        Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        isSearchOpen,
                        onToggleSearch = onToggleSearch
                    )
                }

                item {
                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }

                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ExploreScreenWrapper(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                loadingType = LoadingType.Content,
                isSearchOpen = isSystemInDarkTheme(),
                title = stringResource(R.string.that_cool_artist),
                query = "",
                onQueryChange = {},
                onToggleSearch = {},
                loadingContent = { },
                errorContent = { _, _ -> },
                content = { },
                navigateBack = { },
            )
        }
    }
}