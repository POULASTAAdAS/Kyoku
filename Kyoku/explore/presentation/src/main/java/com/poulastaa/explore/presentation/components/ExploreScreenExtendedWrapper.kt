package com.poulastaa.explore.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreenExtendedWrapper(
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
    content: @Composable (PaddingValues) -> Unit,
    navigateBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            if (loadingType is LoadingType.Content) ExploreTopBar(
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
    ) {
        when (loadingType) {
            LoadingType.Loading -> loadingContent(it)
            is LoadingType.Error -> errorContent(it, loadingType)
            LoadingType.Content ->  content(it)/* Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DummySearch(
                    modifier = Modifier
                        .fillMaxWidth(.7f)
                        .height(38.dp),
                    isSearchOpen = isSearchOpen,
                    onToggleSearch = onToggleSearch
                )

                Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            }
*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    widthDp = 1024,
    heightDp = 540
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 1280,
    heightDp = 740
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ExploreScreenExtendedWrapper(
                modifier = Modifier.fillMaxWidth(.7f),
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