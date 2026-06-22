package com.poulastaa.common.ui.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

expect open class FlowWrapper<VALUE>(flow: Flow<VALUE>) : Flow<VALUE> {
    override suspend fun collect(collector: FlowCollector<VALUE>)
}

fun <VALUE> Flow<VALUE>.wrap() = FlowWrapper(this)