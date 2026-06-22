package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

@OptIn(markerClass = [ExperimentalForInheritanceCoroutinesApi::class])
actual open class StateFlowWrapper<VALUE> actual constructor(private val flow: StateFlow<VALUE>) :
    StateFlow<VALUE> by flow