package com.bajdcc.LALR1.semantic.tracker

/**
 * 下推自动机指令记录器链表
 * [prev] 前向指针
 * [insts] 指令集
 * @author bajdcc
 */
data class InstructionRecord(var prev: InstructionRecord?,
                             var insts: MutableList<Instruction> = mutableListOf())