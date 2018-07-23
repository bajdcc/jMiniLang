package com.bajdcc.LALR1.syntax.automata.nga

/**
 * EpsilonNGA
 * [begin] 初态
 * [end] 终态
 * @author bajdcc
 */
data class ENGA(var begin: NGAStatus? = null, var end: NGAStatus? = null)