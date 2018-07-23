package com.bajdcc.LALR1.grammar.tree.closure

/**
 * 闭包
 *
 * @author bajdcc
 */
open class ClosureScope : IClosureScope {

    private val ref = mutableSetOf<Any>()
    private val decl = mutableSetOf<Any>()

    protected val closures: Set<Any>?
        get() {
            val closure = HashSet(ref)
            closure.removeAll(decl)
            return if (closure.isEmpty()) null else closure
        }

    override fun addRef(obj: Any) {
        ref.add(obj)
    }

    override fun addDecl(obj: Any) {
        decl.add(obj)
    }
}
