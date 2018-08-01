package com.bajdcc.LALR1.syntax.exp

import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.token.TokenType

/**
 * 文法规则（终结符）
 * [id] 终结符ID
 * [name] 终结符名称
 * [type] 终结符对应的正则表达式
 * [obj] 终结符对应的正则表达式解析组件（用于语义分析中的单词流解析）
 * @author bajdcc
 */
class TokenExp(var id: Int,
               var name: String,
               var type: TokenType,
               var obj: Any?) : ISyntaxComponent {

    override fun visit(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun toString(): String {
        return "$id: `$name`，${type.desc}，${obj?.toString() ?: "(null)"}"
    }
}
