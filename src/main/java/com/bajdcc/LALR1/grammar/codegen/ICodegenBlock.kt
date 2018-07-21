package com.bajdcc.LALR1.grammar.codegen

import com.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary

/**
 * 【目标代码生成】块接口
 *
 * @author bajdcc
 */
interface ICodegenBlock {

    val isInBlock: Boolean

    fun genBreak(): RuntimeInstUnary

    fun genContinue(): RuntimeInstUnary

    fun enterBlockEntry(block: CodegenBlock)

    fun leaveBlockEntry()
}
