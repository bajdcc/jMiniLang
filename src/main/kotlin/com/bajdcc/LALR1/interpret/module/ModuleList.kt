package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.util.ResourceLoader

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】列表模块
 *
 * @author bajdcc
 */
class ModuleList : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.list"

    override val moduleCode: String
        get() = ResourceLoader.load(javaClass)

    override val codePage: RuntimeCodePage
        @Throws(Exception::class)
        get() {
            if (runtimeCodePage != null)
                runtimeCodePage!!

            val base = ResourceLoader.load(javaClass)

            val grammar = Grammar(base)
            val page = grammar.codePage
            val info = page.info
            info.addExternalValue("g_new_array", RuntimeDebugValue { RuntimeObject(RuntimeArray()) })
            info.addExternalValue("g_new_map", RuntimeDebugValue { RuntimeObject(RuntimeMap()) })
            buildMethod(info)

            runtimeCodePage = page
            return page
        }

    private fun buildMethod(info: IRuntimeDebugInfo) {
        // 数组
        buildArrayMethod(info)
        // 字典
        buildMapMethod(info)
    }

    /**
     * 数组操作
     *
     * @param info 信息
     */
    private fun buildArrayMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_array_add",
                RuntimeDebugExec("数组添加元素", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        array.add(args[1])
                        args[0]
                    }
                })
        info.addExternalFunc("g_array_contains",
                RuntimeDebugExec("数组查找元素", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        RuntimeObject(array.contains(args[1]))
                    }
                })
        info.addExternalFunc("g_array_append",
                RuntimeDebugExec("数组添加数组", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val array2 = args[1].array
                        array.add(array2)
                        args[0]
                    }
                })
        info.addExternalFunc("g_array_insert",
                RuntimeDebugExec("数组添加元素", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kInt, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val n = args[1].int
                        array.insert(n, args[2])
                        args[0]
                    }
                })
        info.addExternalFunc("g_array_set",
                RuntimeDebugExec("数组设置元素", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kInt, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val index = args[1].int
                        if (!array.set(index, args[2])) {
                            status.err(RuntimeException.RuntimeError.INVALID_INDEX, "array.set")
                        }
                        null
                    }
                })
        info.addExternalFunc("g_array_pop",
                RuntimeDebugExec("数组弹出元素", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        RuntimeObject(array.pop())
                    }
                })
        info.addExternalFunc("g_array_clear",
                RuntimeDebugExec("数组清除元素", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        array.clear()
                        RuntimeObject(array)
                    }
                })
        info.addExternalFunc("g_array_reverse",
                RuntimeDebugExec("数组反转", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        array.reverse()
                        RuntimeObject(array)
                    }
                })
        info.addExternalFunc("g_array_get",
                RuntimeDebugExec("数组查询", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val index = args[1].int
                        RuntimeObject(array[index])
                    }
                })
        info.addExternalFunc("g_array_get_ex",
                RuntimeDebugExec("数组查询", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val index = args[1].int
                        RuntimeObject(array[index, status])
                    }
                })
        info.addExternalFunc("g_array_size",
                RuntimeDebugExec("数组长度", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        array.size()
                    }
                })
        info.addExternalFunc("g_array_remove",
                RuntimeDebugExec("数组移除", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val index = args[1].int
                        RuntimeObject(array.remove(index))
                    }
                })
        info.addExternalFunc("g_array_delete",
                RuntimeDebugExec("数组移除", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val obj = args[1]
                        RuntimeObject(array.delete(obj))
                    }
                })
        info.addExternalFunc("g_array_empty",
                RuntimeDebugExec("数组为空", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        RuntimeObject(array.isEmpty)
                    }
                })
        info.addExternalFunc("g_array_range",
                RuntimeDebugExec("产生连续整数数组", arrayOf(RuntimeObjectType.kInt, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = RuntimeArray()
                        val a = args[0].long
                        val b = args[1].long
                        for (i in a..b) {
                            array.add(RuntimeObject(i))
                        }
                        RuntimeObject(array)
                    }
                })
        info.addExternalFunc("g_array_fill",
                RuntimeDebugExec("填充数组", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val obj = args[1]
                        for (i in 0..array.length()) {
                            array[i] = obj
                        }
                        RuntimeObject(array)
                    }
                })
        info.addExternalFunc("g_array_distinct",
                RuntimeDebugExec("数组去重", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        RuntimeObject(array.distinct())
                    }
                })
    }

    /**
     * 字典操作
     *
     * @param info 信息
     */
    private fun buildMapMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_map_put",
                RuntimeDebugExec("字典设置元素", arrayOf(RuntimeObjectType.kMap, RuntimeObjectType.kString, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        val key = args[1].string
                        map.put(key, args[2])
                        null
                    }
                })
        info.addExternalFunc("g_map_contains",
                RuntimeDebugExec("数组查找键", arrayOf(RuntimeObjectType.kMap, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        val key = args[1].string
                        RuntimeObject(map.contains(key))
                    }
                })
        info.addExternalFunc("g_map_get",
                RuntimeDebugExec("字典查询", arrayOf(RuntimeObjectType.kMap, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        val key = args[1].string
                        RuntimeObject(map[key])
                    }
                })
        info.addExternalFunc("g_map_size",
                RuntimeDebugExec("字典长度", arrayOf(RuntimeObjectType.kMap))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        map.size()
                    }
                })
        info.addExternalFunc("g_map_remove",
                RuntimeDebugExec("字典移除", arrayOf(RuntimeObjectType.kMap, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        val key = args[1].string
                        RuntimeObject(map.remove(key))
                    }
                })
        info.addExternalFunc("g_map_clear",
                RuntimeDebugExec("字典清除元素", arrayOf(RuntimeObjectType.kMap))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        map.clear()
                        RuntimeObject(map)
                    }
                })
        info.addExternalFunc("g_map_empty",
                RuntimeDebugExec("字典为空", arrayOf(RuntimeObjectType.kMap))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        RuntimeObject(map.isEmpty)
                    }
                })
        info.addExternalFunc("g_map_keys",
                RuntimeDebugExec("字典键", arrayOf(RuntimeObjectType.kMap))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        RuntimeObject(map.keys)
                    }
                })
        info.addExternalFunc("g_map_values",
                RuntimeDebugExec("字典值", arrayOf(RuntimeObjectType.kMap))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val map = args[0].map
                        RuntimeObject(map.values)
                    }
                })
    }

    companion object {

        val instance = ModuleList()
    }
}
