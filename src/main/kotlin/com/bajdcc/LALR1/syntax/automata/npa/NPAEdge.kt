package com.bajdcc.LALR1.syntax.automata.npa

/**
 * 非确定性下推自动机边
 * [begin] 初态
 * [end] 终态
 * [data] 数据
 * @author bajdcc
 */
data class NPAEdge (var begin: NPAStatus? = null, var end: NPAStatus? = null, var data: NPAEdgeData = NPAEdgeData())