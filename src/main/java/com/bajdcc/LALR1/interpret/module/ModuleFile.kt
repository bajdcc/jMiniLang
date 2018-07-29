package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.util.ResourceLoader

/**
 * 【模块】文件模块
 *
 * @author bajdcc
 */
class ModuleFile : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.file"

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

            runtimeCodePage = page
            return page
        }

    private fun buildMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_create_file_internal",
                RuntimeDebugExec("创建文件", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kInt, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val name = args[0].string
                        val mode = args[1].int
                        val encoding = args[2].string
                        RuntimeObject(status.service.fileService.create(name, mode, encoding, status.page))
                    }
                })
        info.addExternalFunc("g_destroy_file_internal",
                RuntimeDebugExec("关闭文件", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        RuntimeObject(status.service.fileService.destroy(handle))
                    }
                })
        info.addExternalFunc("g_write_file_internal",
                RuntimeDebugExec("写文件", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kChar))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val ch = args[1].char
                        RuntimeObject(status.service.fileService.write(handle, ch))
                    }
                })
        info.addExternalFunc("g_write_file_string_internal",
                RuntimeDebugExec("写文件（字串）", arrayOf(RuntimeObjectType.kPtr, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val str = args[1].string
                        RuntimeObject(status.service.fileService.writeString(handle, str))
                    }
                })
        info.addExternalFunc("g_read_file_internal",
                RuntimeDebugExec("读文件", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val ch = status.service.fileService.read(handle)
                        if (ch == -1) null else RuntimeObject(ch.toChar())
                    }
                })
        info.addExternalFunc("g_read_file_string_internal",
                RuntimeDebugExec("读文件（行）", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        val str = status.service.fileService.readString(handle)
                        RuntimeObject(str)
                    }
                })
        info.addExternalFunc("g_query_file",
                RuntimeDebugExec("判断文件是否存在", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val filename = args[0].string
                        RuntimeObject(status.service.fileService.exists(filename))
                    }
                })
        info.addExternalFunc("g_read_file_vfs_utf8",
                RuntimeDebugExec("读文件（一次性），限VFS", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val filename = args[0].string
                        RuntimeObject(status.service.fileService.readAll(filename))
                    }
                })
    }

    companion object {

        val instance = ModuleFile()
    }
}
