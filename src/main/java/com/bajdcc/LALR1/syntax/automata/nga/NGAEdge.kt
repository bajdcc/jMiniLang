package com.bajdcc.LALR1.syntax.automata.nga

/**
 * 非确定性文法自动机边
 * [begin] 初态
 * [end] 终态
 * [data] 数据
 * @author bajdcc
 */
data class NGAEdge(var begin: NGAStatus? = null, var end: NGAStatus? = null, var data: NGAEdgeData = NGAEdgeData())