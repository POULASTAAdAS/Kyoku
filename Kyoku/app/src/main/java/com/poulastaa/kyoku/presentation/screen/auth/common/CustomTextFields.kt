package com.poulastaa.kyoku.presentation.screen.auth.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun CustomTextFiled(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    label: String = "Email",
    modifier: Modifier,
    isError: Boolean,
    supportingText: String,
    shape: CornerBasedShape = RoundedCornerShape(16.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,

        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        focusedBorderColor = MaterialTheme.colorScheme.inversePrimary,

        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.inversePrimary,

        cursorColor = MaterialTheme.colorScheme.inversePrimary,

        unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
        focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
    ),
    trailingIcon: @Composable (() -> Unit) = {
        Icon(
            imageVector = Icons.Rounded.Email,
            contentDescription = null,
        )
    },
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Done,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = { onDone.invoke() },
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        isError = isError,
        shape = shape,
        colors = colors,
        supportingText = {
            Text(text = supportingText)
        },
        trailingIcon = trailingIcon,
        label = {
            Text(text = label)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    label: String = "Password",
    modifier: Modifier,
    isError: Boolean,
    supportingText: String,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    colors: TextFieldColors =
        OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,

            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.inversePrimary,

            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.inversePrimary,

            cursorColor = MaterialTheme.colorScheme.inversePrimary,

            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
        ),
    trailingIcon: @Composable (() -> Unit),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = { onDone.invoke() },
    ),
    visualTransformation: VisualTransformation,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        isError = isError,
        shape = shape,
        colors = colors,
        supportingText = {
            Text(text = supportingText)
        },
        trailingIcon = trailingIcon,
        label = {
            Text(text = label)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation
    )
}


@Preview
@Composable
private fun Preview() {
    Column {
        CustomTextFiled(
            value = "",
            onValueChange = {},
            onDone = { },
            modifier = Modifier.fillMaxWidth(),
            isError = false,
            supportingText = "",
        )

        CustomPasswordField(
            value = "",
            onValueChange = {},
            onDone = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
            isError = false,
            supportingText = "",
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = if (true) painterResource(id = R.drawable.ic_visibility_off) else
                            painterResource(id = R.drawable.ic_visibility_on),
                        contentDescription = null
                    )
                }
            },
            visualTransformation = VisualTransformation.None
        )
    }
}
