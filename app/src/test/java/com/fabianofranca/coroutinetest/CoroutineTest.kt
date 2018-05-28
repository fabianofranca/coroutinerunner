package com.fabianofranca.coroutinetest

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(CoroutineRunner::class)
class CoroutineTest {

    @Test
    fun sample_shouldRunNormalTest() {
        println("Normal Test")
    }

    @Test
    suspend fun sample_shouldRunTestWithSuspendFunction() {
        println("Suspend Test")
    }
}