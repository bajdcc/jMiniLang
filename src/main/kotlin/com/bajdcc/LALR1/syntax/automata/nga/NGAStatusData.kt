package com.bajdcc.LALR1.syntax.automata.nga

/**
 * 非确定性文法自动机状态数据
 * [label] 标签
 * [final] 是否为终态
 * @author bajdcc
 */
data class NGAStatusData(var label: String = "", var final: Boolean = false)