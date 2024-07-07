package com.poulastaa.play.presentation.root_drawer.library.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SearchIcon
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopAppbar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
        title = {
            Text(
                text = stringResource(id = R.string.library_title),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2)
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}