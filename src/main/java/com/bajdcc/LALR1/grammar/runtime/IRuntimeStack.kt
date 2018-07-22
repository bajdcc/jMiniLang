package com.bajdcc.LALR1.grammar.runtime

/**
 * 运行时堆栈接口
 *
 * @author bajdcc
 */
interface IRuntimeStack {

    @Throws(RuntimeException::class)
    fun load(): RuntimeObject

    @Throws(RuntimeException::class)
    fun store(obj: RuntimeObject)

    @Throws(RuntimeException::class)
    fun push()

    @Throws(RuntimeException::class)
    fun pop()

    @Throws(RuntimeException::class)
    fun opLoad()

    @Throws(RuntimeException::class)
    fun opLoadFunc()

    @Throws(RuntimeException::class)
    fun opReloadFunc()

    @Throws(RuntimeException::class)
    fun opStore()

    @Throws(RuntimeException::class)
    fun opStoreCopy()

    @Throws(RuntimeException::class)
    fun opStoreDirect()

    @Throws(RuntimeException::class)
    fun opOpenFunc()

    @Throws(RuntimeException::class)
    fun opLoadArgs()

    @Throws(RuntimeException::class)
    fun opPushArgs()

    @Throws(RuntimeException::class)
    fun opReturn()

    @Throws(RuntimeException::class)
    fun opCall()

    @Throws(RuntimeException::class)
    fun opPushNull()

    @Throws(RuntimeException::class)
    fun opPushZero()

    @Throws(RuntimeException::class)
    fun opPushNan()

    @Throws(RuntimeException::class)
    fun opPushPtr(pc: Int)

    @Throws(RuntimeException::class)
    fun opPushObj(obj: RuntimeObject)

    @Throws(RuntimeException::class)
    fun opLoadVar()

    @Throws(RuntimeException::class)
    fun opJump()

    @Throws(RuntimeException::class)
    fun opJumpBool(bool: Boolean)

    @Throws(RuntimeException::class)
    fun opJumpBoolRetain(bool: Boolean)

    @Throws(RuntimeException::class)
    fun opJumpZero(bool: Boolean)

    @Throws(RuntimeException::class)
    fun opJumpYield()

    @Throws(RuntimeException::class)
    fun opJumpNan()

    @Throws(RuntimeException::class)
    fun opImport()

    @Throws(RuntimeException::class)
    fun opLoadExtern()

    @Throws(Exception::class)
    fun opCallExtern(invoke: Boolean)

    @Throws(RuntimeException::class)
    fun opYield(input: Boolean)

    @Throws(RuntimeException::class)
    fun opYieldSwitch(forward: Boolean)

    @Throws(Exception::class)
    fun opYieldCreateContext()

    @Throws(RuntimeException::class)
    fun opYieldDestroyContext()

    @Throws(RuntimeException::class)
    fun opScope(enter: Boolean)

    @Throws(RuntimeException::class)
    fun opArr()

    @Throws(RuntimeException::class)
    fun opMap()

    @Throws(RuntimeException::class)
    fun opIndex()

    @Throws(RuntimeException::class)
    fun opIndexAssign()

    @Throws(RuntimeException::class)
    fun opTry()

    @Throws(RuntimeException::class)
    fun opThrow()
}