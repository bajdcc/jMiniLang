package com.bajdcc.util

/**
 * 位置
 *
 * @author bajdcc
 */
class Position : Cloneable {
    /**
     * 列号
     */
    var column = 0

    /**
     * 行号
     */
    var line = 0

    constructor()

    constructor(obj: Position) {
        column = obj.column
        line = obj.line
    }

    constructor(column: Int, line: Int) {
        this.column = column
        this.line = line
    }

    override fun toString(): String {
        return "$line,$column"
    }

    public override fun clone(): Any {
        return super.clone()
    }

    fun different(obj: Position): Boolean {
        return this.line != obj.line || this.column != obj.column
    }
}
