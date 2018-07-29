package com.bajdcc.LALR1.grammar.runtime

/**
 * 【扩展】外部化过程接口
 *
 * @author bajdcc
 */
class RuntimeDebugExec(val doc: String = "",
                       val type: Array<RuntimeObjectType> = arrayOf(),
                       var paramsDoc: String = type.joinToString(separator = "，", transform = { it.desc }),
                       val call: ((List<RuntimeObject>, IRuntimeStatus) -> RuntimeObject?)? = null)