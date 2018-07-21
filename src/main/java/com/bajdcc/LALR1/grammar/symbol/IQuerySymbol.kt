package com.bajdcc.LALR1.grammar.symbol

/**
 * 符号表查询接口
 *
 * @author bajdcc
 */
interface IQuerySymbol {

    /**
     * 得到命名空间查询接口
     *
     * @return 命名空间查询接口
     */
    val queryScopeService: IQueryScopeSymbol

    /**
     * 得到块查询接口
     *
     * @return 块查询接口
     */
    val queryBlockService: IQueryBlockSymbol

    /**
     * 获取管理接口（尽量少用）
     * @return 管理接口
     */
    val manageService: IManageSymbol
}
