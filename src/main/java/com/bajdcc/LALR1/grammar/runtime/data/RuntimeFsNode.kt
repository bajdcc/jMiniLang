package com.bajdcc.LALR1.grammar.runtime.data

import java.util.*

/**
 * 【运行时】用户服务文件系统结点
 *
 * @author bajdcc
 */
class RuntimeFsNode(var name: String, file: Boolean) {

    var children: MutableMap<String, RuntimeFsNode>? = null
    var createTime = System.currentTimeMillis()
    var modifiedTime = System.currentTimeMillis()
    var data = ByteArray(0)

    val isFile: Boolean
        get() = children == null

    init {
        if (!file) {
            children = TreeMap()
        }
    }

    companion object {
        fun root(): RuntimeFsNode = RuntimeFsNode("/", false)
    }
}

private fun RuntimeFsNode.findNodeOfChildren(name: String): RuntimeFsNode? {
    return when {
        name.isEmpty() -> null
        else -> this.children?.get(name)
    }
}

private fun RuntimeFsNode.findNode(path: String): RuntimeFsNode? {
    val sp = path.split('/')
    if (sp.isEmpty() || sp[0] != "")
        return null
    var i = 1
    var node: RuntimeFsNode = this
    while (i < sp.size) {
        node = node.findNodeOfChildren(sp[i]) ?: return null
        i++
    }
    return node
}

fun RuntimeFsNode.createNode(path: String): RuntimeFsNode? {
    val sp = path.split('/')
    if (sp.isEmpty() || sp[0] != "")
        return null
    var i = 1
    var node: RuntimeFsNode = this
    while (i < sp.size - 1) {
        node = node.findNodeOfChildren(sp[i]) ?: return null
        i++
    }
    if (node.isFile)
        return null
    val name = sp.last()
    if (!node.children!!.contains(name)) {
        val newNode = RuntimeFsNode(name, true)
        node.children!![name] = newNode
        return newNode
    }
    return node.children!![name]
}

fun RuntimeFsNode.deleteNode(path: String): Boolean {
    val sp = path.split('/')
    if (sp.isEmpty() || sp[0] != "")
        return false
    var i = 1
    var node: RuntimeFsNode = this
    while (i < sp.size - 1) {
        node = node.findNodeOfChildren(sp[i]) ?: return false
        i++
    }
    if (node.isFile)
        return false
    return node.children!!.remove(sp.last()) != null
}

fun RuntimeFsNode.query(path: String): Long {
    val node = this.findNode(path) ?: return 0L
    return if (node.isFile) 1L else 2L
}

fun RuntimeFsNode.read(path: String): ByteArray? = this.findNode(path)?.data
fun RuntimeFsNode.write(path: String, data: ByteArray, overwrite: Boolean, createIfNotExist: Boolean): Long {
    var node = this.findNode(path)
    if (node == null && createIfNotExist) {
        node = this.createNode(path) ?: return -1L
    }
    if (node == null)
        return -2L
    if (!node.isFile)
        return -3L
    if (overwrite)
        node.data = data
    else
        node.data += data
    return 0L
}