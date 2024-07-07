package com.poulastaa.play.presentation.root_drawer.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SearchIcon
import com.poulastaa.core.presentation.designsystem.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppbar(
    title: String,
    profileUrl: String,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    TopAppBar(
        modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
        title = {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2)
            )
        },
        navigationIcon = {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(profileUrl)
                    .placeholder(drawableResId = R.drawable.ic_user)
                    .crossfade(true)
                    .error(drawableResId = R.drawable.ic_user)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small1)
                    .size(50.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                    )
                    .clickable(
                        onClick = {
                            onProfileClick()
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    )
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

@Preview
@Composable
private fun Preview() {
    AppThem {
        HomeAppbar(title = "Good Morning", profileUrl = "", onProfileClick = { /*TODO*/ }) {

        }
    }
}