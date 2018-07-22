package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.util.lexer.token.Token

/**
 * 【运行时】运行时辅助类
 *
 * @author bajdcc
 */
object RuntimeTools {

    @Throws(RuntimeException::class)
    fun calcOp(inst: RuntimeInst, stk: IRuntimeStack): Boolean {
        when (inst) {
            RuntimeInst.inot, RuntimeInst.iinv, RuntimeInst.iinc, RuntimeInst.idec -> {
                val obj = stk.load()
                if (obj.type == RuntimeObjectType.kNan) {
                    stk.store(obj)
                    return true
                }
                val tk = Token.createFromObject(obj.obj!!)
                if (TokenTools.sin(TokenTools.ins2op(inst), tk)) {
                    stk.store(RuntimeObject(tk.obj))
                } else {
                    return false
                }
            }
            RuntimeInst.icl, RuntimeInst.icg, RuntimeInst.icle, RuntimeInst.icge, RuntimeInst.ice, RuntimeInst.icne, RuntimeInst.iadd, RuntimeInst.iand, RuntimeInst.iandl, RuntimeInst.idiv, RuntimeInst.imod, RuntimeInst.imul, RuntimeInst.ior, RuntimeInst.iorl, RuntimeInst.ishl, RuntimeInst.ishr, RuntimeInst.isub, RuntimeInst.ixor -> {
                val obj2 = stk.load()
                val obj = stk.load()
                if (obj.type == RuntimeObjectType.kNan) {
                    stk.store(obj)
                    return true
                }
                if (obj2.type == RuntimeObjectType.kNan) {
                    stk.store(obj2)
                    return true
                }
                val tk = Token.createFromObject(obj.obj!!)
                if (TokenTools.bin(TokenTools.ins2op(inst), tk,
                                Token.createFromObject(obj2.obj!!))) {
                    stk.store(RuntimeObject(tk.obj))
                } else {
                    return false
                }
            }
            else -> {
            }
        }
        return true
    }

    @Throws(RuntimeException::class)
    fun calcJump(inst: RuntimeInst, stk: IRuntimeStack): Boolean {
        when (inst) {
            RuntimeInst.ijmp -> stk.opJump()
            RuntimeInst.ijt -> stk.opJumpBool(true)
            RuntimeInst.ijf -> stk.opJumpBool(false)
            RuntimeInst.ijtx -> stk.opJumpBoolRetain(true)
            RuntimeInst.ijfx -> stk.opJumpBoolRetain(false)
            RuntimeInst.ijz -> stk.opJumpZero(true)
            RuntimeInst.ijnz -> stk.opJumpZero(false)
            RuntimeInst.ijyld -> stk.opJumpYield()
            RuntimeInst.ijnan -> stk.opJumpNan()
            else -> return false
        }
        return true
    }

    @Throws(Exception::class)
    fun calcData(inst: RuntimeInst, stk: IRuntimeStack): Boolean {
        when (inst) {
            RuntimeInst.ialloc -> stk.opStoreDirect()
            RuntimeInst.icall -> stk.opCall()
            RuntimeInst.ildfun -> stk.opLoadFunc()
            RuntimeInst.irefun -> stk.opReloadFunc()
            RuntimeInst.iload -> stk.opLoad()
            RuntimeInst.iloada -> stk.opLoadArgs()
            RuntimeInst.iloadv -> stk.opLoadVar()
            RuntimeInst.inop -> {
            }
            RuntimeInst.iopena -> stk.opOpenFunc()
            RuntimeInst.ipop -> stk.pop()
            RuntimeInst.ipush -> stk.push()
            RuntimeInst.ipusha -> stk.opPushArgs()
            RuntimeInst.ipushx -> stk.opPushNull()
            RuntimeInst.ipushz -> stk.opPushZero()
            RuntimeInst.ipushn -> stk.opPushNan()
            RuntimeInst.iret -> stk.opReturn()
            RuntimeInst.istore -> stk.opStore()
            RuntimeInst.icopy -> stk.opStoreCopy()
            RuntimeInst.iimp -> stk.opImport()
            RuntimeInst.iloadx -> stk.opLoadExtern()
            RuntimeInst.icallx -> stk.opCallExtern(false)
            RuntimeInst.ically -> stk.opCallExtern(true)
            RuntimeInst.iyldi -> stk.opYield(true)
            RuntimeInst.iyldo -> stk.opYield(false)
            RuntimeInst.iyldl -> stk.opYieldSwitch(false)
            RuntimeInst.iyldr -> stk.opYieldSwitch(true)
            RuntimeInst.iyldx -> stk.opYieldDestroyContext()
            RuntimeInst.iyldy -> stk.opYieldCreateContext()
            RuntimeInst.iscpi -> stk.opScope(true)
            RuntimeInst.iscpo -> stk.opScope(false)
            RuntimeInst.iarr -> stk.opArr()
            RuntimeInst.imap -> stk.opMap()
            RuntimeInst.iidx -> stk.opIndex()
            RuntimeInst.iidxa -> stk.opIndexAssign()
            RuntimeInst.itry -> stk.opTry()
            RuntimeInst.ithrow -> stk.opThrow()
            else -> return false
        }
        return true
    }

    fun getYieldHash(stackLevel: Int, funcLevel: Int,
                     pageName: String, line: Int): String {
        return String.format("%d#%d#%s#%d", stackLevel, funcLevel, pageName,
                line)
    }
}
