package com.poulastaa.kyoku.presentation.screen.song_view.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens
import com.poulastaa.kyoku.utils.shimmerEffect

@Composable
fun SongViewContentLoading(
    isFavourite: Boolean = false,
    isSmallPhone: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.dimens.medium1)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .clip(CircleShape)
                    .shimmerEffect()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        }

        Box(
            modifier = Modifier
                .size(if (isSmallPhone) 200.dp else 240.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect(),
            contentAlignment = Alignment.Center
        ) {
            if (isFavourite)
                Icon(
                    imageVector = Icons.Rounded.Favorite, contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.large1),
                    tint = Color(0xFF222831)
                )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(150.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(80.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFF393E46)
                    ),
                    modifier = Modifier
                        .border(
                            width = 3.dp,
                            color = Color(0xFF393E46),
                            shape = CircleShape
                        )
                        .size(35.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = null
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shuffle),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp),
                    tint = Color(0xFF393E46)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF393E46)
                )
            }
        }

        repeat(6) {
            LoadingSongCard()
        }
    }
}

@Composable
private fun LoadingSongCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        SongCardDragButton(
            modifier = Modifier.fillMaxWidth(.04f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(.2f)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmerEffect()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(20.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(16.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect()
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(.5f),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.TwoTone.AddCircle,
                    contentDescription = null,
                    tint = Color(0xFF393E46),
                    modifier = Modifier.size(34.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null,
                    tint = Color(0xFF393E46),
                    modifier = Modifier.size(34.dp)
                )
            }
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        SongViewContentLoading(
            isSmallPhone = false
        )
    }
}