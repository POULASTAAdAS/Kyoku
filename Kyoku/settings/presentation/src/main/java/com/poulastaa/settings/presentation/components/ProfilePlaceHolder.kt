package com.poulastaa.settings.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.poulastaa.core.presentation.designsystem.ui.UserIcon

@Composable
fun ProfilePlaceHolder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = UserIcon,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(.8f)
        )
    }
}