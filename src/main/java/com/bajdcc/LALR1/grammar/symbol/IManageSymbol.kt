package com.bajdcc.LALR1.grammar.symbol

/**
 * 符号表管理接口
 *
 * @author bajdcc
 */
interface IManageSymbol : IQuerySymbol {

    /**
     * 得到命名空间管理接口
     *
     * @return 命名空间管理接口
     */
    val manageScopeService: IManageScopeSymbol

    /**
     * 得到数据管理接口
     *
     * @return 数据管理接口
     */
    val manageDataService: IManageDataSymbol
}
