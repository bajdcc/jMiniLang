package com.bajdcc.util

/**
 * 语句错误参数包
 * [position] 错误位置
 * [pass] 若为假，则状态机不进行错误状态转移
 * [read] 若为假，则状态机不跳过当前记号
 * [halt] 若为假，则状态机不结束分析
 * [giveUp] 若为真，则不处理错误
 * @author bajdcc
 */
data class TrackerErrorBag(var position: Position) {
    var pass: Boolean = false
    var read: Boolean = false
    var halt: Boolean = false
    var giveUp: Boolean = false
}