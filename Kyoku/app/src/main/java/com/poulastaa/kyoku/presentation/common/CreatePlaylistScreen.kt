package com.poulastaa.kyoku.presentation.common

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens


@Composable
fun CreatePlaylistScreen(
    text: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
    onDoneClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.primary,
                    )
                )
            )
            .padding(MaterialTheme.dimens.medium1),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onCancelClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp)
                )
            }
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Playlist Name",
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            textAlign = TextAlign.Center,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

        TextField(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .focusRequester(focusRequester),
            value = text,
            onValueChange = onValueChange,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusRequester.freeFocus()
                    onDoneClick.invoke()
                }
            )
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomButton(text = "C A N C E L") {
                focusRequester.freeFocus()
                onCancelClick.invoke()
            }

            CustomButton(text = "S A V E") {
                focusRequester.freeFocus()
                onDoneClick.invoke()
            }
        }
    }
}

@Composable
private fun CustomButton(
    text: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick
    ) {
        Text(text = text)
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    TestThem {
        val state = remember {
            mutableStateOf(true)
        }

        val text = remember {
            mutableStateOf("Daily Mix [10/4/24]")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    state.value = true
                }
            ) {
                Text(text = "show")
            }
        }

        AnimatedVisibility(
            visible = state.value,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 400)
            ) + slideInVertically(
                animationSpec = tween(durationMillis = 400),
                initialOffsetY = { it / 2 }
            ),
            exit = fadeOut(
                animationSpec = tween(400)
            ) + slideOutVertically(
                animationSpec = tween(durationMillis = 400),
                targetOffsetY = { it / 2 }
            )
        ) {
            CreatePlaylistScreen(
                onDoneClick = {
                    state.value = false
                },
                onCancelClick = {
                    state.value = false
                },
                text = text.value,
                onValueChange = {
                    text.value = it
                }
            )
        }
    }
}