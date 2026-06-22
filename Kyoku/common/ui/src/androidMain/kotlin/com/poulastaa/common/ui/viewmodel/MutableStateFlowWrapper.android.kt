package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(markerClass = [ExperimentalForInheritanceCoroutinesApi::class])
actual class MutableStateFlowWrapper<VALUE> actual constructor(private val flow: MutableStateFlow<VALUE>) :
    MutableStateFlow<VALUE> by flow