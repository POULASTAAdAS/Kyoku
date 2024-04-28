package com.poulastaa.kyoku.presentation.screen.view_artist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiArtist
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiEvent
import com.poulastaa.kyoku.presentation.common.ArtistView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun ViewArtistContent(
    paddingValues: PaddingValues,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isCookie: Boolean,
    headerValue: String,
    data: List<ViewArtistUiArtist>,
    onClick: (ViewArtistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceBright,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceDim,
                        MaterialTheme.colorScheme.surfaceDim
                    )
                )
            )
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                onClick = navigateBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))


        data.forEach {
            ArtistView(
                data = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .clickable {
                        onClick.invoke(ViewArtistUiEvent.ArtistClick(it.name))
                    },
                imageSize = 120.dp,
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                header = headerValue,
                nameFontSize = MaterialTheme.typography.titleLarge.fontSize,
                listenedFontSize = MaterialTheme.typography.titleSmall.fontSize
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    TestThem {
        val data = ArrayList<ViewArtistUiArtist>()

        for (i in 1..10) {
            data.add(
                ViewArtistUiArtist(
                    artistId = i.toLong(),
                    name = "name $i",
                    listened = 10 * i.toLong(),
                    coverImage = ""
                )
            )
        }

        ViewArtistContent(paddingValues = PaddingValues(), data = data,
            isCookie = false,
            headerValue = "",
            onClick = {}) {

        }
    }
}