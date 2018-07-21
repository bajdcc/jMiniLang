package com.bajdcc.LALR1.grammar.symbol

/**
 * 块查询接口
 *
 * @author bajdcc
 */
interface IQueryBlockSymbol {

    /**
     * 进入循环体
     *
     * @param type 块类型
     */
    fun enterBlock(type: BlockType)

    /**
     * 离开循环体
     *
     * @param type 块类型
     */
    fun leaveBlock(type: BlockType)

    /**
     * 是否在循环体内
     *
     * @param type 块类型
     * @return 在循环体内则为真
     */
    fun isInBlock(type: BlockType): Boolean
}