package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.util.ResourceLoader
import java.util.regex.Pattern

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】字符串模块
 *
 * @author bajdcc
 */
class ModuleString : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.string"

    override val moduleCode: String
        get() = ResourceLoader.load(javaClass)

    override val codePage: RuntimeCodePage
        @Throws(Exception::class)
        get() {
            if (runtimeCodePage != null)
                return runtimeCodePage!!

            val base = ResourceLoader.load(javaClass)

            val grammar = Grammar(base)
            val page = grammar.codePage
            val info = page.info
            buildStringUtils(info)

            runtimeCodePage = page
            return page
        }

    private fun buildStringUtils(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_string_replace",
                RuntimeDebugExec("字符串替换", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val pat = args[1].string
                        val sub = args[2].string
                        RuntimeObject(str.replace(pat.toRegex(), sub))
                    }
                })
        info.addExternalFunc("g_string_split",
                RuntimeDebugExec("字符串分割", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val split = args[1].string
                        RuntimeObject(RuntimeArray(str.split(split.toRegex(), Integer.MAX_VALUE).toTypedArray().map { RuntimeObject(it) }))
                    }
                })
        info.addExternalFunc("g_string_splitn",
                RuntimeDebugExec("字符串分割", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val split = args[1].string
                        val n = args[1].long.toInt()
                        RuntimeObject(RuntimeArray(str.split(split.toRegex(), n.coerceAtLeast(0)).toTypedArray().map { RuntimeObject(it) }))
                    }
                })
        info.addExternalFunc("g_string_trim",
                RuntimeDebugExec("字符串去除头尾空白", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        RuntimeObject(str.trim { it <= ' ' })
                    }
                })
        info.addExternalFunc("g_string_length",
                RuntimeDebugExec("字符串长度", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        RuntimeObject(str.length.toLong())
                    }
                })
        info.addExternalFunc("g_string_empty",
                RuntimeDebugExec("字符串是否为空", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].string.isEmpty()) })
        info.addExternalFunc("g_string_get",
                RuntimeDebugExec("字符串查询", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val index = args[1].long
                        RuntimeObject(str[index.toInt()])
                    }
                })
        info.addExternalFunc("g_string_regex",
                RuntimeDebugExec("字符串正则匹配", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val regex = args[1].string
                        val m = Pattern.compile(regex).matcher(str)
                        val arr = RuntimeArray()
                        if (m.find()) {
                            for (i in 0..m.groupCount()) {
                                arr.add(RuntimeObject(m.group(i)))
                            }
                        }
                        RuntimeObject(arr)
                    }
                })
        info.addExternalFunc("g_string_build",
                RuntimeDebugExec("从字节数组构造字符串", arrayOf(RuntimeObjectType.kArray))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val array = args[0].array
                        val sb = StringBuilder()
                        for (obj in array.toList()) {
                            sb.append(obj)
                        }
                        RuntimeObject(sb.toString())
                    }
                })
        info.addExternalFunc("g_string_atoi",
                RuntimeDebugExec("字符串转换成数字", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        try {
                            RuntimeObject(java.lang.Long.parseLong(str))
                        } catch (e: NumberFormatException) {
                            RuntimeObject(-1L)
                        }
                    }
                })
        info.addExternalFunc("g_string_atoi_s",
                RuntimeDebugExec("字符串转换成数字（安全版本）", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        try {
                            RuntimeObject(java.lang.Long.parseLong(str))
                        } catch (e: NumberFormatException) {
                            null
                        }
                    }
                })
        info.addExternalFunc("g_string_join_array",
                RuntimeDebugExec("字符串数组连接", arrayOf(RuntimeObjectType.kArray, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val arr = args[0].array
                        val delim = args[1].string
                        RuntimeObject(arr.toStringList().joinToString(delim))
                    }
                })
        info.addExternalFunc("g_string_toupper",
                RuntimeDebugExec("字符串大写", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val delim = args[0].string
                        RuntimeObject(delim.toUpperCase())
                    }
                })
        info.addExternalFunc("g_string_tolower",
                RuntimeDebugExec("字符串小写", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val delim = args[0].string
                        RuntimeObject(delim.toLowerCase())
                    }
                })
        info.addExternalFunc("g_string_rep",
                RuntimeDebugExec("字符串重复构造", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val dup = args[1].long
                        val n = dup.toInt()
                        val sb = StringBuilder()
                        for (i in 0 until n) {
                            sb.append(str)
                        }
                        RuntimeObject(sb.toString())
                    }
                })
        info.addExternalFunc("g_string_to_number",
                RuntimeDebugExec("字符串转换数字", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        try {
                            RuntimeObject(java.lang.Long.parseLong(str))
                        } catch (e1: Exception) {
                            try {
                                RuntimeObject(java.lang.Double.parseDouble(str))
                            } catch (e2: Exception) {
                                null
                            }
                        }
                    }
                })
        info.addExternalFunc("g_string_equal",
                RuntimeDebugExec("字符串相等比较", arrayOf(RuntimeObjectType.kObject, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (args[0].type === RuntimeObjectType.kString) {
                            val str = args[0].string
                            val cmp = args[1].string
                            RuntimeObject(str.compareTo(cmp) == 0)
                        } else RuntimeObject(false)
                    }
                })
        info.addExternalFunc("g_string_not_equal",
                RuntimeDebugExec("字符串不等比较", arrayOf(RuntimeObjectType.kObject, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (args[0].type === RuntimeObjectType.kString) {
                            val str = args[0].string
                            val cmp = args[1].string
                            RuntimeObject(str.compareTo(cmp) != 0)
                        } else RuntimeObject(true)
                    }
                })
        info.addExternalFunc("g_string_start_with",
                RuntimeDebugExec("字符串开头比较", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val cmp = args[1].string
                        RuntimeObject(str.startsWith(cmp))
                    }
                })
        info.addExternalFunc("g_string_end_with",
                RuntimeDebugExec("字符串结尾比较", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val cmp = args[1].string
                        RuntimeObject(str.endsWith(cmp))
                    }
                })
        info.addExternalFunc("g_string_substr",
                RuntimeDebugExec("字符串子串", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kInt, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val a = args[1].long
                        val b = args[2].long
                        RuntimeObject(str.substring(a.toInt(), b.toInt()))
                    }
                })
        info.addExternalFunc("g_string_left",
                RuntimeDebugExec("字符串左子串", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val a = args[1].long
                        RuntimeObject(str.substring(0, a.toInt()))
                    }
                })
        info.addExternalFunc("g_string_right",
                RuntimeDebugExec("字符串右子串", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].string
                        val a = args[1].long
                        RuntimeObject(str.substring(a.toInt()))
                    }
                })
    }

    companion object {

        val instance = ModuleString()
    }
}
