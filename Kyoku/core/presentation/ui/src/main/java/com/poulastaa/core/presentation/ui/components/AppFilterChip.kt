package com.poulastaa.core.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
fun AppFilterChip(
    title: String,
    isSelected: Boolean,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    ElevatedFilterChip(
        modifier = Modifier.animateContentSize(tween(400)),
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = title,
                modifier = Modifier.padding(2.dp)
            )
        },
        leadingIcon = if (isSelected && icon != null) {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        } else null,
        elevation = FilterChipDefaults.elevatedFilterChipElevation(
            elevation = if (isSelected) 6.dp else 0.dp,
            pressedElevation = 0.dp
        ),
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.primaryContainer
        ),
        colors = FilterChipDefaults.elevatedFilterChipColors(
            labelColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.primary,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
            disabledLeadingIconColor = MaterialTheme.colorScheme.background
        ),
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    var toggle by remember { mutableStateOf(false) }

    AppThem {
        Surface {
            Box(Modifier.padding(MaterialTheme.dimens.medium1)) {
                AppFilterChip(
                    title = "Test",
                    isSelected = true,
                    icon = ImageVector.vectorResource(R.drawable.ic_user),
                    onClick = {
                        toggle = !toggle
                    }
                )
            }
        }
    }
}