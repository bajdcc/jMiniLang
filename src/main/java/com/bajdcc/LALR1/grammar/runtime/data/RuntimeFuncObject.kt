package com.bajdcc.LALR1.grammar.runtime.data

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import java.util.*

/**
 * 【运行时】函数调用
 *
 * @author bajdcc
 */
class RuntimeFuncObject(val page: String, val addr: Int) {
    val env = HashMap<Int, RuntimeObject>()

    fun getEnv(): Map<Int, RuntimeObject> {
        return env
    }

    fun addEnv(id: Int, obj: RuntimeObject) {
        env[id] = obj
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(page)
        sb.append(',')
        sb.append(addr)
        if (!env.isEmpty()) {
            sb.append(',')
            sb.append(env.toString())
        }
        return sb.toString()
    }
}
