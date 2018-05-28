package com.fabianofranca.coroutinerunner

import org.junit.internal.runners.statements.InvokeMethod
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import java.lang.reflect.Method

open class CoroutineRunner(testClass: Class<*>) : BlockJUnit4ClassRunner(testClass) {

    val async = AsyncTestRunner()

    override fun getChildren(): MutableList<FrameworkMethod> {
        val children = mutableListOf<FrameworkMethod>()
        children.addAll(super.getChildren())

        val coroutineMethods = mutableListOf<FrameworkMethod>()

        val method = async::class.java.getMethod(AsyncTestRunner::run.name)

        getCoroutineChildren().forEach {
            coroutineMethods.add(CoroutineFrameworkMethod(it.method.name, method))
        }

        children.addAll(coroutineMethods)

        return children
    }

    protected fun getCoroutineChildren(): List<FrameworkMethod> {
        return testClass.getAnnotatedMethods(CoroutineTest::class.java)
    }

    override fun methodInvoker(method: FrameworkMethod?, test: Any?): Statement {

        return if (method is CoroutineFrameworkMethod) {
            InvokeMethod(method, async)
        } else {
            super.methodInvoker(method, test)
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
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CoroutineTest