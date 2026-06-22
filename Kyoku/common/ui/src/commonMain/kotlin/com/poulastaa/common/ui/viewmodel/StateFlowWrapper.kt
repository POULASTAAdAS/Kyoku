package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
expect open class StateFlowWrapper<VALUE>(flow: StateFlow<VALUE>) : StateFlow<VALUE> {
    override val replayCache: List<VALUE>
    override val value: VALUE
    override suspend fun collect(collector: FlowCollector<VALUE>): Nothing
}

fun <VALUE> StateFlow<VALUE>.wrap() = StateFlowWrapper(this)