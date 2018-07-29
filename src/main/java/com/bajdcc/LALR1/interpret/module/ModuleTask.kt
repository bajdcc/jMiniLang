package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.OP.grammar.error.GrammarException
import com.bajdcc.OP.grammar.handler.IPatternHandler
import com.bajdcc.OP.syntax.handler.SyntaxException
import com.bajdcc.util.ResourceLoader
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType
import org.apache.log4j.Logger
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】服务模块
 *
 * @author bajdcc
 */
class ModuleTask : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.task"

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
            buildSystemMethod(info)
            buildUtilMethod(info)

            runtimeCodePage = page
            return page
        }

    private fun buildSystemMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_task_get_time",
                RuntimeDebugExec("获取当前时间", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val format = args[0].obj.toString()
                        RuntimeObject(SimpleDateFormat(format).format(Date()))
                    }
                })
        info.addExternalFunc("g_task_get_timestamp",
                RuntimeDebugExec("获取当前时间戳")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(System.currentTimeMillis()) })
        info.addExternalFunc("g_task_get_pipe_stat",
                RuntimeDebugExec("获取管道信息")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.pipeService.stat(false)) })
        info.addExternalFunc("g_task_get_share_stat",
                RuntimeDebugExec("获取共享信息")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.shareService.stat(false)) })
        info.addExternalFunc("g_task_get_file_stat",
                RuntimeDebugExec("获取文件信息")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.fileService.stat(false)) })
        info.addExternalFunc("g_task_get_vfs_stat",
                RuntimeDebugExec("获取虚拟文件信息")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.fileService.getVfsList(false)) })
        info.addExternalFunc("g_task_get_user_stat",
                RuntimeDebugExec("获取用户服务信息")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.userService.stat(false)) })
        info.addExternalFunc("g_task_get_guid",
                RuntimeDebugExec("获取GUID")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(guid.toString()) })
        info.addExternalFunc("g_task_sys_speed",
                RuntimeDebugExec("获取虚拟机运行速度")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(status.service.processService.speed) })
    }

    private fun buildUtilMethod(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_task_calc",
                RuntimeDebugExec("四则运算", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val expr = args[0].obj.toString()
                        RuntimeObject(util_calc(expr))
                    }
                })
    }

    companion object {

        val instance = ModuleTask()
        private val logger = Logger.getLogger("task")

        val TASK_NUM = 16
        private val guid = UUID.randomUUID()

        private fun util_calc(expr: String): String {
            try {
                val grammar = com.bajdcc.OP.grammar.Grammar(expr)
                grammar.addTerminal("i", TokenType.INTEGER, null)
                grammar.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS)
                grammar.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS)
                grammar.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES)
                grammar.addTerminal("DIVIDE", TokenType.OPERATOR,
                        OperatorType.DIVIDE)
                grammar.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN)
                grammar.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN)
                val nons = arrayOf("E", "T", "F")
                for (non in nons) {
                    grammar.addNonTerminal(non)
                }
                grammar.addPatternHandler("1", object : IPatternHandler {
                    override fun handle(tokens: List<Token>, symbols: List<Any>): Any {
                        return Integer.parseInt(tokens[0].obj!!.toString())
                    }

                    override fun getPatternName(): String {
                        return "操作数转换"
                    }
                })
                grammar.addPatternHandler("010", object : IPatternHandler {
                    override fun handle(tokens: List<Token>, symbols: List<Any>): Any {
                        val lop = symbols[0] as Int
                        val rop = symbols[1] as Int
                        val op = tokens[0]
                        if (op.type === TokenType.OPERATOR) {
                            val kop = op.obj as OperatorType?
                            when (kop) {
                                OperatorType.PLUS -> return lop + rop
                                OperatorType.MINUS -> return lop - rop
                                OperatorType.TIMES -> return lop * rop
                                OperatorType.DIVIDE -> return if (rop == 0) {
                                    lop
                                } else {
                                    lop / rop
                                }
                                else -> return 0
                            }
                        } else {
                            return 0
                        }
                    }

                    override fun getPatternName(): String {
                        return "二元运算"
                    }
                })
                grammar.addPatternHandler("101", object : IPatternHandler {
                    override fun handle(tokens: List<Token>, symbols: List<Any>): Any? {
                        val ltok = tokens[0]
                        val rtok = tokens[1]
                        val exp = symbols[0]
                        return if (ltok.obj === OperatorType.LPARAN && rtok.obj === OperatorType.RPARAN) {// 判断括号
                            exp
                        } else null
                    }

                    override fun getPatternName(): String {
                        return "括号运算"
                    }
                })
                grammar.infer("E -> E @PLUS T | E @MINUS T | T")
                grammar.infer("T -> T @TIMES F | T @DIVIDE F | F")
                grammar.infer("F -> @LPA E @RPA | @i")
                grammar.initialize("E")
                return grammar.run().toString()
            } catch (e: RegexException) {
                logger.error("#CALC# Error: " + e.position + "," + e.message)
            } catch (e: SyntaxException) {
                logger.error("#CALC#Error: " + e.position + "," + e.message + " " + e.info)
            } catch (e: GrammarException) {
                logger.error("#CALC#Error: " + e.position + "," + e.message + " " + e.info)
            }

            return "#CALC#Error"
        }
    }
}
