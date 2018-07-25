package com.bajdcc.LALR1.semantic.tracker

/**
 * 错误记录器链表
 * [prev] 前向指针
 * [errors] 错误集
 * @author bajdcc
 */
data class ErrorRecord(var prev: ErrorRecord?,
                       var errors: MutableList<TrackerError> = mutableListOf<TrackerError>())