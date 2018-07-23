package com.bajdcc.LALR1.syntax.stringify

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LALR1.syntax.exp.*
import com.bajdcc.util.VisitBag
import java.util.*

class SyntaxToString : ISyntaxComponentVisitor {

    /**
     * 文法推导式描述
     */
    private val context = StringBuilder()

    /**
     * 存放结果的栈
     */
    private val stkStringList = Stack<MutableList<String>>()

    /**
     * 当前描述表
     */
    private var arrData = mutableListOf<String>()

    /**
     * 焦点
     */
    private var focusedExp: ISyntaxComponent? = null

    /**
     * LR项目符号'*'，是否移进当前符号
     */
    private var bFront = true

    constructor()

    constructor(exp: ISyntaxComponent, front: Boolean) {
        focusedExp = exp
        bFront = front
    }

    /**
     * 开始遍历子结点
     */
    private fun beginChilren() {
        stkStringList.push(mutableListOf())
    }

    /**
     * 结束遍历子结点
     */
    private fun endChilren() {
        arrData = stkStringList.pop()
    }

    /**
     * 保存结果
     *
     * @param exp    当前表达式结点
     * @param string 描述
     */
    private fun store(exp: ISyntaxComponent, string: String) {
        var str = string
        if (focusedExp === exp) {
            /* 添加LR项目集符号 */
            if (bFront) {
                str = "*$str"
            } else {
                str += " *"
            }
        }
        if (stkStringList.isEmpty()) {
            context.append(str)
        } else {
            stkStringList.peek().add(str)
        }
    }

    override fun visitBegin(node: TokenExp, bag: VisitBag) {
        bag.visitEnd = false
        store(node, " `" + node.name + "` ")
    }

    override fun visitBegin(node: RuleExp, bag: VisitBag) {
        bag.visitEnd = false
        store(node, " " + node.name + " ")
    }

    override fun visitBegin(node: SequenceExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitBegin(node: BranchExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitBegin(node: OptionExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitBegin(node: PropertyExp, bag: VisitBag) {
        beginChilren()
    }

    override fun visitEnd(node: TokenExp) {

    }

    override fun visitEnd(node: RuleExp) {

    }

    override fun visitEnd(node: SequenceExp) {
        endChilren()
        val sb = StringBuilder()
        arrData.forEach { sb.append(it) }
        store(node, sb.toString())
    }

    override fun visitEnd(node: BranchExp) {
        endChilren()
        val sb = StringBuilder()
        sb.append(" (")
        for (string in arrData) {
            sb.append(string)
            sb.append('|')
        }
        sb.deleteCharAt(sb.length - 1)
        sb.append(") ")
        store(node, sb.toString())
    }

    override fun visitEnd(node: OptionExp) {
        endChilren()
        store(node, " [" + arrData[0] + "] ")
    }

    override fun visitEnd(node: PropertyExp) {
        endChilren()
        store(node, arrData[0] + if (node.iStorage == -1) "" else "[" + node.iStorage + "] ")
    }

    override fun toString(): String {
        return context.toString()
    }
}
