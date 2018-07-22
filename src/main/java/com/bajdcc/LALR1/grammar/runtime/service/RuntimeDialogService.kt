package com.bajdcc.LALR1.grammar.runtime.service

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import org.apache.log4j.Logger
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * 【运行时】运行时文件服务
 *
 * @author bajdcc
 */
class RuntimeDialogService(private val service: RuntimeService) : IRuntimeDialogService {
    private val arrDialogs = Array<DialogStruct?>(MAX_DIALOG) { null }
    private val setDialogId = mutableSetOf<Int>()
    private var cyclePtr = 0

    override fun create(caption: String, text: String, mode: Int, panel: JPanel): Int {
        if (setDialogId.size >= MAX_DIALOG) {
            return -1
        }
        val handle: Int
        while (true) {
            if (arrDialogs[cyclePtr] == null) {
                handle = cyclePtr
                val ds = DialogStruct(handle, caption, text, mode, panel)
                synchronized(setDialogId) {
                    setDialogId.add(cyclePtr)
                    arrDialogs[cyclePtr++] = ds
                    if (cyclePtr >= MAX_DIALOG) {
                        cyclePtr -= MAX_DIALOG
                    }
                }
                break
            }
            cyclePtr++
            if (cyclePtr >= MAX_DIALOG) {
                cyclePtr -= MAX_DIALOG
            }
        }
        logger.debug("Dialog #$handle '$caption' created")
        return handle
    }

    override fun show(handle: Int): Boolean {
        if (!setDialogId.contains(handle)) {
            return false
        }
        val ds = arrDialogs[handle]!!
        val mode = ds.mode
        when (mode) {
            in 0..4 -> {
                val type = mode - 1
                SwingUtilities.invokeLater {
                    JOptionPane.showMessageDialog(ds.panel, ds.text, ds.caption, type)
                    // 取得共享变量
                    // 发送信号
                    synchronized(setDialogId) {
                        arrDialogs[handle] = null
                        setDialogId.remove(handle)
                    }
                    service.pipeService.write(service.pipeService.create("DIALOG#SIG#$handle", "/dialog/0"), '*')
                }
            }
            in 10..13 -> {
                val type = mode - 11
                SwingUtilities.invokeLater {
                    val value = JOptionPane.showConfirmDialog(ds.panel, ds.text, ds.caption, type).toLong()
                    // 取得共享变量
                    val obj = service.shareService.getSharing("DIALOG#DATA#$handle", false)
                    assert(obj.type == RuntimeObjectType.kArray)
                    (obj.obj as RuntimeArray).add(RuntimeObject(value))
                    // 发送信号
                    synchronized(setDialogId) {
                        arrDialogs[handle] = null
                        setDialogId.remove(handle)
                    }
                    service.pipeService.write(service.pipeService.create("DIALOG#SIG#$handle", "/dialog/1"), '*')
                }
            }
            in 20..24 -> {
                val type = mode - 21
                SwingUtilities.invokeLater {
                    val input = JOptionPane.showInputDialog(ds.panel, ds.text, ds.caption, type)
                    // 取得共享变量
                    val obj = service.shareService.getSharing("DIALOG#DATA#$handle", false)
                    assert(obj.type == RuntimeObjectType.kArray)
                    (obj.obj as RuntimeArray).add(RuntimeObject(input))
                    // 发送信号
                    synchronized(setDialogId) {
                        arrDialogs[handle] = null
                        setDialogId.remove(handle)
                    }
                    service.pipeService.write(service.pipeService.create("DIALOG#SIG#$handle", "/dialog/2"), '*')
                }
            }
            else -> {
                synchronized(setDialogId) {
                    arrDialogs[handle] = null
                    setDialogId.remove(handle)
                }
                service.pipeService.write(service.pipeService.create("DIALOG#SIG#$handle", "/dialog/?"), '*')
            }
        }
        return true
    }

    internal data class DialogStruct(val handle: Int, val caption: String, val text: String, val mode: Int, val panel: JPanel)

    companion object {

        private const val MAX_DIALOG = 10
        private val logger = Logger.getLogger("dialog")
    }
}
