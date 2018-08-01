package com.bajdcc.OP.syntax.exp

import com.bajdcc.OP.syntax.ISyntaxComponent
import com.bajdcc.OP.syntax.ISyntaxComponentVisitor
import com.bajdcc.util.VisitBag
import com.bajdcc.util.lexer.token.TokenType

/**
 * 文法规则（终结符）
 *
 * @author bajdcc
 */
class TokenExp(var id: Int,
               var name: String,
               var kType: TokenType,
               var `object`: Any?) : ISyntaxComponent {

    override fun visit(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun visitReverse(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }

    override fun toString(): String {
        return String.format("%d: `%s`，%s，%s", id, name, kType.desc,
                if (`object` == null) "(null)" else `object`!!.toString())
    }
}
