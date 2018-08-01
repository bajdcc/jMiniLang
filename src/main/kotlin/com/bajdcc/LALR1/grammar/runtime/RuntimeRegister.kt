package com.bajdcc.LALR1.grammar.runtime

/**
 * 【运行时】运行时寄存器
 * [pageId] 代码页
 * [execId] 程序计数器
 * @author bajdcc
 */
data class RuntimeRegister(var pageId: String = "", var execId: Int = -1)
