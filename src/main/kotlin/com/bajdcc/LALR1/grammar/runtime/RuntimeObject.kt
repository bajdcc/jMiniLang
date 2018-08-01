package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【运行时】运行时对象
 *
 * @author bajdcc
 */
class RuntimeObject : Cloneable {

    data class FlagObject(var flag: Long = 0)

    var obj: Any? = null
    var symbol: Any? = null
    var type = RuntimeObjectType.kNull
    private var flag: FlagObject = FlagObject()

    val int: Int
        get() = if (type == RuntimeObjectType.kInt) (obj as Long).toInt() else obj as Int

    val long: Long
        get() = if (type == RuntimeObjectType.kPtr) (obj as Int).toLong() else obj as Long

    val double: Double
        get() = obj as Double

    val array: RuntimeArray
        get() = obj as RuntimeArray

    val map: RuntimeMap
        get() = obj as RuntimeMap

    val string: String
        get() = obj as String

    val typeName: String?
        get() = fromObject(obj).desc

    val bool: Boolean
        get() = obj as Boolean

    val typeString: String
        get() = fromObject(obj).toString()

    val char: Char
        get() = obj as Char

    val typeIndex: Long
        get() = fromObject(obj).ordinal.toLong()

    constructor(obj: Any?) {
        this.obj = obj
        calcTypeFromObject()
    }

    constructor(obj: Any?, type: RuntimeObjectType) {
        this.obj = obj
        this.type = type
    }

    constructor(obj: RuntimeObject?) {
        copyFrom(obj)
    }

    private fun calcTypeFromObject() {
        type = fromObject(obj)
    }

    internal fun copyFrom(obj: RuntimeObject?) {
        if (obj != null) {
            this.obj = obj.obj // 注意：这里是浅拷贝！
            this.type = obj.type
            this.symbol = obj.symbol
            this.flag = obj.flag
        } else {
            calcTypeFromObject()
        }
    }

    fun getFlag(): Long {
        return flag.flag
    }

    fun setFlag(flag: Long) {
        this.flag.flag = flag
    }

    public override fun clone(): RuntimeObject {
        if (obj == null) {
            return RuntimeObject(null)
        }
        val o = super.clone() as RuntimeObject
        if (obj is RuntimeArray) {
            return RuntimeObject(RuntimeArray(obj as RuntimeArray)) // 引用类型要创建
        }
        return if (obj is RuntimeMap) {
            RuntimeObject(RuntimeMap(obj as RuntimeMap)) // 引用类型要创建
        } else o
    }

    override fun equals(other: Any?): Boolean {
        if (other is RuntimeObject) {
            val o = other as RuntimeObject?
            if (this.obj == null)
                return o!!.obj == null && this.type == o.type && this.flag == o.flag
            return if (this.type == o!!.type && this.flag == o.flag) {
                this.obj == o.obj
            } else false
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return if (obj == null) super.hashCode() else obj!!.hashCode()
    }

    override fun toString(): String {
        return " " + symbol.toString() + " " + type.desc +
                ("(" + obj.toString() + ")") +
                if (flag.flag == 0L) "" else "#${flag.flag}"
    }

    companion object {

        internal fun createObject(obj: RuntimeObject): RuntimeObject {
            return RuntimeObject(obj)
        }

        fun fromObject(obj: Any?): RuntimeObjectType {
            if (obj == null) {
                return RuntimeObjectType.kNull
            }
            if (obj is String) {
                return RuntimeObjectType.kString
            }
            if (obj is Char) {
                return RuntimeObjectType.kChar
            }
            if (obj is Long) {
                return RuntimeObjectType.kInt
            }
            if (obj is Double) {
                return RuntimeObjectType.kReal
            }
            if (obj is Boolean) {
                return RuntimeObjectType.kBool
            }
            if (obj is Int) {
                return RuntimeObjectType.kPtr
            }
            if (obj is RuntimeFuncObject) {
                return RuntimeObjectType.kFunc
            }
            if (obj is RuntimeArray) {
                return RuntimeObjectType.kArray
            }
            return if (obj is RuntimeMap) {
                RuntimeObjectType.kMap
            } else RuntimeObjectType.kObject
        }

        internal fun toTokenType(obj: RuntimeObjectType): TokenType {
            return when (obj) {
                RuntimeObjectType.kBool -> TokenType.BOOL
                RuntimeObjectType.kChar -> TokenType.CHARACTER
                RuntimeObjectType.kInt -> TokenType.INTEGER
                RuntimeObjectType.kReal -> TokenType.DECIMAL
                RuntimeObjectType.kString -> TokenType.STRING
                RuntimeObjectType.kPtr -> TokenType.POINTER
                else -> TokenType.ERROR
            }
        }
    }
}
