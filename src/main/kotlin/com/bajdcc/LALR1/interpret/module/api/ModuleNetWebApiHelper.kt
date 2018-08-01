package com.bajdcc.LALR1.interpret.module.api

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap

/**
 * 实用方法
 *
 * @author bajdcc
 */
object ModuleNetWebApiHelper {

    fun toJsonObject(obj: RuntimeObject?): Any? {
        if (obj == null)
            return null
        when (obj.type) {
            RuntimeObjectType.kNull, RuntimeObjectType.kFunc, RuntimeObjectType.kNan -> return null
            RuntimeObjectType.kPtr, RuntimeObjectType.kObject, RuntimeObjectType.kChar, RuntimeObjectType.kString, RuntimeObjectType.kBool, RuntimeObjectType.kInt, RuntimeObjectType.kReal -> return obj.obj
            RuntimeObjectType.kArray -> return (obj.obj as RuntimeArray).getArray()
                    .map { toJsonObject(it) }
            RuntimeObjectType.kMap -> return (obj.obj as RuntimeMap).getMap().entries
                    .filter { it.value.type !== RuntimeObjectType.kNull }
                    .associate { it.key to toJsonObject(it.value) }
            else -> {
            }
        }
        return null
    }
}
