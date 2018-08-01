package com.bajdcc.LALR1.semantic

import com.bajdcc.LALR1.grammar.semantic.ISemanticAction
import com.bajdcc.LALR1.grammar.semantic.ISemanticAnalyzer
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.symbol.IManageSymbol
import com.bajdcc.LALR1.grammar.symbol.IQuerySymbol
import com.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens
import com.bajdcc.LALR1.semantic.token.ParsingStack
import com.bajdcc.LALR1.semantic.tracker.Instruction
import com.bajdcc.LALR1.syntax.automata.npa.NPAInstruction
import com.bajdcc.LALR1.syntax.rule.RuleItem
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】语义指令运行时机器
 * [items] 规则集合
 * [actions] 语义动作集合
 * [tokens] 单词流
 * [query] 符号表查询接口
 * [manage] 符号表管理接口
 * [recorder] 语义错误处理接口
 * @author bajdcc
 */
class SemanticMachine(private val items: List<RuleItem>,
                      private val actions: List<ISemanticAction>,
                      private val tokens: List<Token>,
                      private val query: IQuerySymbol,
                      private val manage: IManageSymbol,
                      private val recorder: ISemanticRecorder,
                      val debug: Boolean = false) : IRandomAccessOfTokens {

    /**
     * 单词索引
     */
    private var index = 0

    /**
     * 语义动作接口
     */
    private var action: ISemanticAction? = null

    /**
     * 语义处理接口
     */
    private var handler: ISemanticAnalyzer? = null

    /**
     * 数据处理堆栈
     */
    private val ps = ParsingStack()

    /**
     * 结果
     */
    var obj: Any = Object()
        private set

    /**
     * 运行一个指令
     *
     * @param inst 指令
     */
    fun run(inst: Instruction) {
        /* 重置处理机制 */
        handler = null
        action = null
        /* 处理前 */
        if (inst.handler != -1) {
            when (inst.inst) {
                NPAInstruction.PASS, NPAInstruction.READ, NPAInstruction.SHIFT ->
                    action = actions[inst.handler]
                else ->
                    handler = items[inst.handler].handler
            }
        }
        action?.handle(ps, manage, this, recorder)
        when (inst.inst) {
            NPAInstruction.PASS -> index++
            NPAInstruction.READ -> {
                ps[inst.index] = tokens[index]
                index++
            }
            NPAInstruction.SHIFT -> ps.push()
            else -> {
            }
        }
        /* 处理时 */
        if (handler != null) {
            obj = handler!!.handle(ps, query, recorder)
        }
        /* 处理后 */
        when (inst.inst) {
            NPAInstruction.LEFT_RECURSION -> {
                ps.pop()// 先pop再push为了让栈层成为current的引用
                ps.push()
                ps[inst.index] = obj
            }
            NPAInstruction.LEFT_RECURSION_DISCARD -> {
                ps.pop()
                ps.push()
            }
            NPAInstruction.PASS -> {
            }
            NPAInstruction.READ -> {
            }
            NPAInstruction.SHIFT -> {
            }
            NPAInstruction.TRANSLATE -> {
                ps.pop()
                ps[inst.index] = obj
            }
            NPAInstruction.TRANSLATE_DISCARD -> ps.pop()
            NPAInstruction.TRANSLATE_FINISH -> ps.pop()
        }
        /* 打印调试信息 */
        if (debug) {
            System.err.println("#### $inst ####")
            System.err.println(ps.toString())
            System.err.println()
        }
    }

    override fun relativeGet(index: Int): Token {
        return tokens[this.index + index]
    }

    override fun positiveGet(index: Int): Token {
        return tokens[index]
    }
}
