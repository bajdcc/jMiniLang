package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope

/**
 * 【语义类型】通用语义接口
 *
 * @author bajdcc
 */
interface ICommon {

    /**
     * 语义分析
     *
     * @param recorder 错误记录器
     */
    fun analysis(recorder: ISemanticRecorder)

    /**
     * 生成中间代码
     *
     * @param codegen 代码生成接口
     */
    fun genCode(codegen: ICodegen)

    /**
     * 输出
     *
     * @param prefix 前缀空白
     * @return 结点内容
     */
    fun print(prefix: StringBuilder): String

    /**
     * 求函数闭包
     *
     * @param scope 闭包操作接口
     */
    fun addClosure(scope: IClosureScope)
}
