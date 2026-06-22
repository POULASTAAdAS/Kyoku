package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
expect class MutableStateFlowWrapper<VALUE>(flow: MutableStateFlow<VALUE>) :
    MutableStateFlow<VALUE> {
    override fun compareAndSet(expect: VALUE, update: VALUE): Boolean
    override var value: VALUE
    override suspend fun collect(collector: FlowCollector<VALUE>): Nothing
    override val replayCache: List<VALUE>
    override suspend fun emit(value: VALUE)

    @ExperimentalCoroutinesApi
    override fun resetReplayCache()
    override fun tryEmit(value: VALUE): Boolean
    override val subscriptionCount: StateFlow<Int>
}

fun <VALUE> MutableStateFlow<VALUE>.wrap() = MutableStateFlowWrapper(this)