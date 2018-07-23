package com.bajdcc.LALR1.grammar.symbol

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.Function
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.util.HashListMap
import com.bajdcc.util.HashListMapEx
import com.bajdcc.util.Position
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType
import java.util.*

/**
 * 命名空间管理
 *
 * @author bajdcc
 */
class ManageScopeSymbol : IQueryScopeSymbol, IQueryBlockSymbol, IManageDataSymbol, IManageScopeSymbol {
    private var lambdaId = 0
    override val symbolList = HashListMap<Any>()
    override val funcMap = HashListMapEx<String, MutableList<Function>>()
    private val funcScope = mutableListOf<MutableMap<String, Function>>()
    private val stkScope = mutableListOf<MutableSet<String>>()
    private val stkLambdaId = Stack<Int>()
    private val stkLambdaLine = Stack<Int>()
    private val symbolsInFutureBlock = mutableSetOf<String>()
    private val blockLevel = mutableMapOf<BlockType, Int>()
    private val blockStack = Stack<BlockType>()

    override val entryName: String
        get() = ENTRY_NAME

    override val entryToken: Token
        get() {
            val token = Token()
            token.type = TokenType.ID
            token.obj = entryName
            token.position = Position()
            return token
        }

    override val lambda: Function
        get() {
            val lambdaId = stkLambdaId.pop()
            val lambdaLine = stkLambdaLine.pop()
            return funcMap["$LAMBDA_PREFIX$lambdaId!$lambdaLine"][0]
        }

    private val symbolString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 符号表 ####")
            sb.append(System.lineSeparator())
            symbolList.toList().withIndex().forEach { (i, symbol) ->
                sb.append(i).append(": ").append("[").append(RuntimeObject.fromObject(symbol).desc).append("] ").append(symbol)
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    private val funcString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 过程表 ####")
            sb.append(System.lineSeparator())
            var i = 0
            for (funcs in funcMap.toList()) {
                for (func in funcs) {
                    sb.append("----==== #").append(i).append(" ====----")
                    sb.append(System.lineSeparator())
                    sb.append(func.toString())
                    sb.append(System.lineSeparator())
                    sb.append(System.lineSeparator())
                    i++
                }
            }
            return sb.toString()
        }

    init {
        enterScope()
        val entry = mutableListOf<Function>()
        entry.add(Function(Token()))
        funcMap.add(ENTRY_NAME, entry)
        for (type in BlockType.values()) {
            blockLevel[type] = 0
        }
    }

    private val currentScope: MutableSet<String>
        get() = stkScope[stkScope.size - 1]

    private val currentFuncScope: MutableMap<String, Function>
        get() = funcScope[funcScope.size - 1]

    override fun enterScope() {
        stkScope.add(mutableSetOf())
        funcScope.add(mutableMapOf())
        symbolsInFutureBlock.forEach { this.registerSymbol(it) }
        clearFutureArgs()
    }

    override fun leaveScope() {
        stkScope.removeAt(stkScope.size - 1)
        funcScope.removeAt(funcScope.size - 1)
        clearFutureArgs()
    }

    override fun clearFutureArgs() {
        symbolsInFutureBlock.clear()
    }

    override fun findDeclaredSymbol(name: String): Boolean {
        if (symbolsInFutureBlock.contains(name)) {
            return true
        }
        if (stkScope.reversed().any { it.contains(name) })
            return true
        if (TokenTools.isExternalName(name)) {
            registerSymbol(name)
            return true
        }
        return false
    }

    override fun findDeclaredSymbolDirect(name: String): Boolean {
        return symbolsInFutureBlock.contains(name) || currentScope.contains(name)
    }

    override fun isUniqueSymbolOfBlock(name: String): Boolean {
        return currentScope.contains(name)
    }

    override fun getFuncByName(name: String): Function? {
        funcScope.indices.reversed().forEach { i ->
            val f = funcScope[i]
            val f1 = f[name]
            if (f1 != null)
                return f1
            for (func in f.values) {
                val funcName = func.realName
                if (funcName == name) {
                    return func
                }
            }
        }
        return null
    }

    override fun isLambda(name: String): Boolean {
        return name.startsWith(LAMBDA_PREFIX)
    }

    override fun registerSymbol(name: String) {
        currentScope.add(name)
        symbolList.add(name)
    }

    override fun registerFunc(func: Function) {
        if (func.name.type === TokenType.ID) {
            func.realName = func.name.toRealString()
            symbolList.add(func.realName)
        } else {
            func.realName = LAMBDA_PREFIX + lambdaId++
        }
        val f = mutableListOf<Function>()
        f.add(func)
        funcMap.add(func.realName, f)
        currentFuncScope[func.realName] = func
    }

    override fun registerLambda(func: Function) {
        stkLambdaId.push(lambdaId)
        stkLambdaLine.push(func.name.position.line)
        func.name.type = TokenType.ID
        func.realName = LAMBDA_PREFIX + lambdaId++ + "!" + stkLambdaLine.peek()
        currentFuncScope[func.realName] = func
        val f = mutableListOf<Function>()
        f.add(func)
        funcMap.add(func.realName, f)
    }

    override fun isRegisteredFunc(name: String): Boolean {
        return funcMap.contains(name)
    }

    override fun registerFutureSymbol(name: String): Boolean {
        return symbolsInFutureBlock.add(name)
    }

    fun check(recorder: ISemanticRecorder) {
        funcMap.toList().flatMap { it }.forEach { it.analysis(recorder) }
    }

    override fun toString(): String {
        return symbolString + funcString
    }

    override fun enterBlock(type: BlockType) {
        when (type) {
            BlockType.kCycle -> {
                val level = blockLevel[type]!!
                blockLevel[type] = level + 1
            }
            BlockType.kFunc, BlockType.kYield -> blockStack.push(type)
        }
    }

    override fun leaveBlock(type: BlockType) {
        when (type) {
            BlockType.kCycle -> {
                val level = blockLevel[type]!!
                blockLevel[type] = level - 1
            }
            BlockType.kFunc, BlockType.kYield -> if (blockStack.peek() == type) {
                blockStack.pop()
            }
        }
    }

    override fun isInBlock(type: BlockType): Boolean {
        return when (type) {
            BlockType.kCycle -> blockLevel[type]!! > 0
            BlockType.kFunc, BlockType.kYield -> !blockStack.isEmpty() && blockStack.peek() == type
        }
    }

    companion object {
        private const val ENTRY_NAME = "main"
        private const val LAMBDA_PREFIX = "~lambda#"
    }
}
