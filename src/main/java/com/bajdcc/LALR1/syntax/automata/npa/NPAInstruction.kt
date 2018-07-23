package com.bajdcc.LALR1.syntax.automata.npa

/**
 * 非确定性下推自动机指令
 *
 * @author bajdcc
 */
enum class NPAInstruction constructor(var desc: String?) {
    PASS("通过"),
    READ("读入"),
    SHIFT("移进"),
    TRANSLATE("翻译"),
    LEFT_RECURSION("左递归"),
    TRANSLATE_DISCARD("翻译"),
    LEFT_RECURSION_DISCARD("左递归"),
    TRANSLATE_FINISH("翻译结束")
}
