package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(markerClass = [ExperimentalForInheritanceCoroutinesApi::class])
actual class MutableStateFlowWrapper<VALUE> actual constructor(private val flow: MutableStateFlow<VALUE>) :
    StateFlowWrapper<VALUE>(flow), MutableStateFlow<VALUE> {

    actual override var value: VALUE
        get() = super.value
        set(value) {
            flow.value = value
        }

    actual override val subscriptionCount: StateFlow<Int> = flow.subscriptionCount

    actual override suspend fun emit(value: VALUE) = flow.emit(value)

    @ExperimentalCoroutinesApi
    actual override fun resetReplayCache() = flow.resetReplayCache()

    actual override fun tryEmit(value: VALUE): Boolean = flow.tryEmit(value)

    actual override fun compareAndSet(expect: VALUE, update: VALUE): Boolean =
        flow.compareAndSet(expect, update)
}