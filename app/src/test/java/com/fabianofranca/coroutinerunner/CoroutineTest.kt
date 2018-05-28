package com.fabianofranca.coroutinerunner

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(CoroutineRunner::class)
class CoroutineSampleTest {

    @Test
    fun sample_shouldRunNormalTest() {
        println("Normal Test")
    }

    @CoroutineTest
    suspend fun sample_shouldRunTestWithSuspendFunction() {
        println("Suspend Test")
    }
}