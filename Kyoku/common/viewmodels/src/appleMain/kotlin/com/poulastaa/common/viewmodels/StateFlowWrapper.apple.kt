package com.poulastaa.common.viewmodels

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@OptIn(markerClass = [ExperimentalForInheritanceCoroutinesApi::class])
actual open class StateFlowWrapper<VALUE> actual constructor(private val flow: StateFlow<VALUE>) :
    FlowWrapper<VALUE>(flow), StateFlow<VALUE> {
    actual override val replayCache: List<VALUE>
        get() = flow.replayCache

    actual override val value: VALUE
        get() = flow.value

    actual override suspend fun collect(collector: FlowCollector<VALUE>): Nothing =
        flow.collect(collector)
}