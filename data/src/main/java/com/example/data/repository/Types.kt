package com.example.data.repository

import com.example.domain.usecase.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeApiCall(request: suspend () -> T): FlowResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            FlowResult.Success(request.invoke())
        } catch (e: Throwable) {
            FlowResult.Error(e)
        }
    }
}
