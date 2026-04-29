package com.poulastaa.common.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColumnSpacer(height: Dp) = Spacer(Modifier.fillMaxWidth().height(height))

@Composable
fun ColumnSpacer(height: Int) = Spacer(Modifier.fillMaxWidth().height(height.dp))

@Composable
fun RowSpacer(width: Dp) = Spacer(Modifier.width(width))

@Composable
fun RowSpacer(width: Int) = Spacer(Modifier.width(width.dp))