package com.example.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn


sealed class FlowResult<out T> {

    data class Success<out T>(val data: T) : FlowResult<T>()
    data class Error(val exception: Throwable) : FlowResult<Nothing>()
}

fun <T> Flow<FlowResult<T>>.execute(): Flow<FlowResult<T>> {
    this.catch { e -> emit(FlowResult.Error(e)) }
        .flowOn(Dispatchers.IO)

    return this
}