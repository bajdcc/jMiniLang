package com.bajdcc.LALR1.interpret.module.api

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * 【模块】API请求上下文
 *
 * @author bajdcc
 */
class ModuleNetWebApiContext() {

    private var sem: Semaphore? = null // 多线程同步只能用信号量

    var req = RuntimeMap()
    var resp = RuntimeObject(null)

    init {
        req.put("__ctx__", RuntimeObject(this))
        req.put("resp", RuntimeObject(null))
    }

    constructor(route: String) : this() {
        req.put("route", RuntimeObject(route))
    }

    constructor(route: String, params: Map<String, String>?) : this(route) {
        params?.forEach { key, value -> req.put(key, RuntimeObject(value)) }
    }

    fun block(): Boolean {
        sem = Semaphore(0)
        try {
            if (!sem!!.tryAcquire(3, TimeUnit.SECONDS)) {
                throw TimeoutException("Timed out.")
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun unblock() {
        try {
            while (sem == null) {
                Thread.sleep(100)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        sem!!.release()
    }
}
