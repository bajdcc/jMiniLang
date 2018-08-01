package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage

/**
 * 【解释器】解释器扩展接口
 *
 * @author bajdcc
 */
interface IInterpreterModule {

    /**
     * 返回模块名
     *
     * @return 模块名
     */
    val moduleName: String

    /**
     * 返回模块代码
     *
     * @return 模块代码
     */
    val moduleCode: String

    /**
     * 返回代码页
     *
     * @return 代码页
     * @throws Exception 异常
     */
    val codePage: RuntimeCodePage
}
