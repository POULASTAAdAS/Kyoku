package com.poulastaa.common.ui.viewmodel

import com.poulastaa.common.ui.utils.ioDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

actual open class FlowWrapper<VALUE> actual constructor(private val flow: Flow<VALUE>) :
    Flow<VALUE> by flow {
    fun subscribe(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        onCollect: (VALUE) -> Unit,
    ): DisposableHandle {
        val job: Job = coroutineScope.launch(dispatcher) {
            flow.onEach { onCollect(it) }.flowOn(ioDispatcher).collect()
        }
        return DisposableHandle {
            job.cancel()
        }
    }

    fun subscribe(coroutineScope: CoroutineScope, onCollect: (VALUE) -> Unit): DisposableHandle {
        val job: Job = coroutineScope.launch(Dispatchers.Main) {
            flow.onEach { onCollect(it) }.flowOn(ioDispatcher).collect()
        }
        return DisposableHandle {
            job.cancel()
        }
    }

    fun subscribe(onCollect: (VALUE) -> Unit): DisposableHandle {
        @Suppress("OPT_IN_USAGE")
        return subscribe(
            coroutineScope = GlobalScope,
            dispatcher = Dispatchers.Main,
            onCollect = onCollect
        )
    }
}