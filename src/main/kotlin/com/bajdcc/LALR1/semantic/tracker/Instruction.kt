package com.bajdcc.LALR1.semantic.tracker

import com.bajdcc.LALR1.syntax.automata.npa.NPAInstruction

/**
 * 下推自动机指令结构
 * [inst] 指令
 * [index] 参数
 * [handler] 处理器
 * @author bajdcc
 */
data class Instruction(var inst: NPAInstruction = NPAInstruction.PASS,
                       var index: Int = -1,
                       var handler: Int = -1) {

    override fun toString(): String {
        return String.format("指令：%5s \t参数：%4d \t处理：%d", inst.desc, index, handler)
    }
}
