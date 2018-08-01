package com.bajdcc.util

/**
 * 遍历数据包
 * [visitChildren] 是否允许访问子结点
 * [visitEnd] 是否允许调用VisitEnd方法
 * @author bajdcc
 */
data class VisitBag (var visitChildren: Boolean = true, var visitEnd: Boolean = true)
