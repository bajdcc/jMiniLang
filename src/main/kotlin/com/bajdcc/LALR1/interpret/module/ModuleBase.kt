package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError
import com.bajdcc.util.ResourceLoader
import org.apache.log4j.Logger
import java.io.File
import java.util.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】基本模块
 *
 * @author bajdcc
 */
class ModuleBase : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.base"

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
            info.addExternalValue("g_null", RuntimeDebugValue { RuntimeObject(null) })
            info.addExternalValue("g_true", RuntimeDebugValue { RuntimeObject(true) })
            info.addExternalValue("g_false", RuntimeDebugValue { RuntimeObject(false) })
            val newline = System.lineSeparator()
            info.addExternalValue("g_endl", RuntimeDebugValue { RuntimeObject(newline) })
            info.addExternalValue("g_nullptr", RuntimeDebugValue { RuntimeObject(-1) })
            info.addExternalFunc("g_is_null",
                    RuntimeDebugExec("判断是否为空", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, _: IRuntimeStatus -> RuntimeObject(args[0].obj == null) })
            info.addExternalFunc("g_is_valid_handle",
                    RuntimeDebugExec("判断句柄合法", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].int >= 0) })
            info.addExternalFunc("g_set_flag",
                    RuntimeDebugExec("设置对象属性", arrayOf(RuntimeObjectType.kObject, RuntimeObjectType.kInt))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val flag = args[1].long
                            args[0].setFlag(flag)
                            args[0]
                        }
                    })
            info.addExternalFunc("g_get_flag",
                    RuntimeDebugExec("获取对象属性", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].getFlag()) })
            info.addExternalFunc("g_is_flag",
                    RuntimeDebugExec("判断对象属性", arrayOf(RuntimeObjectType.kObject, RuntimeObjectType.kInt))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val flag = args[1].long
                            RuntimeObject(args[0].getFlag() == flag)
                        }
                    })
            info.addExternalFunc("g_set_debug",
                    RuntimeDebugExec("输出调试信息", arrayOf(RuntimeObjectType.kBool))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val debug = args[0].bool
                            status.service.processService.setDebug(status.pid, debug)
                            null
                        }
                    })
            info.addExternalFunc("g_set_rapid",
                    RuntimeDebugExec("设置高速模式", arrayOf(RuntimeObjectType.kBool))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val mode = args[0].bool
                            status.service.processService.setHighSpeed(mode)
                            null
                        }
                    })
            info.addExternalFunc("g_not_null",
                    RuntimeDebugExec("判断是否有效", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].obj != null) })
            info.addExternalFunc("g_print",
                    RuntimeDebugExec("标准输出", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            logger.info(args[0].obj)
                            null
                        }
                    })
            info.addExternalFunc("g_print_info",
                    RuntimeDebugExec("标准输出", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            logger.info(args[0])
                            null
                        }
                    })
            info.addExternalFunc("g_printn",
                    RuntimeDebugExec("标准输出并换行", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val inf = status.procInfo
                            logger.info(String.format("#%03d [%s] %s", status.pid, inf[3], args[0].obj))
                            null
                        }
                    })
            info.addExternalFunc("g_printdn",
                    RuntimeDebugExec("调试输出并换行", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val inf = status.procInfo
                            logger.debug(String.format("#%03d [%s] %s", status.pid, inf[3], args[0].obj))
                            null
                        }
                    })
            info.addExternalFunc("g_print_err",
                    RuntimeDebugExec("错误输出", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            logger.error(args[0].obj)
                            null
                        }
                    })
            info.addExternalFunc("g_to_string",
                    RuntimeDebugExec("将对象转换成字符串", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].obj.toString()) })
            info.addExternalFunc("g_new",
                    RuntimeDebugExec("深拷贝", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> args[0].clone() })
            info.addExternalFunc("g_doc",
                    RuntimeDebugExec("文档", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.getHelpString(args[0].string)) })
            info.addExternalFunc("g_get_type",
                    RuntimeDebugExec("获取类型", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].typeName) })
            info.addExternalFunc("g_get_type_ordinal",
                    RuntimeDebugExec("获取类型(索引)", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].typeIndex) })
            info.addExternalFunc("g_type",
                    RuntimeDebugExec("获取类型", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].typeString) })
            info.addExternalFunc("g_hash",
                    RuntimeDebugExec("获取哈希", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> if (args[0].obj == null) RuntimeObject("NULL") else RuntimeObject(args[0].obj!!.hashCode().toString()) })
            info.addExternalFunc("g_exit",
                    RuntimeDebugExec("程序退出")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> throw RuntimeException(RuntimeError.EXIT, -1, "用户自行退出"); })
            info.addExternalFunc("g_load",
                    RuntimeDebugExec("载入并运行程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.runProcess(args[0].string).toLong()) })
            info.addExternalFunc("g_load_x",
                    RuntimeDebugExec("载入并运行程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.runProcessX(args[0].string).toLong()) })
            info.addExternalFunc("g_load_user",
                    RuntimeDebugExec("载入并运行用户态程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.runUsrProcess(args[0].string).toLong()) })
            info.addExternalFunc("g_load_user_x",
                    RuntimeDebugExec("载入并运行用户态程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.runUsrProcessX(args[0].string).toLong()) })
            info.addExternalFunc("g_print_file",
                    RuntimeDebugExec("载入并运行程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            try {
                                File(args[0].string).readLines().forEach { println(it) }
                            } catch (e: Exception) {
                                System.err.println(e.message)
                            }
                            null
                        }
                    })
            info.addExternalFunc("g_args_count",
                    RuntimeDebugExec("取得函数参数数量")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.funcArgsCount.toLong()) })
            info.addExternalFunc("g_args_index",
                    RuntimeDebugExec("取得函数参数", arrayOf(RuntimeObjectType.kInt))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val index = args[0].long
                            status.getFuncArgs(index.toInt())
                        }
                    })
            info.addExternalFunc("g_get_timestamp",
                    RuntimeDebugExec("获取当前时间戳")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(System.currentTimeMillis()) })
            info.addExternalFunc("g_doc_grammar",
                    RuntimeDebugExec("语法参考")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Grammar.npaDesc) })
            buildIORead(info)

            runtimeCodePage = page
            return page
        }

    private fun buildIORead(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_stdin_read", RuntimeDebugExec("标准输入读取字符") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(System.`in`.read().toChar())
        })
        info.addExternalFunc("g_stdin_read_int", RuntimeDebugExec("标准输入读取整数") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.nextLong())
        })
        info.addExternalFunc("g_stdin_read_real", RuntimeDebugExec("标准输入读取实数") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.nextDouble())
        })
        info.addExternalFunc("g_stdin_read_bool", RuntimeDebugExec("标准输入读取布尔值") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.nextBoolean())
        })
        info.addExternalFunc("g_stdin_read_line", RuntimeDebugExec("标准输入读取行") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.nextLine())
        })
        info.addExternalFunc("g_stdin_read_has_int", RuntimeDebugExec("标准输入是否匹配整数") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.hasNextLong())
        })
        info.addExternalFunc("g_stdin_read_has_real", RuntimeDebugExec("标准输入是否匹配实数") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.hasNextDouble())
        })
        info.addExternalFunc("g_stdin_read_has_bool", RuntimeDebugExec("标准输入是否匹配布尔值") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.hasNextBoolean())
        })
        info.addExternalFunc("g_stdin_read_has_line", RuntimeDebugExec("标准输入是否匹配行") { args: List<RuntimeObject>, status: IRuntimeStatus ->
            RuntimeObject(scanner.hasNextLine())
        })
    }

    companion object {

        val instance = ModuleBase()
        private val logger = Logger.getLogger("console")
        private val scanner = Scanner(System.`in`)
    }
}