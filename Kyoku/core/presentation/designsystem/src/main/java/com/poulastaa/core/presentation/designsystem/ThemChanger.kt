package com.poulastaa.core.presentation.designsystem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.model.ThemColor
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.ThemModeChanger.Companion.themMode
import com.poulastaa.core.presentation.designsystem.ui.blueBackgroundDark
import com.poulastaa.core.presentation.designsystem.ui.blueBackgroundLight
import com.poulastaa.core.presentation.designsystem.ui.blueSecondaryDark
import com.poulastaa.core.presentation.designsystem.ui.blueSecondaryLight
import com.poulastaa.core.presentation.designsystem.ui.greenBackgroundDark
import com.poulastaa.core.presentation.designsystem.ui.greenBackgroundLight
import com.poulastaa.core.presentation.designsystem.ui.greenSecondaryDark
import com.poulastaa.core.presentation.designsystem.ui.greenSecondaryLight
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemChanger @Inject constructor() {
    companion object Companion : ViewModel() {
        private var ds: DatastoreRepository? = null
        var themColor by mutableStateOf(ThemColor.GREEN)
            private set

        internal fun init(ds: DatastoreRepository) {
            this.ds = ds
            loadThem()
        }

        fun changeThem(them: ThemColor) {
            viewModelScope.launch {
                ds?.storeThemeColor(them)
            }
        }

        fun themBackground(them: ThemColor) = when (them) {
            ThemColor.GREEN -> if (themMode) listOf(
                greenBackgroundDark,
                greenSecondaryDark
            ) else listOf(
                greenBackgroundLight,
                greenSecondaryLight
            )

            ThemColor.BLUE -> if (themMode) listOf(
                blueBackgroundDark,
                blueSecondaryDark
            ) else listOf(
                blueBackgroundLight,
                blueSecondaryLight
            )
        }


        private fun loadThem() {
            viewModelScope.launch {
                ds?.readThemeColor()?.collectLatest {
                    themColor = it
                }
            }
        }
    }
}