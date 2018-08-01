package com.bajdcc.LALR1.grammar.symbol

import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder

/**
 * 【语义分析】符号表
 *
 * @author bajdcc
 */
class SymbolTable : IQuerySymbol, IManageSymbol {

    /**
     * 符号表管理
     */
    private val manageScopeSymbol = ManageScopeSymbol()

    override val manageScopeService: IManageScopeSymbol
        get() = manageScopeSymbol

    override val queryScopeService: IQueryScopeSymbol
        get() = manageScopeSymbol

    override val manageDataService: IManageDataSymbol
        get() = manageScopeSymbol

    override val queryBlockService: IQueryBlockSymbol
        get() = manageScopeSymbol

    override val manageService: IManageSymbol
        get() = this

    fun check(recorder: ISemanticRecorder) {
        manageScopeSymbol.check(recorder)
    }

    override fun toString(): String {
        return manageScopeSymbol.toString()
    }
}
