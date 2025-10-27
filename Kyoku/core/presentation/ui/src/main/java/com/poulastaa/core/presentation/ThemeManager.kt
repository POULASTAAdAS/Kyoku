package com.poulastaa.core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.poulastaa.core.domain.ThemColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton


class ThemeManager @Inject constructor() {
    private val _isModeDark = MutableStateFlow(true)
    val isModeDark = _isModeDark.asStateFlow()
    var themColor by mutableStateOf(ThemColor.BASE)
        private set

    //        private var ds: DatastoreRepository? = null

    fun loadOrChangeTheme(isSystemThemDark: Boolean) {
        // TODO: if saved them is null set system them as default
        _isModeDark.update {
            isSystemThemDark
        }
    }
}