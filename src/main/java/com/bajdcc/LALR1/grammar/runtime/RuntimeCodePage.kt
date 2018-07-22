package com.bajdcc.LALR1.grammar.runtime

import com.bajdcc.util.intervalTree.Interval
import com.bajdcc.util.intervalTree.IntervalTree
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

/**
 * 【目标代码】代码页
 *
 * @author bajdcc
 */
class RuntimeCodePage(
        /**
         * 数据
         */
        val data: List<Any>,
        /**
         * 指令
         */
        val insts: List<Byte>,
        /**
         * 调试开发
         */
        val info: IRuntimeDebugInfo,
        /**
         * 汇编调试的符号表
         */
        private val itvList: List<Interval<Any>>) : Serializable {

    @Transient
    private var tree: IntervalTree<Any>? = null

    val codeString: String
        get() = "#### 目标代码 ####" +
                System.lineSeparator() +
                insts +
                System.lineSeparator()

    fun getDebugInfoByInc(index: Long): String {
        if (tree == null)
            tree = IntervalTree(itvList)
        val list = tree!![index]
        return if (list.isEmpty()) "[NO DEBUG INFO]" else
            list.joinToString(
                    separator = System.lineSeparator(),
                    prefix = "---=== 代码片段 ===---" + System.lineSeparator(),
                    transform = { it.toString() })
    }

    override fun toString(): String {
        return codeString
    }

    companion object {

        private const val serialVersionUID = 1L

        fun importFromStream(input: InputStream): RuntimeCodePage {
            return ObjectTools.deserialize<RuntimeCodePage>(input)!!
        }

        fun exportFromStream(page: RuntimeCodePage,
                             output: OutputStream) {
            ObjectTools.serialize(page, output)
        }
    }
}
