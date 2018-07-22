package com.bajdcc.LALR1.grammar.runtime

/**
 * 【扩展】外部化值接口
 *
 * @author bajdcc
 */
interface IRuntimeDebugValue {

    /**
     * 获取外部化对象
     *
     * @return 外部化对象
     */
    val runtimeObject: RuntimeObject
}
