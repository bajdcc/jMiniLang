package com.bajdcc.LALR1.grammar.type

import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.ExpBinop
import com.bajdcc.LALR1.grammar.tree.ExpSinop
import com.bajdcc.LALR1.grammar.tree.ExpTriop
import com.bajdcc.LALR1.grammar.tree.ExpValue
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语义分析】一元表达式辅助计算工具
 *
 * @author bajdcc
 */
object TokenTools {

    private val mapConverter = mutableMapOf<TokenType, ITokenConventer>()
    private val mapOp2Ins = mutableMapOf<OperatorType, RuntimeInst>()
    private val mapIns2Op = mutableMapOf<RuntimeInst, OperatorType>()

    init {
        mapConverter[TokenType.BOOL] = ConvertToBoolean()
        mapConverter[TokenType.CHARACTER] = ConvertToChar()
        mapConverter[TokenType.INTEGER] = ConvertToInt()
        mapConverter[TokenType.DECIMAL] = ConvertToDecimal()
        mapConverter[TokenType.STRING] = ConvertToString()
    }

    init {
        mapOp2Ins[OperatorType.LOGICAL_NOT] = RuntimeInst.inot
        mapOp2Ins[OperatorType.BIT_NOT] = RuntimeInst.iinv
        mapOp2Ins[OperatorType.PLUS_PLUS] = RuntimeInst.iinc
        mapOp2Ins[OperatorType.MINUS_MINUS] = RuntimeInst.idec
        mapOp2Ins[OperatorType.PLUS] = RuntimeInst.iadd
        mapOp2Ins[OperatorType.MINUS] = RuntimeInst.isub
        mapOp2Ins[OperatorType.TIMES] = RuntimeInst.imul
        mapOp2Ins[OperatorType.DIVIDE] = RuntimeInst.idiv
        mapOp2Ins[OperatorType.MOD] = RuntimeInst.imod
        mapOp2Ins[OperatorType.LEFT_SHIFT] = RuntimeInst.ishl
        mapOp2Ins[OperatorType.RIGHT_SHIFT] = RuntimeInst.ishr
        mapOp2Ins[OperatorType.LOGICAL_AND] = RuntimeInst.iandl
        mapOp2Ins[OperatorType.LOGICAL_OR] = RuntimeInst.iorl
        mapOp2Ins[OperatorType.BIT_AND] = RuntimeInst.iand
        mapOp2Ins[OperatorType.BIT_OR] = RuntimeInst.ior
        mapOp2Ins[OperatorType.BIT_XOR] = RuntimeInst.ixor
        mapOp2Ins[OperatorType.LESS_THAN] = RuntimeInst.icl
        mapOp2Ins[OperatorType.LESS_THAN_OR_EQUAL] = RuntimeInst.icle
        mapOp2Ins[OperatorType.GREATER_THAN] = RuntimeInst.icg
        mapOp2Ins[OperatorType.GREATER_THAN_OR_EQUAL] = RuntimeInst.icge
        mapOp2Ins[OperatorType.EQUAL] = RuntimeInst.ice
        mapOp2Ins[OperatorType.NOT_EQUAL] = RuntimeInst.icne
        mapOp2Ins.forEach { (key, value) ->
            mapIns2Op[value] = key
        }
        mapOp2Ins[OperatorType.PLUS_ASSIGN] = RuntimeInst.iadd
        mapOp2Ins[OperatorType.MINUS_ASSIGN] = RuntimeInst.isub
        mapOp2Ins[OperatorType.TIMES_ASSIGN] = RuntimeInst.imul
        mapOp2Ins[OperatorType.DIV_ASSIGN] = RuntimeInst.idiv
        mapOp2Ins[OperatorType.MOD_ASSIGN] = RuntimeInst.imod
        mapOp2Ins[OperatorType.AND_ASSIGN] = RuntimeInst.iand
        mapOp2Ins[OperatorType.OR_ASSIGN] = RuntimeInst.ior
        mapOp2Ins[OperatorType.XOR_ASSIGN] = RuntimeInst.ixor
        mapOp2Ins[OperatorType.EQ_ASSIGN] = RuntimeInst.ice
    }

