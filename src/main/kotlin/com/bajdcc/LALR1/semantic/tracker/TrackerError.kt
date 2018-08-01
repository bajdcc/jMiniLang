package com.bajdcc.LALR1.semantic.tracker

import com.bajdcc.util.Position

/**
 * 错误
 * [position] 位置
 * [message] 错误信息
 * @author bajdcc
 */
data class TrackerError(var position: Position, var message: String = "") {

    override fun toString(): String {
        return "位置：[$position] 信息：$message"
    }
}
