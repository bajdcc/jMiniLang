package com.bajdcc.util.lexer.token

/**
 * 字符类型
 *
 * @author bajdcc
 */
enum class MetaType constructor(var char: Char) {
    CHARACTER('\u0000'),
    LPARAN('('),
    RPARAN(')'),
    STAR('*'),
    PLUS('+'),
    CARET('^'),
    QUERY('?'),
    LSQUARE('['),
    RSQUARE(']'),
    BAR('|'),
    ESCAPE('\\'),
    DASH('-'),
    LBRACE('{'),
    RBRACE('}'),
    COMMA(','),
    DOT('.'),
    NEW_LINE('\n'),
    CARRIAGE_RETURN('\r'),
    BACKSPACE('\b'),
    DOUBLE_QUOTE('\"'),
    SINGLE_QUOTE('\''),
    END('\u0000'),
    ERROR('\u0000'),
    NULL('\u0000'),
    MUST_SAVE('\u0000'),
    TERMINAL('`'),
    LT('<'),
    GT('>'),
    SHARP('#')
}
