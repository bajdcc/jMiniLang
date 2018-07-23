package com.bajdcc.LALR1.syntax.automata.nga

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import java.util.*

/**
 * 非确定性文法自动机结果包
 * [stkNGA] NGA栈
 * [childNGA] NGA子表
 * [nga] 存储结果的ENGA
 * [prefix] 标记前缀
 * [expression] 表达式
 * @author bajdcc
 */
data class NGABag(var stkNGA: Stack<MutableList<ENGA>> = Stack(),
                  var childNGA: MutableList<ENGA> = mutableListOf(),
                  var nga: ENGA? = null,
                  var prefix: String = "",
                  var expression: ISyntaxComponent? = null)