package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@OptIn(markerClass = [ExperimentalForInheritanceCoroutinesApi::class])
actual open class StateFlowWrapper<VALUE> actual constructor(private val flow: StateFlow<VALUE>) :
    FlowWrapper<VALUE>(flow), StateFlow<VALUE> {
    actual override val replayCache: List<VALUE> = flow.replayCache

    actual override val value: VALUE = flow.value

    actual override suspend fun collect(collector: FlowCollector<VALUE>): Nothing =
        flow.collect(collector)
}