package com.bajdcc.LALR1.grammar.runtime.service

/**
 * 【运行时】运行时服务接口
 *
 * @author bajdcc
 */
interface IRuntimeService {

    val pipeService: IRuntimePipeService

    val shareService: IRuntimeShareService

    val processService: IRuntimeProcessService

    val fileService: IRuntimeFileService

    val dialogService: IRuntimeDialogService

    val userService: IRuntimeUserService
}
