package com.poulastaa.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.ui.backgroundDark
import com.poulastaa.core.presentation.designsystem.ui.backgroundLight
import com.poulastaa.core.presentation.designsystem.ui.secondaryDark
import com.poulastaa.core.presentation.designsystem.ui.secondaryLight
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemChanger @Inject constructor() {
    companion object Companion : ViewModel() {
        private var ds: DatastoreRepository? = null
        var them by mutableStateOf(true) // initial value is darkTheme
            private set

        internal fun init(ds: DatastoreRepository) {
            this.ds = ds
        }

        init {
            loadThem()
        }

        @Composable
        fun getGradiantBackground() = when (them) {
            true -> listOf(
                backgroundDark,
                secondaryDark
            )

            false -> listOf(
                backgroundLight,
                secondaryLight
            )
        }

        fun toggleTheme() {
            viewModelScope.launch {
                ds?.storeThem(them.not())
            }
        }

        private fun loadThem() {
            viewModelScope.launch {
                ds?.readThem()?.collectLatest { state ->
                    them = state
                }
            }
        }
    }
}
