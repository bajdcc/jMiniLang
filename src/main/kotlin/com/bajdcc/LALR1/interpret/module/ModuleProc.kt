package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject
import com.bajdcc.util.ResourceLoader

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】进程模块
 *
 * @author bajdcc
 */
class ModuleProc : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.proc"

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
            buildMethod(info)
            buildPipeMethod(info)
            buildShareMethod(info)

            runtimeCodePage = page
            return page
        }

    private fun buildMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_create_process",
                RuntimeDebugExec("创建进程", arrayOf(RuntimeObjectType.kFunc))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val func = args[0].obj as RuntimeFuncObject?
                        RuntimeObject(status.createProcess(func!!).toLong())
                    }
                })
        info.addExternalFunc("g_create_process_args",
                RuntimeDebugExec("创建进程带参数", arrayOf(RuntimeObjectType.kFunc, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val func = args[0].obj as RuntimeFuncObject?
                        val obj = args[1]
                        RuntimeObject(status.createProcess(func!!, obj).toLong())
                    }
                })
        info.addExternalFunc("g_create_user_process",
                RuntimeDebugExec("创建用户态进程", arrayOf(RuntimeObjectType.kFunc))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val func = args[0].obj as RuntimeFuncObject?
                        RuntimeObject(status.createUsrProcess(func!!).toLong())
                    }
                })
        info.addExternalFunc("g_create_user_process_args",
                RuntimeDebugExec("创建用户态进程带参数", arrayOf(RuntimeObjectType.kFunc, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val func = args[0].obj as RuntimeFuncObject?
                        val obj = args[1]
                        RuntimeObject(status.createUsrProcess(func!!, obj).toLong())
                    }
                })
        info.addExternalFunc("g_get_user_procs",
                RuntimeDebugExec("获取用户态进程ID列表")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.usrProcs.map { RuntimeObject(it) }) })
        info.addExternalFunc("g_get_pid",
                RuntimeDebugExec("获取进程ID")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.pid.toLong()) })
        info.addExternalFunc("g_get_parent_pid",
                RuntimeDebugExec("获取父进程ID")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.parentPid.toLong()) })
        info.addExternalFunc("g_get_process_priority",
                RuntimeDebugExec("获取进程优先级")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.priority.toLong()) })
        info.addExternalFunc("g_set_process_priority",
                RuntimeDebugExec("设置进程优先级", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val priority = args[0].long
                        RuntimeObject(status.setPriority(priority.toInt()))
                    }
                })
        info.addExternalFunc("g_join_process_once",
                RuntimeDebugExec("进程等待", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val pid = args[0].long
                        RuntimeObject(status.service.processService.join(pid.toInt(), status.pid))
                    }
                })
        info.addExternalFunc("g_live_process",
                RuntimeDebugExec("进程存活", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val pid = args[0].long
                        RuntimeObject(status.service.processService.live(pid.toInt()))
                    }
                })
        info.addExternalFunc("g_sleep",
                RuntimeDebugExec("进程睡眠", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val time = args[0].int
                        RuntimeObject(status.service.processService.sleep(status.pid, if (time > 0) time else 0).toLong())
                    }
                })
        info.addExternalFunc("g_query_usr_proc",
                RuntimeDebugExec("枚举用户态进程")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(ProcInfoHelper.getProcInfo(status, status.usrProcs)) })
        info.addExternalFunc("g_query_sys_proc",
                RuntimeDebugExec("枚举内核态进程")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(ProcInfoHelper.getProcInfo(status, status.sysProcs)) })
        info.addExternalFunc("g_query_all_proc",
                RuntimeDebugExec("枚举进程")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(ProcInfoHelper.getProcInfoAll(status)) })
        info.addExternalFunc("g_set_process_desc",
                RuntimeDebugExec("设置进程说明", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        status.setProcDesc(args[0].obj.toString())
                        null
                    }
                })
    }

    private fun buildPipeMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_create_pipe",
                RuntimeDebugExec("创建管道", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        val handle = status.service.pipeService.create(name, status.page)
                        if (handle == -1)
                            status.err(RuntimeException.RuntimeError.MAX_HANDLE)
                        RuntimeObject(handle)
                    }
                })
        info.addExternalFunc("g_query_pipe",
                RuntimeDebugExec("查询管道", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        RuntimeObject(status.service.pipeService.query(name))
                    }
                })
        info.addExternalFunc("g_destroy_pipe_once",
                RuntimeDebugExec("销毁管道", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        RuntimeObject(status.service.pipeService.destroy(handle))
                    }
                })
        info.addExternalFunc("g_destroy_pipe_by_name_once",
                RuntimeDebugExec("销毁管道", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].obj.toString()
                        RuntimeObject(status.service.pipeService.destroyByName(status.pid, name))
                    }
                })
        info.addExternalFunc("g_wait_pipe_empty",
                RuntimeDebugExec("等待管道为空", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        status.service.processService.sleep(status.pid, PIPE_READ_TIME)
                        RuntimeObject(status.service.pipeService.hasData(handle))
                    }
                })
        info.addExternalFunc("g_read_pipe_char",
                RuntimeDebugExec("管道读", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val ch = status.service.pipeService.read(status.pid, handle)
                        RuntimeObject(ch)
                    }
                })
        info.addExternalFunc("g_read_pipe_char_no_block",
                RuntimeDebugExec("管道读（非阻塞）", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val ch = status.service.pipeService.readNoBlock(status.pid, handle)
                        RuntimeObject(ch)
                    }
                })
        info.addExternalFunc("g_write_pipe_char",
                RuntimeDebugExec("管道写", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kChar))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val ch = args[1].char
                        RuntimeObject(status.service.pipeService.write(handle, ch))
                    }
                })
    }

    private fun buildShareMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_start_share",
                RuntimeDebugExec("创建共享", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        val result = status.service.shareService.startSharing(name, args[1], status.page)
                        if (result == -1)
                            status.err(RuntimeException.RuntimeError.MAX_HANDLE, name)
                        if (result == 0)
                            status.err(RuntimeException.RuntimeError.DUP_SHARE_NAME, name)
                        RuntimeObject(result.toLong())
                    }
                })
        info.addExternalFunc("g_create_share",
                RuntimeDebugExec("创建共享", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kObject))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        val result = status.service.shareService.createSharing(name, args[1], status.page)
                        if (result == -1)
                            status.err(RuntimeException.RuntimeError.MAX_HANDLE, name)
                        RuntimeObject(result.toLong())
                    }
                })
        info.addExternalFunc("g_query_share",
                RuntimeDebugExec("查询共享", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        status.service.shareService.getSharing(name, false)
                    }
                })
        info.addExternalFunc("g_reference_share",
                RuntimeDebugExec("引用共享", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        status.service.shareService.getSharing(name, true)
                    }
                })
        info.addExternalFunc("g_stop_share",
                RuntimeDebugExec("停止共享", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        val result = status.service.shareService.stopSharing(name)
                        if (result == -1)
                            status.err(RuntimeException.RuntimeError.INVALID_SHARE_NAME, name)
                        if (result == 2)
                            status.err(RuntimeException.RuntimeError.INVALID_REFERENCE, name)
                        RuntimeObject(result == 1)
                    }
                })
        info.addExternalFunc("g_try_lock_share",
                RuntimeDebugExec("尝试锁定共享变量", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        if (status.service.shareService.isLocked(name)) {
                            status.service.processService.sleep(status.pid, LOCK_TIME)
                            RuntimeObject(true)
                        } else {
                            status.service.shareService.setLocked(name, true)
                            RuntimeObject(false)
                        }
                    }
                })
        info.addExternalFunc("g_unlock_share",
                RuntimeDebugExec("解锁共享变量", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        if (status.service.shareService.isLocked(name)) {
                            status.service.shareService.setLocked(name, false)
                            RuntimeObject(true)
                        } else {
                            RuntimeObject(false)
                        }
                    }
                })
        info.addExternalFunc("g_proc_exec",
                RuntimeDebugExec("运行用户态代码", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.ring3.exec(args[0].string).toLong()) })
        info.addExternalFunc("g_proc_exec_file",
                RuntimeDebugExec("运行用户态代码", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    RuntimeObject(status.ring3.execFile(
                            args[0].obj.toString(),
                            args[1].obj.toString()).toLong())
                })
        info.addExternalFunc("g_proc_kill",
                RuntimeDebugExec("强制结束用户态进程", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val pid = args[0].long
                        RuntimeObject(status.service.processService.ring3Kill(pid.toInt(), "强制退出").toLong())
                    }
                })
    }

    internal object ProcInfoHelper {
        fun getProcInfo(status: IRuntimeStatus, pids: List<Int>): RuntimeArray {
            return getProcInfo2(getProcInfo3(status, pids))
        }

        fun getProcInfoAll(status: IRuntimeStatus): RuntimeArray {
            return getProcInfo2(getProcInfo4(status))
        }

        fun getProcInfo2(objs: List<Array<Any>>): RuntimeArray {
            val array = RuntimeArray()
            array.add(RuntimeObject(String.format(" %s  %s %-5s   %-15s   %-25s   %s",
                    " ", "环", "标识", "名称", "过程", "描述")))
            for (obj in objs) {
                var name = obj[4].toString()
                name = name.substring(0, Math.min(name.length, 20))
                array.add(RuntimeObject(String.format(" %s  %s %5s   %-15s   %-25s   %s",
                        obj[0], obj[1], obj[2], obj[3], name, obj[5])))
            }
            return array
        }

        fun getProcInfo3(status: IRuntimeStatus, pids: List<Int>): List<Array<Any>> {
            val objs = arrayListOf<Array<Any>>()
            for (pid in pids) {
                objs.add(status.getProcInfoById(pid))
            }
            return objs
        }

        fun getProcInfo4(status: IRuntimeStatus): List<Array<Any>> {
            return status.service.processService.procInfoCache
        }
    }

    companion object {

        val instance = ModuleProc()

        private const val LOCK_TIME = 20
        private const val PIPE_READ_TIME = 5
    }
}
