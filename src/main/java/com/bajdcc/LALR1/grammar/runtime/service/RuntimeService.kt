package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess

/**
 * 【运行时】运行时服务
 *
 * @author bajdcc
 */
class RuntimeService(private val process: RuntimeProcess) : IRuntimeService {
    private val pipe: RuntimePipeService = RuntimePipeService(this)
    private val share: RuntimeShareService = RuntimeShareService(this)
    private val file: RuntimeFileService = RuntimeFileService(this)
    private val dialog: RuntimeDialogService = RuntimeDialogService(this)
    private val user: RuntimeUserService = RuntimeUserService(this)

    override val pipeService: IRuntimePipeService
        get() = pipe

    override val shareService: IRuntimeShareService
        get() = share

    override val processService: IRuntimeProcessService
        get() = process

    override val fileService: IRuntimeFileService
        get() = file

    override val dialogService: IRuntimeDialogService
        get() = dialog

    override val userService: IRuntimeUserService
        get() = user
}
