package com.fabianofranca.coroutinerunner

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

class Sample {

    fun asyncValue() : Deferred<String> = async { "ok" }

    suspend fun useAsyncValue() = asyncValue().await()
}