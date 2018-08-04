package com.bajdcc.LALR1.interpret.module.user

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.RuntimeMachine.Ring3Option.LOG_FILE
import com.bajdcc.LALR1.grammar.runtime.RuntimeMachine.Ring3Option.LOG_PIPE
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.LALR1.interpret.module.*
import com.bajdcc.util.ResourceLoader
import org.apache.log4j.Logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets.UTF_8

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】用户态-基类
 *
 * @author bajdcc
 */
class ModuleUserBase : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "user.base"

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

            buildHandle(info)
            importFromBase(info, ModuleBase.instance.codePage.info)
            importFromString(info, ModuleString.instance.codePage.info)
            importFromTask(info, ModuleTask.instance.codePage.info)
            importFromList(info, ModuleList.instance.codePage.info)
            importFromProc(info, ModuleProc.instance.codePage.info)
            importFromNet(info, ModuleNet.instance.codePage.info)
            importFromFile(info, ModuleFile.instance.codePage.info)
            importFromMath(info, ModuleMath.instance.codePage.info)

            runtimeCodePage = page
            return page
        }

    companion object {

        val instance = ModuleUserBase()
        private val logger = Logger.getLogger("user")
        private val globalContext = RuntimeObject(RuntimeMap())

        const val EXEC_PREFIX = "WEB_EXEC#"
        private const val EXEC_PATH_PREFIX = "/web/exec/"

        private fun buildHandle(info: IRuntimeDebugInfo) {
            info.addExternalFunc("g_handle",
                    RuntimeDebugExec("创建用户服务句柄", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val name = args[0].obj.toString()
                            RuntimeObject(status.service.userService.create(name, status.page))
                        }
                    })
            info.addExternalFunc("g_destroy_handle",
                    RuntimeDebugExec("销毁用户服务句柄", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val id = args[0].int
                            RuntimeObject(status.service.userService.destroy(id))
                        }
                    })
            info.addExternalFunc("g_is_noop",
                    RuntimeDebugExec("是否是空闲值（用于句柄返回值）", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].type === RuntimeObjectType.kNoop) })
            info.addExternalFunc("g_from_noop",
                    RuntimeDebugExec("将空闲值转成正常值", arrayOf(RuntimeObjectType.kNoop))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(args[0].obj) })
            info.addExternalFunc("g_read_pipe",
                    RuntimeDebugExec("读取管道", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> status.service.userService.pipe.read(args[0].int) })
            info.addExternalFunc("g_write_pipe",
                    RuntimeDebugExec("写入管道", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.pipe.write(args[0].int, args[1])) })
            info.addExternalFunc("g_get_share",
                    RuntimeDebugExec("读取共享", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> status.service.userService.share[args[0].int] })
            info.addExternalFunc("g_set_share",
                    RuntimeDebugExec("写入共享", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.share.set(args[0].int, args[1])) })
            info.addExternalFunc("g_lock_share",
                    RuntimeDebugExec("锁定共享", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.share.lock(args[0].int)) })
            info.addExternalFunc("g_unlock_share",
                    RuntimeDebugExec("解锁共享", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.share.unlock(args[0].int)) })
            info.addExternalFunc("g_win_send_msg",
                    RuntimeDebugExec("发送消息", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kInt, RuntimeObjectType.kInt, RuntimeObjectType.kInt))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.window.sendMessage(args[0].int, args[1].int, args[2].int, args[3].int)) })
        }

        private fun importFromBase(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            val importValue = arrayOf("g_null", "g_true", "g_false", "g_endl", "g_nullptr")
            for (key in importValue) {
                info.addExternalValue(key, refer.getValueCallByName(key)!!)
            }
            info.addExternalValue("g_class_context", RuntimeDebugValue { globalContext })
            info.addExternalValue("g_noop_true", RuntimeDebugValue { RuntimeObject(true, RuntimeObjectType.kNoop) })
            info.addExternalValue("g_noop_false", RuntimeDebugValue { RuntimeObject(false, RuntimeObjectType.kNoop) })

            val importFunc = arrayOf("g_is_null", "g_set_debug", "g_not_null", "g_to_string", "g_new", "g_doc", "g_get_type", "g_get_type_ordinal", "g_type", "g_args_count", "g_args_index", "g_get_timestamp", "g_is_flag", "g_set_flag", "g_get_flag", "g_is_valid_handle")
            for (key in importFunc) {
                info.addExternalFunc(key, refer.getExecCallByName(key)!!)
            }

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
            info.addExternalFunc("g_put",
                    RuntimeDebugExec("流输出", arrayOf(RuntimeObjectType.kObject))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val text = args[0].obj.toString()
                            status.ring3.put(text)
                            null
                        }
                    })
            info.addExternalFunc("g_env_get",
                    RuntimeDebugExec("获取系统变量", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(System.getProperty(args[0].obj.toString()).toString()) })
            info.addExternalFunc("g_load_resource",
                    RuntimeDebugExec("读资源文件", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val filename = args[0].obj.toString()
                            val `is` = ::ModuleUserBase.javaClass.getResourceAsStream(filename) ?: null
                            val reader = BufferedReader(InputStreamReader(`is`, UTF_8))
                            val content = reader.readLines().joinToString(System.lineSeparator())
                            RuntimeObject(content)
                        }
                    })
            info.addExternalFunc("g_disable_result",
                    RuntimeDebugExec("禁用输出结果")
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            status.ring3.setOptionsBool(LOG_FILE, false)
                            null
                        }
                    })
            info.addExternalFunc("g_info_get_doc",
                    RuntimeDebugExec("获取所有文档")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.allDocs) })
            info.addExternalFunc("g_web_exec",
                    RuntimeDebugExec("执行用户程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val map = RuntimeMap()
                            if (args[0].obj == null) {
                                map.put("error", RuntimeObject(true))
                                map.put("msg", RuntimeObject("input null"))
                            } else {
                                val id = args[0].obj.toString()
                                val code = status.service.pipeService.readAndDestroy(EXEC_PREFIX + id)
                                if (code == null) {
                                    map.put("error", RuntimeObject(true))
                                    map.put("msg", RuntimeObject("code null"))
                                } else {
                                    try {
                                        val pid = status.ring3.execFile(EXEC_PATH_PREFIX + id, code)
                                        status.getRing3(pid)!!.setOptionsBool(LOG_PIPE, true)
                                        map.put("pid", RuntimeObject(pid.toLong()))
                                    } catch (e: RuntimeException) {
                                        e.printStackTrace()
                                        map.put("error", RuntimeObject(true))
                                        map.put("msg", RuntimeObject(e.info))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        map.put("error", RuntimeObject(true))
                                        map.put("msg", RuntimeObject(e.message))
                                    }

                                }
                            }
                            RuntimeObject(map)
                        }
                    })
            info.addExternalFunc("g_web_exec_query",
                    RuntimeDebugExec("查询用户程序状态", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val map = RuntimeMap()
                            if (args[0].obj == null) {
                                map.put("error", RuntimeObject(true))
                                map.put("msg", RuntimeObject("id null"))
                            } else {
                                val id = args[0].obj.toString()
                                val result = status.service.fileService.readAndDestroy("$" + RuntimeProcess.USER_PROC_FILE_PREFIX + id)
                                if (result == null) { // 未结束
                                    val data = status.service.pipeService.readAll(RuntimeProcess.USER_PROC_PIPE_PREFIX + id)
                                    if (data != null) {
                                        map.put("data", RuntimeObject(data))
                                    } else {
                                        map.put("error", RuntimeObject(true))
                                        map.put("msg", RuntimeObject("invalid id"))
                                    }
                                } else { // 结束
                                    map.put("halt", RuntimeObject(true))
                                    map.put("data", RuntimeObject(status.service.pipeService.readAndDestroy(RuntimeProcess.USER_PROC_PIPE_PREFIX + id)))
                                    map.put("result", RuntimeObject(result))
                                }
                            }
                            RuntimeObject(map)
                        }
                    })
            info.addExternalFunc("g_web_exec_kill",
                    RuntimeDebugExec("中止用户程序", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val map = RuntimeMap()
                            if (args[0].obj == null) {
                                map.put("error", RuntimeObject(true))
                                map.put("msg", RuntimeObject("id null"))
                            } else {
                                val id = args[0].obj.toString()
                                try {
                                    val pid = Integer.parseInt(id)
                                    map.put("data", RuntimeObject(status.service.processService.ring3Kill(pid, "远程中止")))
                                } catch (e: NumberFormatException) {
                                    e.printStackTrace()
                                    map.put("error", RuntimeObject(true))
                                    map.put("msg", RuntimeObject("invalid id"))
                                }

                            }
                            RuntimeObject(map)
                        }
                    })
            info.addExternalFunc("g_fork",
                    RuntimeDebugExec("进程分叉")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.processService.ring3.fork().toLong()) })
        }

        private fun importFromString(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            val importFunc = arrayOf("g_string_replace", "g_string_split", "g_string_splitn", "g_string_trim", "g_string_length", "g_string_empty", "g_string_get", "g_string_regex", "g_string_build", "g_string_atoi", "g_string_atoi_s", "g_string_join_array", "g_string_toupper", "g_string_tolower", "g_string_rep", "g_string_to_number", "g_string_equal", "g_string_not_equal", "g_string_start_with", "g_string_end_with", "g_string_substr", "g_string_left", "g_string_right")
            for (key in importFunc) {
                info.addExternalFunc(key, refer.getExecCallByName(key)!!)
            }
            info.addExternalFunc("g_is_letter",
                    RuntimeDebugExec("是否是英文字符", arrayOf(RuntimeObjectType.kChar))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Character.isLetter(args[0].char)) })
            info.addExternalFunc("g_is_digit",
                    RuntimeDebugExec("是否是数字字符", arrayOf(RuntimeObjectType.kChar))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Character.isDigit(args[0].char)) })
            info.addExternalFunc("g_is_letter_or_digit",
                    RuntimeDebugExec("是否是数字与英文字符", arrayOf(RuntimeObjectType.kChar))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Character.isLetterOrDigit(args[0].char)) })
            info.addExternalFunc("g_is_whitespace",
                    RuntimeDebugExec("是否是空白字符", arrayOf(RuntimeObjectType.kChar))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Character.isWhitespace(args[0].char)) })
            info.addExternalFunc("g_char_to_digit",
                    RuntimeDebugExec("字符转数字", arrayOf(RuntimeObjectType.kChar))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            var ch = args[0].char
                            if (Character.isDigit(ch)) {
                                RuntimeObject((ch - '0').toLong())
                            } else {
                                ch = Character.toLowerCase(ch)
                                if (ch in 'a'..'f') {
                                    RuntimeObject((ch.toInt() + 10 - 'a'.toInt()).toLong())
                                } else RuntimeObject(16.toLong())
                            }
                        }
                    })
        }

        private fun importFromTask(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            info.addExternalFunc("g_env_get_guid", refer.getExecCallByName("g_task_get_guid")!!)
            info.addExternalFunc("g_res_get_speed",
                    RuntimeDebugExec("管道列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val speed = status.service.processService.speed.toLong()
                            when {
                                speed > 1000000L -> RuntimeObject((speed / 1000000L).toString() + "M")
                                speed > 1000L -> RuntimeObject((speed / 1000L).toString() + "K")
                                else -> RuntimeObject(speed.toString())
                            }
                        }
                    })
            info.addExternalFunc("g_res_get_pipe",
                    RuntimeDebugExec("管道列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.pipeService.stat(true)) })
            info.addExternalFunc("g_res_get_pipe_size",
                    RuntimeDebugExec("管道列表数量")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.pipeService.size()) })
            info.addExternalFunc("g_res_get_share",
                    RuntimeDebugExec("共享列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.shareService.stat(true)) })
            info.addExternalFunc("g_res_get_share_size",
                    RuntimeDebugExec("共享列表数量")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.shareService.size()) })
            info.addExternalFunc("g_res_get_user_list",
                    RuntimeDebugExec("用户服务列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.stat(true)) })
        }

        private fun importFromList(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            val importFunc = arrayOf("g_array_add", "g_array_contains", "g_array_append", "g_array_insert", "g_array_set", "g_array_pop", "g_array_clear", "g_array_reverse", "g_array_get", "g_array_get_ex", "g_array_size", "g_array_remove", "g_array_delete", "g_array_empty", "g_array_fill", "g_map_keys", "g_map_values", "g_map_put", "g_map_contains", "g_map_get", "g_map_size", "g_map_remove", "g_map_clear", "g_map_empty", "g_array_range")
            for (key in importFunc) {
                info.addExternalFunc(key, refer.getExecCallByName(key)!!)
            }
        }

        private fun importFromProc(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            val importProc = arrayOf("g_available_process", "g_block", "g_sleep")
            for (key in importProc) {
                info.addExternalFunc(key, refer.getExecCallByName(key)!!)
            }
            info.addExternalFunc("g_pid", refer.getExecCallByName("g_get_pid")!!)
            info.addExternalFunc("g_available_process", refer.getExecCallByName("g_available_process")!!)
            info.addExternalFunc("g_res_get_proc",
                    RuntimeDebugExec("进程列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val inf = status.service.processService.procInfoCache
                            val array = RuntimeArray()
                            for (i in inf) {
                                val item = RuntimeArray()
                                for (j in i) {
                                    item.add(RuntimeObject(j))
                                }
                                array.add(RuntimeObject(item))
                            }
                            RuntimeObject(array)
                        }
                    })
            info.addExternalFunc("g_res_get_proc_size",
                    RuntimeDebugExec("进程列表数量")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.processService.procInfoCache.size.toLong()) })
        }

        private fun importFromNet(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            info.addExternalFunc("g_info_get_ip", refer.getExecCallByName("g_web_get_ip")!!)
            info.addExternalFunc("g_info_get_hostname", refer.getExecCallByName("g_web_get_hostname")!!)
        }

        private fun importFromFile(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            info.addExternalFunc("g_res_get_file",
                    RuntimeDebugExec("文件列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.fileService.stat(true)) })
            info.addExternalFunc("g_res_get_file_size",
                    RuntimeDebugExec("文件列表数量")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.fileService.size()) })
            info.addExternalFunc("g_res_get_vfs", refer.getExecCallByName("g_read_file_vfs_utf8")!!)
            info.addExternalFunc("g_res_get_vfs_list",
                    RuntimeDebugExec("获取VFS列表")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.fileService.getVfsList(true)) })
            info.addExternalFunc("g_res_get_vfs_size",
                    RuntimeDebugExec("VFS列表数量")
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.fileService.vfsListSize) })
            info.addExternalFunc("g_query_file_internal",
                    RuntimeDebugExec("查询文件状态", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.file.queryFile(args[0].int)) })
            info.addExternalFunc("g_create_file_internal",
                    RuntimeDebugExec("创建文件", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kBool))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.file.createFile(args[0].int, args[1].bool)) })
            info.addExternalFunc("g_delete_file_internal",
                    RuntimeDebugExec("删除文件", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.file.deleteFile(args[0].int)) })
            info.addExternalFunc("g_read_file_internal",
                    RuntimeDebugExec("读取文件", arrayOf(RuntimeObjectType.kPtr))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(String(status.service.userService.file.readFile(args[0].int) ?: ByteArray(0), Charsets.UTF_8)) })
            info.addExternalFunc("g_write_file_internal",
                    RuntimeDebugExec("写入文件", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kString, RuntimeObjectType.kBool, RuntimeObjectType.kBool))
                    { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.file.writeFile(args[0].int, args[1].string.toByteArray(Charsets.UTF_8), args[2].bool, args[3].bool)) })
        }

        private fun importFromMath(info: IRuntimeDebugInfo, refer: IRuntimeDebugInfo) {
            val importValue = arrayOf("g_PI", "g_PI_2", "g_E", "g_random")
            for (key in importValue) {
                info.addExternalValue(key, refer.getValueCallByName(key)!!)
            }

            val importFunc = arrayOf("g_sqrt", "g_cos", "g_sin", "g_floor", "g_atan2", "g_random_int")
            for (key in importFunc) {
                info.addExternalFunc(key, refer.getExecCallByName(key)!!)
            }
        }
    }
}