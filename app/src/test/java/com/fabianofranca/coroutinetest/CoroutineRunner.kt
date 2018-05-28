package com.fabianofranca.coroutinetest

import org.junit.internal.runners.statements.InvokeMethod
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.junit.runners.model.TestClass
import java.lang.reflect.Method

class CoroutineRunner(testClass: Class<*>) : BlockJUnit4ClassRunner(testClass) {

    val async = AsyncTestRunner()

    override fun validatePublicVoidNoArgMethods(
        annotation: Class<out Annotation>?,
        isStatic: Boolean,
        errors: MutableList<Throwable>?
    ) {
        val methods = testClass.getAnnotatedMethods(annotation)

        for (eachTestMethod in methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors)
        }
    }


    override fun getChildren(): MutableList<FrameworkMethod> {
        val children = mutableListOf<FrameworkMethod>()
        children.addAll(super.getChildren())

//        val method = async::class.java.getMethod(AsyncTestRunner::run.name)
//
//        children.add(CoroutineFrameworkMethod("SampleTest", method))

        return children
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

    override fun validatePublicVoid(isStatic: Boolean, errors: MutableList<Throwable>?) {
        super.validatePublicVoid(isStatic, errors)
    }

    override fun validatePublicVoidNoArg(isStatic: Boolean, errors: MutableList<Throwable>?) {
        super.validatePublicVoidNoArg(isStatic, errors)
    }
}