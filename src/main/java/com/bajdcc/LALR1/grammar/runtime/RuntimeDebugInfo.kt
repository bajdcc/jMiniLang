package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.LALR1.grammar.codegen.CodegenFuncDoc
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import java.io.Serializable

/**
 * 【扩展】调试与开发
 *
 * @author bajdcc
 */
class RuntimeDebugInfo : IRuntimeDebugInfo, Serializable {
    override val dataMap = mutableMapOf<String, Any>()
    private val exports = mutableMapOf<String, Int>()
    private val func = mutableMapOf<Int, String>()
    private val externalValue = mutableMapOf<String, IRuntimeDebugValue>()
    private val externalExec = mutableMapOf<String, IRuntimeDebugExec>()

    override val externFuncList: List<RuntimeArray>
        get() {
            val array = mutableListOf<RuntimeArray>()
            externalExec.entries.sortedBy { it.key }.forEach { a ->
                val arr = RuntimeArray()
                arr.add(RuntimeObject(a.key))
                arr.add(RuntimeObject(exports.getOrDefault(a.key, -1).toLong()))
                if (a.value is CodegenFuncDoc)
                    arr.add(RuntimeObject((a.value as CodegenFuncDoc).paramsDoc))
                else
                    arr.add(RuntimeObject(argsToString(a.value.argsType)))
                arr.add(RuntimeObject(a.value.doc))
                array.add(arr)
            }
            return array
        }

    fun addExports(name: String, addr: Int) {
        exports[name] = addr
    }

    fun addFunc(name: String, addr: Int) {
        func[addr] = name
    }

    override fun getFuncNameByAddress(addr: Int): String {
        return func.getOrDefault(addr, "unknown")
    }

    override fun getAddressOfExportFunc(name: String): Int {
        return exports.getOrDefault(name, -1)
    }

    override fun getValueCallByName(name: String): IRuntimeDebugValue? {
        return externalValue.getOrDefault(name, null)
    }

    override fun getExecCallByName(name: String): IRuntimeDebugExec? {
        return externalExec.getOrDefault(name, null)
    }

    override fun addExternalValue(name: String, func: IRuntimeDebugValue): Boolean {
        return externalValue.put(name, func) != null
    }

    override fun addExternalFunc(name: String, func: IRuntimeDebugExec): Boolean {
        return externalExec.put(name, func) != null
    }

    companion object {

        private const val serialVersionUID = 1L

        private fun argsToString(args: Array<RuntimeObjectType>?): String {
            return args?.joinToString(separator = ", ", transform = { it.desc }) ?: "无"
        }
    }
}
