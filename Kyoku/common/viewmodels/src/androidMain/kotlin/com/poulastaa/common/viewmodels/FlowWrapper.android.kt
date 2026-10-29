package com.poulastaa.common.viewmodels

import kotlinx.coroutines.flow.Flow

actual open class FlowWrapper<VALUE> actual constructor(private val flow: Flow<VALUE>) :
    Flow<VALUE> by flow