package com.bajdcc.util.lexer.token

import com.bajdcc.util.Position

/**
 * 单词
 *
 * @author bajdcc
 */
class Token : Cloneable {
    /**
     * 单词类型
     */
    var type = TokenType.ERROR

    /**
     * 数据
     */
    var obj: Any? = null

    /**
     * 位置
     */
    var position = Position()

    fun toRealString(): String {
        return getRealString(type, obj ?: "")
    }

    private fun toSimpleString(): String {
        return when (type) {
            TokenType.KEYWORD -> (obj as KeywordType).desc
            TokenType.OPERATOR -> (obj as OperatorType).desc
            else -> ""
        }
    }

    override fun toString(): String {
        return String.format("%04d,%03d:\t%s\t%s %s", position.line,
                position.column, type.desc,
                obj?.toString() ?: "(null)",
                if (obj == null) "(null)" else toSimpleString())
    }

    fun copy(): Token {
        return clone() as Token
    }

    companion object {
        fun getRealString(type: TokenType, obj: Any): String = when (type) {
            TokenType.KEYWORD -> (obj as KeywordType).desc
            TokenType.OPERATOR -> (obj as OperatorType).desc
            TokenType.STRING -> "\"" + obj.toString() + "\""
            TokenType.CHARACTER -> {
                val ch = obj as Char
                String.format("'%s'",
                        (if (Character.isISOControl(ch))
                            String.format("\\u%04x", ch.toInt())
                        else
                            ch + ""))
            }
            TokenType.BOOL, TokenType.DECIMAL, TokenType.INTEGER, TokenType.ID -> obj.toString()
            else -> ""
        }

        fun createFromObject(obj: Any): Token {
            val token = Token()
            when (obj) {
                is String -> token.type = TokenType.STRING
                is Char -> token.type = TokenType.CHARACTER
                is Long -> token.type = TokenType.INTEGER
                is Double -> token.type = TokenType.DECIMAL
                is Boolean -> token.type = TokenType.BOOL
                is Int -> token.type = TokenType.POINTER
                else -> {
                    token.type = TokenType.ERROR
                    return token
                }
            }
            token.obj = obj
            return token
        }
    }
}