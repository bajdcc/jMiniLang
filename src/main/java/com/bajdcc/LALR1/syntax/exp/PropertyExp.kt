package com.bajdcc.LALR1.syntax.exp

import com.bajdcc.LALR1.semantic.token.ISemanticAction
import com.bajdcc.LALR1.syntax.ISyntaxComponent
import com.bajdcc.LALR1.syntax.ISyntaxComponentVisitor
import com.bajdcc.LALR1.syntax.handler.IErrorHandler
import com.bajdcc.util.VisitBag

/**
 * 文法规则（属性）
 * [iStorage] 存储序号
 * [expression] 子表达式
 * @author bajdcc
 */
class PropertyExp(var iStorage: Int,
                  var expression: ISyntaxComponent) : ISyntaxComponent {

    /**
     * 错误处理
     */
    var errorHandler: IErrorHandler? = null

    /**
     * 动作名称
     */
    var actionHandler: ISemanticAction? = null

    override fun visit(visitor: ISyntaxComponentVisitor) {
        val bag = VisitBag()
        visitor.visitBegin(this, bag)
        if (bag.visitChildren) {
            expression.visit(visitor)
        }
        if (bag.visitEnd) {
            visitor.visitEnd(this)
        }
    }
}
