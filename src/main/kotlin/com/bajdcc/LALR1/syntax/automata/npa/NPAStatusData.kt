package com.bajdcc.LALR1.syntax.automata.npa

/**
 * 非确定性下推自动机状态数据
 * [label] 标签
 * [rule] 状态所属规则的索引
 * @author bajdcc
 */
data class NPAStatusData(var label: String = "", var rule: Int = -1)