package com.bajdcc.LALR1.grammar.runtime

import java.util.*

/**
 * 【运行时】调用函数基本单位
 *
 * @author bajdcc
 */
class RuntimeFunc {

    /**
     * 调用地址
     */
    var address = -1

    /**
     * 名称
     */
    var name: String? = null

    /**
     * 当前的代码页名
     */
    var currentPc = 0

    /**
     * 当前的代码页名
     */
    var currentPage = "extern"

    /**
     * 保存的返回指令地址
     */
    var retPc = 0

    /**
     * 保存的返回代码页名
     */
    var retPage = ""

    /**
     * 保存的异常跳转地址
     */
    var tryJmp = -1

    /**
     * 参数
     */
    private var params = mutableListOf<RuntimeObject>()

    /**
     * 临时变量
     */
    private var tmp = mutableListOf<MutableMap<Int, RuntimeObject>>()

    /**
     * 函数闭包
     */
    private var closure = mutableMapOf<Int, RuntimeObject>()

    /**
     * YIELD
     */
    var yields = 0
        private set

    val simpleName: String
        get() {
            if (name == null)
                return "extern"
            val s = name!!.split("\\$".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return s[s.size - 1]
        }

    fun addYield() {
        this.yields++
    }

    fun popYield() {
        this.yields--
    }

    fun resetYield() {
        this.yields = 0
    }

    init {
        enterScope()
    }

    fun getParam(idx: Int): RuntimeObject {
        return params[idx]
    }

    fun getParams(): List<RuntimeObject> {
        return params
    }

    fun addParams(param: RuntimeObject) {
        params.add(param)
    }

    fun getTmp(): List<Map<Int, RuntimeObject>> {
        return tmp
    }

    fun addTmp(idx: Int, `val`: RuntimeObject) {
        tmp[0].put(idx, `val`)
    }

    fun enterScope() {
        tmp.add(0, mutableMapOf())
    }

    fun leaveScope() {
        tmp.removeAt(0)
    }

    fun getClosure(): Map<Int, RuntimeObject> {
        return closure
    }

    fun addClosure(idx: Int, `val`: RuntimeObject) {
        closure[idx] = `val`
    }

    override fun toString(): String {
        return System.lineSeparator() + "代码页：$currentPage，地址：$currentPc，名称：${name ?: "extern"}，参数：$params，变量：$tmp，闭包：$closure"
    }

    fun copy(): RuntimeFunc {
        val func = RuntimeFunc()
        func.address = address
        func.name = name
        func.currentPc = currentPc
        func.currentPage = currentPage
        func.retPc = retPc
        func.retPage = retPage
        func.tryJmp = tryJmp
        func.params = ArrayList(params)
        func.tmp = tmp.map { it.entries.associate { it.key to it.value.clone() }.toMutableMap() }.toMutableList()
        func.closure = closure.entries.associate { it.key to it.value.clone() }.toMutableMap()
        func.yields = yields
        return func
    }
}
