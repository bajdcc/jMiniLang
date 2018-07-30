package com.bajdcc.LALR1.interpret.os

/**
 * 【操作系统】代码页接口
 *
 * @author bajdcc
 */
interface IOSCodePage {

    /**
     * 返回页名
     *
     * @return 页名
     */
    val name: String

    /**
     * 返回代码
     *
     * @return 代码
     */
    val code: String
}