    /**
     * 单目运算
     *
     * @param recorder 错误记录
     * @param exp      表达式
     * @return 运算是否合法
     */
    fun sinop(recorder: ISemanticRecorder, exp: ExpSinop): Boolean {
        val value = exp.operand as ExpValue?
        val token = value!!.token
        val type = exp.token.obj as OperatorType?
        if (sin(type!!, token)) {
            return true
        }
        recorder.add(SemanticError.INVALID_OPERATOR, token)
        return false
    }

    /**
     * 单目运算
     *
     * @param type  操作符
     * @param token 操作数
     * @return 运算是否合法
     */
    fun sin(type: OperatorType, token: Token): Boolean {
        when (type) {
            OperatorType.LOGICAL_NOT -> {
                val bool = mapConverter[TokenType.BOOL]!!.convert(token)
                if (bool.type === TokenType.BOOL) {
                    bool.obj = !(bool.obj as Boolean)
                    return true
                }
            }
            OperatorType.BIT_NOT -> if (token.type === TokenType.INTEGER) {
                token.obj = (token.obj as Long).inv()
                return true
            } else if (token.type === TokenType.DECIMAL) {
                token.obj = -(token.obj as Double)
                return true
            }
            OperatorType.PLUS_PLUS -> if (token.type === TokenType.INTEGER) {
                token.obj = token.obj as Long + 1L
                return true
            } else if (token.type === TokenType.DECIMAL) {
                token.obj = token.obj as Double + 1.0
                return true
            }
            OperatorType.MINUS_MINUS -> if (token.type === TokenType.INTEGER) {
                token.obj = token.obj as Long - 1L
                return true
            } else if (token.type === TokenType.DECIMAL) {
                token.obj = token.obj as Double - 1.0
                return true
            }
            else -> {
            }
        }
        return false
    }

    /**
     * 双目运算
     *
     * @param recorder 错误记录
     * @param exp      表达式
     * @return 运算是否合法
     */
    fun binop(recorder: ISemanticRecorder, exp: ExpBinop): Boolean {
        val leftValue = exp.leftOperand as ExpValue?
        val rightValue = exp.rightOperand as ExpValue?
        val token = exp.token
        val leftToken = leftValue!!.token
        val rightToken = rightValue!!.token
        val type = token.obj as OperatorType
        if (bin(type, leftToken, rightToken)) {
            return true
        }
        recorder.add(SemanticError.INVALID_OPERATOR, token)
        return false
    }

    fun bin(type: OperatorType, lop: Token, rop: Token): Boolean {
        return bin(type, lop, rop, true)
    }

