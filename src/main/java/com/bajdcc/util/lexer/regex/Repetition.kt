package com.bajdcc.util.lexer.regex

/**
 * 循环功能
 * [component] 循环部件表达式
 * [lowerBound] 循环次数下限
 * [upperBound] 循环次数上限
 * @author bajdcc
 */
data class Repetition (val component: IRegexComponent,
                       var lowerBound: Int,
                       var upperBound: Int) : IRegexComponent {

    override fun visit(visitor: IRegexComponentVisitor) {
        visitor.visitBegin(this)
        component.visit(visitor)
        visitor.visitEnd(this)
    }
}
