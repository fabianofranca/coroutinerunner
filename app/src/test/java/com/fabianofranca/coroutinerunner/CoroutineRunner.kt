package com.fabianofranca.coroutinerunner

import org.junit.internal.runners.statements.InvokeMethod
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import java.lang.reflect.Method

open class CoroutineRunner(testClass: Class<*>) : BlockJUnit4ClassRunner(testClass) {

    private lateinit var async: AsyncTestRunner
    private lateinit var coroutineMethods: List<CoroutineFrameworkMethod>

    protected fun getCoroutineChildren(): List<CoroutineFrameworkMethod> {
        if (!::coroutineMethods.isInitialized) {
            val methods = mutableListOf<CoroutineFrameworkMethod>()

            if (!::async.isInitialized) {
                async = AsyncTestRunner()
            }

            val asyncMethod = async::class.java.getMethod(AsyncTestRunner::run.name)

            testClass.getAnnotatedMethods(CoroutineTest::class.java).forEach {
                methods.add(CoroutineFrameworkMethod(it.method.name, asyncMethod))
            }

            coroutineMethods = methods
        }

        return coroutineMethods
    }

    override fun getChildren(): MutableList<FrameworkMethod> {
        val children = mutableListOf<FrameworkMethod>()
        children.addAll(super.getChildren())
        children.addAll(getCoroutineChildren())

        return children
    }

    override fun methodInvoker(method: FrameworkMethod?, test: Any?): Statement {
        return if (method is CoroutineFrameworkMethod) {
            InvokeMethod(method, async)
        } else {
            super.methodInvoker(method, test)
        }
    }

    override fun validateTestMethods(errors: MutableList<Throwable>) {
        super.validateTestMethods(errors)
        validatePublicVoidNoArgCoroutineMethods(false, errors)
    }

    protected fun validatePublicVoidNoArgCoroutineMethods(
        isStatic: Boolean,
        errors: MutableList<Throwable>
    ) {
        for (method in getCoroutineChildren()) {
            method.validatePublicVoidNoArg(isStatic, errors)
        }
    }

    class AsyncTestRunner {
        fun run() {
            println("Run Forest, Run!!!")
        }

    }
}

class CoroutineFrameworkMethod(private val name: String, method: Method) :
    FrameworkMethod(method) {

    override fun getName(): String {
        return name
    }

    override fun validatePublicVoidNoArg(isStatic: Boolean, errors: MutableList<Throwable>?) {
        if (isStatic() != isStatic) {
            val state = if (isStatic) "should" else "should not"
            errors?.add(Exception("Method " + method.name + "() " + state + " be static"))
        }
        if (!isPublic) {
            errors?.add(Exception("Method " + method.name + "() should be public"))
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CoroutineTest