    /**
     * 二元运算（包含向上转换）
     *
     * @param lop  左操作数
     * @param rop  右操作数
     * @param init 操作数默认转型
     * @return 运算是否合法
     */
    private fun bin(type: OperatorType, lop: Token, rop: Token,
                    init: Boolean): Boolean {
        if (type === OperatorType.DOT) {
            mapConverter[TokenType.STRING]!!.convert(rop)
            return true
        }
        if (init) {
            when (type) {
                OperatorType.LOGICAL_AND, OperatorType.LOGICAL_OR -> {
                    val bool = mapConverter[TokenType.BOOL]!!
                    bool.convert(lop)
                    bool.convert(rop)
                }
                OperatorType.BIT_AND, OperatorType.BIT_OR, OperatorType.BIT_XOR -> {
                    val integer = mapConverter[TokenType.INTEGER]!!
                    integer.convert(lop)
                    integer.convert(rop)
                }
                OperatorType.LEFT_SHIFT, OperatorType.RIGHT_SHIFT -> {
                    val integer = mapConverter[TokenType.INTEGER]!!
                    integer.convert(rop)
                }
                else -> {
                }
            }
        }
        if (lop.type === rop.type) {// 操作数类型相同
            when (lop.type) {
                TokenType.BOOL -> {
                    val lbo = lop.obj as Boolean
                    val rbo = rop.obj as Boolean
                    when (type) {
                        OperatorType.PLUS, OperatorType.LOGICAL_OR -> lop.obj = lbo || rbo
                        OperatorType.MINUS, OperatorType.DIVIDE -> lop.obj = lbo && !rbo
                        OperatorType.TIMES, OperatorType.LOGICAL_AND -> lop.obj = lbo && rbo
                        else -> return false
                    }
                    return true
                }
                TokenType.CHARACTER -> {
                    val lch = (lop.obj as Char).toInt()
                    val rch = (rop.obj as Char).toInt()
                    when (type) {
                        OperatorType.PLUS -> lop.obj = (lch + rch).toChar()
                        OperatorType.MINUS -> lop.obj = (lch - rch).toChar()
                        OperatorType.TIMES -> lop.obj = (lch * rch).toChar()
                        OperatorType.DIVIDE -> lop.obj = (lch / rch).toChar()
                        OperatorType.MOD -> lop.obj = (lch % rch).toChar()
                        OperatorType.LESS_THAN -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lch < rch
                        }
                        OperatorType.LESS_THAN_OR_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lch <= rch
                        }
                        OperatorType.GREATER_THAN -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lch > rch
                        }
                        OperatorType.GREATER_THAN_OR_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lch >= rch
                        }
                        OperatorType.EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lch == rch
                        }
                        OperatorType.NOT_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lch != rch
                        }
                        else -> return false
                    }
                    return true
                }
                TokenType.STRING -> {
                    val lstr = lop.obj as String
                    val rstr = rop.obj as String
                    when (type) {
                        OperatorType.PLUS -> lop.obj = lstr + rstr
                        OperatorType.EQUAL -> {
                            lop.obj = lstr == rstr
                            lop.type = TokenType.BOOL
                        }
                        OperatorType.NOT_EQUAL -> {
                            lop.obj = lstr != rstr
                            lop.type = TokenType.BOOL
                        }
                        else -> return false
                    }
                    return true
                }
                TokenType.DECIMAL -> {
                    val ldec = lop.obj as Double
                    val rdec = rop.obj as Double
                    when (type) {
                        OperatorType.PLUS -> lop.obj = ldec + rdec
                        OperatorType.MINUS -> lop.obj = ldec - rdec
                        OperatorType.TIMES -> lop.obj = ldec * rdec
                        OperatorType.DIVIDE -> lop.obj = ldec / rdec
                        OperatorType.LESS_THAN -> {
                            lop.type = TokenType.BOOL
                            lop.obj = ldec < rdec
                        }
                        OperatorType.LESS_THAN_OR_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = ldec <= rdec
                        }
                        OperatorType.GREATER_THAN -> {
                            lop.type = TokenType.BOOL
                            lop.obj = ldec > rdec
                        }
                        OperatorType.GREATER_THAN_OR_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = ldec >= rdec
                        }
                        OperatorType.EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = ldec.compareTo(rdec) == 0
                        }
                        OperatorType.NOT_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = ldec.compareTo(rdec) != 0
                        }
                        else -> return false
                    }
                    return true
                }
                TokenType.INTEGER -> {
                    val lint = lop.obj as Long
                    val rint = rop.obj as Long
                    when (type) {
                        OperatorType.PLUS -> lop.obj = lint + rint
                        OperatorType.MINUS -> lop.obj = lint - rint
                        OperatorType.TIMES -> lop.obj = lint * rint
                        OperatorType.DIVIDE -> lop.obj = lint / rint
                        OperatorType.MOD -> lop.obj = lint % rint
                        OperatorType.LEFT_SHIFT -> lop.obj = lint shl rint.toInt()
                        OperatorType.RIGHT_SHIFT -> lop.obj = lint shr rint.toInt()
                        OperatorType.BIT_AND -> lop.obj = lint and rint
                        OperatorType.BIT_OR -> lop.obj = lint or rint
                        OperatorType.BIT_XOR -> lop.obj = lint xor rint
                        OperatorType.LESS_THAN -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lint < rint
                        }
                        OperatorType.LESS_THAN_OR_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lint <= rint
                        }
                        OperatorType.GREATER_THAN -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lint > rint
                        }
                        OperatorType.GREATER_THAN_OR_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lint >= rint
                        }
                        OperatorType.EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lint.compareTo(rint) == 0
                        }
                        OperatorType.NOT_EQUAL -> {
                            lop.type = TokenType.BOOL
                            lop.obj = lint.compareTo(rint) != 0
                        }
                        else -> return false
                    }
                    return true
                }
                else -> {
                }
            }
        } else {// 操作数类型不同，需要提升
            if (promote(lop, rop)) {
                return bin(type, lop, rop, false)
            }
        }
        return false
    }

    /**
     * 三目运算（当前只有一种形式）
     *
     * @param recorder 错误记录
     * @param exp      表达式
     * @return 运算是否合法
     */
    fun triop(recorder: ISemanticRecorder, exp: ExpTriop): Int {
        val firstValue = exp.firstOperand as ExpValue?
        val firstToken = exp.firstToken!!
        val secondToken = exp.secondToken!!
        val branch = tri(firstToken, secondToken, firstValue!!.token)
        if (branch != 0) {
            return branch
        }
        recorder.add(SemanticError.INVALID_OPERATOR, firstToken)
        return 0
    }

    /**
     * 三目运算（当前只有一种形式）
     *
     * @param op1   操作符1
     * @param op2   操作符2
     * @param token 操作数
     * @return 运算是否合法
     */
    private fun tri(op1: Token, op2: Token?, token: Token): Int {
        return if (op1.obj === OperatorType.QUERY && op2!!.obj === OperatorType.COLON) {
            tri(token)
        } else 0
    }

    /**
     * 三目运算（当前只有一种形式）
     *
     * @param token 操作数
     * @return 运算是否合法
     */
    private fun tri(token: Token): Int {
        val bool = mapConverter[TokenType.BOOL]!!.convert(token)
        return if (bool.type === TokenType.BOOL) {
            if (bool.obj as Boolean) 1 else 2
        } else 0
    }

    /**
     * 操作数提升（即向上转换），提升主要看左操作数（这样即隐含类型转换）
     *
     * @param lop 左操作数
     * @param rop 右操作数
     * @return 运算是否合法
     */
    private fun promote(lop: Token, rop: Token): Boolean {
        return promote(lop.type, rop)
    }

    /**
     * 操作数提升（即向上转换），提升主要看左操作数（这样即隐含类型转换）
     *
     * @param type 左操作数类型
     * @param rop  右操作数
     * @return 运算是否合法
     */
    fun promote(type: TokenType, rop: Token): Boolean {
        val conventer = mapConverter[type]
        if (conventer != null) {
            val token = mapConverter[type]!!.convert(rop)
            return token.type === type
        } else {
            return false
        }
    }

    fun op2ins(token: Token): RuntimeInst {
        if (token.type === TokenType.OPERATOR) {
            val inst = mapOp2Ins[token.obj]
            if (inst != null)
                return inst
        }
        return RuntimeInst.inop
    }

    fun ins2op(inst: RuntimeInst): OperatorType? {
        return mapIns2Op[inst]
    }

    fun isExternalName(token: Token): Boolean {
        if (token.type === TokenType.ID) {
            val name = token.toRealString()
            return isExternalName(name)
        }
        return false
    }

    fun isExternalName(name: String): Boolean {
        return name.startsWith("g_")
    }

    fun isAssignment(op: OperatorType): Boolean {
        when (op) {
            OperatorType.PLUS_ASSIGN, OperatorType.MINUS_ASSIGN, OperatorType.TIMES_ASSIGN, OperatorType.DIV_ASSIGN, OperatorType.AND_ASSIGN, OperatorType.OR_ASSIGN, OperatorType.XOR_ASSIGN, OperatorType.MOD_ASSIGN, OperatorType.EQ_ASSIGN -> return true
            else -> {
            }
        }
        return false
    }
}
