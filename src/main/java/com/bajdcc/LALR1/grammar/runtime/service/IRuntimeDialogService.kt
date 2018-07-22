package com.bajdcc.LALR1.grammar.runtime.service

import javax.swing.JPanel

/**
 * 【运行时】运行时对话框服务接口
 *
 * @author bajdcc
 */
interface IRuntimeDialogService {

    /**
     * 创建对话框
     *
     * @param caption 标题
     * @param text    内容
     * @param mode    类型
     * @param panel   父窗口
     * @return 对话框句柄
     */
    fun create(caption: String, text: String, mode: Int, panel: JPanel): Int


    /**
     * 弹出对话框
     *
     * @param handle 句柄
     * @return 是否成功
     */
    fun show(handle: Int): Boolean
}
