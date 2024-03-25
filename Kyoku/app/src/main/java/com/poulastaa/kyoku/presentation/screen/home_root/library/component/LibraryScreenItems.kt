package com.poulastaa.kyoku.presentation.screen.home_root.library.component

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenCardPlaylistPrev
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.HomeScreenImage
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun LibraryScreenListItem(
    modifier: Modifier = Modifier,
    isCookie: Boolean,
    authHeader: String,
    name: String,
    imageUrls: List<String>,
    onClick: () -> Unit
) {
    HomeScreenCardPlaylistPrev(
        modifier = modifier,
        name = name,
        imageUrls = imageUrls,
        isCookie = isCookie,
        headerValue = authHeader,
        color = MaterialTheme.colorScheme.background,
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick
    )
}

@Composable
fun LibraryScreenGridItem(
    modifier: Modifier = Modifier,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    isCookie: Boolean,
    authHeader: String,
    name: String,
    imageUrls: List<String>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(120.dp)
    ) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.5f)
                ) {
                    HomeScreenImage(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[0],
                        contentScale = ContentScale.FillBounds
                    )

                    HomeScreenImage(
                        modifier = Modifier.fillMaxSize(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[1],
                        contentScale = ContentScale.FillBounds
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HomeScreenImage(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .fillMaxHeight(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[0],
                        contentScale = ContentScale.FillBounds
                    )

                    HomeScreenImage(
                        modifier = Modifier.fillMaxSize(),
                        isDarkThem = isDarkThem,
                        isCookie = isCookie,
                        headerValue = authHeader,
                        url = imageUrls[1],
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

        Row {
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    TestThem {
        LibraryScreenGridItem(
            Modifier.size(130.dp),
            false,
            false,
            "",
            "Name",
            listOf("", "", "", "")
        ) {

        }
    }
}