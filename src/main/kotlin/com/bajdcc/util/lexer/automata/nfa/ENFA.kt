package com.bajdcc.util.lexer.automata.nfa

/**
 * EpsilonNFA
 * [begin] 初态
 * [end] 终态
 * @author bajdcc
 */
data class ENFA(var begin: NFAStatus? = null,
                var end: NFAStatus? = null)
