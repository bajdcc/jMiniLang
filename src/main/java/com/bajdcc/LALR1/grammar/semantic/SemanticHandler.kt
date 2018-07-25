package com.bajdcc.LALR1.grammar.semantic

import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.LALR1.grammar.symbol.BlockType
import com.bajdcc.LALR1.grammar.symbol.IManageSymbol
import com.bajdcc.LALR1.grammar.symbol.IQuerySymbol
import com.bajdcc.LALR1.grammar.tree.*
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.LALR1.semantic.token.IIndexedData
import com.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

import com.bajdcc.util.lexer.token.TokenType.ID

/**
 * 【语义分析】语义处理器集合
 *
 * @author bajdcc
 */
class SemanticHandler {

    /**
     * 语义分析动作映射表
     */
    private val semanticAnalyzier = mutableMapOf<String, ISemanticAnalyzer>()

    /**
     * 语义执行动作映射表
     */
    private val semanticAction = mutableMapOf<String, ISemanticAction>()

    init {
        initializeAction()
        initializeHandler()
    }

    /**
     * 初始化动作
     */
    private fun initializeAction() {
        /* 进入块 */
        semanticAction["do_enter_scope"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                manage.manageScopeService.enterScope()
            }
        }
        /* 离开块 */
        semanticAction["do_leave_scope"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                manage.manageScopeService.leaveScope()
            }
        }
        /* 声明过程名 */
        semanticAction["predeclear_funcname"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                val token = access.relativeGet(0)
                val funcName = token.toRealString()
                if (token.type === ID) {
                    if (manage.queryScopeService.entryName == funcName) {
                        recorder.add(SemanticError.DUP_ENTRY, token)
                    }
                } else if (manage.queryScopeService.isRegisteredFunc(
                                funcName)) {
                    recorder.add(SemanticError.DUP_FUNCNAME, token)
                }
                val func = Func(token)
                manage.manageScopeService.registerFunc(func)
                if (token.type !== ID) {
                    token.obj = func.realName
                    token.type = ID
                }
            }
        }
        /* 声明变量名 */
        semanticAction["declear_variable"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                val spec = access.relativeGet(-1).obj as KeywordType
                val token = access.relativeGet(0)
                val name = token.toRealString()
                if (spec == KeywordType.VARIABLE) {
                    if (!manage.queryScopeService.findDeclaredSymbol(name)) {
                        if (!manage.queryScopeService.isRegisteredFunc(
                                        name)) {
                            manage.manageScopeService.registerSymbol(name)
                        } else {
                            recorder.add(SemanticError.VAR_FUN_CONFLICT, token)
                        }
                    } else if (!TokenTools.isExternalName(name)
                            && manage.queryScopeService
                                    .findDeclaredSymbolDirect(name)) {
                        recorder.add(SemanticError.VARIABLE_REDECLARAED, token)
                    }
                } else if (spec == KeywordType.LET) {
                    if (!manage.queryScopeService.findDeclaredSymbol(name)) {
                        recorder.add(SemanticError.VARIABLE_NOT_DECLARAED,
                                token)
                    }
                }
            }
        }
        /* 声明参数 */
        semanticAction["declear_param"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                val token = access.relativeGet(0)
                if (!manage.manageScopeService.registerFutureSymbol(
                                token.toRealString())) {
                    recorder.add(SemanticError.DUP_PARAM, token)
                }
            }
        }
        /* 清除参数 */
        semanticAction["func_clearargs"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                manage.manageScopeService.clearFutureArgs()
                val token = access.relativeGet(0)
                val type = token.obj as KeywordType
                if (type === KeywordType.YIELD) {
                    manage.queryBlockService.enterBlock(BlockType.kYield)
                }
            }
        }
        /* CATCH 清除参数 */
        semanticAction["clear_catch"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                manage.manageScopeService.clearFutureArgs()
            }
        }
        /* 循环体 */
        semanticAction["do_enter_cycle"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                manage.queryBlockService.enterBlock(BlockType.kCycle)
            }
        }
        /* 匿名函数处理 */
        semanticAction["lambda"] = object : ISemanticAction {
            override fun handle(indexed: IIndexedData, manage: IManageSymbol, access: IRandomAccessOfTokens, recorder: ISemanticRecorder) {
                val token = access.relativeGet(0)
                val func = Func(token)
                manage.manageScopeService.registerLambda(func)
                token.obj = func.realName
            }
        }
    }

    /**
     * 初始化语义
     */
    private fun initializeHandler() {
        /* 复制 */
        semanticAnalyzier["copy"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                return indexed[0].obj!!
            }
        }
        semanticAnalyzier["scope"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                query.manageService.manageScopeService.leaveScope()
                return indexed[0].obj!!
            }
        }
        /* 表达式 */
        semanticAnalyzier["exp"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                if (indexed.exists(2)) {// 双目运算
                    val token = indexed[2].token
                    if (token!!.type === TokenType.OPERATOR) {
                        if (token!!.obj === OperatorType.DOT && indexed[0].obj is ExpInvokeProperty) {
                            val invoke = indexed[0].obj as ExpInvokeProperty
                            invoke.obj = indexed[1].toExp()
                            return invoke
                        } else if (TokenTools.isAssignment(token!!.obj as OperatorType)) {
                            if (indexed[1].obj is ExpBinop) {
                                val bin = indexed[1].obj as ExpBinop
                                if (bin.token.obj === OperatorType.DOT) {
                                    val assign = ExpAssignProperty()
                                    assign.setToken(token)
                                    assign.obj = bin.leftOperand
                                    assign.property = bin.rightOperand
                                    assign.exp = indexed[0].toExp()
                                    if (assign.property is ExpValue && assign.exp is ExpFunc) {
                                        val v = assign.property as ExpValue
                                        val f = assign.exp as ExpFunc
                                        f.func!!.setMethodName(v.toString().replace("\"", ""))
                                    }
                                    return assign
                                }
                            } else if (indexed[1].obj is ExpIndex) {
                                val bin = indexed[1].obj as ExpIndex
                                val assign = ExpIndexAssign()
                                assign.setToken(token)
                                assign.exp = bin.exp
                                assign.index = bin.index
                                assign.obj = indexed[0].toExp()
                                return assign
                            }
                        }
                    }
                    val binop = ExpBinop(indexed[2].token!!)
                    binop.leftOperand = indexed[1].toExp()
                    binop.rightOperand = indexed[0].toExp()
                    return binop.simplify(recorder)
                } else if (indexed.exists(3)) {// 单目运算
                    val token = indexed[3].token
                    if (token!!.type === TokenType.OPERATOR) {
                        if ((token!!.obj === OperatorType.PLUS_PLUS || token!!.obj === OperatorType.MINUS_MINUS) && indexed[1].obj is ExpBinop) {
                            val bin = indexed[1].obj as ExpBinop
                            if (bin.token.obj === OperatorType.DOT) {
                                val assign = ExpAssignProperty()
                                assign.setToken(token!!)
                                assign.obj = bin.leftOperand
                                assign.property = bin.rightOperand
                                return assign
                            }
                        }
                    }
                    val sinop = ExpSinop(indexed[3].token!!, indexed[1].toExp())
                    return sinop.simplify(recorder)
                } else if (indexed.exists(4)) {// 三目运算
                    val triop = ExpTriop()
                    triop.firstToken = indexed[4].token
                    triop.secondToken = indexed[5].token
                    triop.firstOperand = indexed[0].toExp()
                    triop.secondOperand = indexed[6].toExp()
                    triop.thirdOperand = indexed[7].toExp()
                    return triop.simplify(recorder)
                } else if (indexed.exists(5)) {
                    val exp = ExpIndex()
                    exp.exp = indexed[1].toExp()
                    exp.index = indexed[5].toExp()
                    return exp
                } else if (!indexed.exists(10)) {
                    val obj = indexed[0].obj
                    if (obj is ExpValue) {
                        if (!obj.isConstant() && !query
                                        .queryScopeService
                                        .findDeclaredSymbol(
                                                obj.token.toRealString())) {
                            recorder.add(SemanticError.VARIABLE_NOT_DECLARAED,
                                    obj.token)
                        }
                    }
                    return obj!!
                } else {
                    val token = indexed[10].token
                    val num = Token()
                    if (token!!.type === TokenType.INTEGER) {
                        val n = token!!.obj as Long
                        if (n > 0L) {
                            recorder.add(SemanticError.INVALID_OPERATOR,
                                    token)
                            return indexed[0].obj!!
                        }
                        num.obj = -n
                        num.type = TokenType.INTEGER
                    } else {
                        val n = token!!.obj as Double
                        if (n > 0.0) {
                            recorder.add(SemanticError.INVALID_OPERATOR,
                                    token)
                            return indexed[0].obj!!
                        }
                        num.obj = -n
                        num.type = TokenType.DECIMAL
                    }
                    val minus = Token()
                    minus.obj = OperatorType.MINUS
                    minus.type = TokenType.OPERATOR
                    minus.position = token.position
                    val binop = ExpBinop(minus)
                    binop.leftOperand = indexed[0].toExp()
                    num.position = token.position
                    num.position.column = num.position.column + 1
                    binop.rightOperand = ExpValue(num)
                    return binop.simplify(recorder)
                }
            }
        }
        /* 基本数据结构 */
        semanticAnalyzier["type"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                if (indexed.exists(1)) {
                    return indexed[1].obj!!
                } else if (indexed.exists(2)) {
                    return indexed[2].obj!!
                } else if (indexed.exists(3)) {
                    val token = indexed[0].token!!
                    if (token.type === ID) {
                        val invoke = ExpInvoke()
                        invoke.name = token
                        val func = query.queryScopeService.getFuncByName(
                                token.toRealString())
                        if (func == null) {
                            when {
                                TokenTools.isExternalName(token) -> invoke.extern = token
                                query.queryScopeService
                                        .findDeclaredSymbol(token.toRealString()) -> {
                                    invoke.extern = token
                                    invoke.isInvoke = true
                                }
                                else -> recorder.add(SemanticError.MISSING_FUNCNAME, token)
                            }
                        } else {
                            invoke.func = func
                        }
                        if (indexed.exists(4)) {
                            invoke.params = indexed[4].toExps()
                        }
                        return invoke
                    } else {
                        val invoke = ExpInvokeProperty(token)
                        invoke.property = ExpValue(token)
                        if (indexed.exists(4)) {
                            invoke.params = indexed[4].toExps()
                        }
                        return invoke
                    }
                } else {
                    val token = indexed[0].token!!
                    return ExpValue(token)
                }
            }
        }
        /* 入口 */
        semanticAnalyzier["main"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val func = Func(query.queryScopeService.entryToken)
                func.realName = func.name.toRealString()
                val block = Block(indexed[0].toStmts())
                block.stmts.add(StmtReturn())
                func.block = block
                return func
            }
        }
        /* 块 */
        semanticAnalyzier["block"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                if (!indexed.exists(0)) {
                    return Block()
                }
                return Block(indexed[0].toStmts())
            }
        }
        /* 语句集合 */
        semanticAnalyzier["stmt_list"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val stmts = if (indexed.exists(1)) {
                    indexed[1].toStmts()
                } else {
                    mutableListOf()
                }
                stmts.add(0, indexed[0].toStmt())
                return stmts
            }
        }
        /* 变量定义 */
        semanticAnalyzier["var"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val assign = ExpAssign()
                val token = indexed[0].token
                assign.name = token
                if (indexed.exists(11)) {
                    assign.isDecleared = true
                }
                if (indexed.exists(1)) {
                    val func = ExpFunc()
                    func.func = indexed[1].toFunc()
                    func.genClosure()
                    if (assign.isDecleared) {
                        val funcName = func.func!!.realName
                        if (!query.queryScopeService.isLambda(funcName) && funcName != token!!.toRealString()) {
                            recorder.add(SemanticError.DIFFERENT_FUNCNAME, token)
                        }
                        func.func!!.realName = token!!.toRealString()
                    }
                    assign.exp = func
                } else if (indexed.exists(2)) {
                    assign.exp = indexed[2].toExp()
                } else {
                    if (!assign.isDecleared) {
                        recorder.add(SemanticError.INVALID_ASSIGNMENT, token!!)
                    }
                }
                return assign
            }
        }
        /* 属性设置 */
        semanticAnalyzier["set"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val assign = ExpAssignProperty()
                assign.obj = indexed[3].toExp()
                assign.property = indexed[4].toExp()
                assign.exp = indexed[2].toExp()
                return assign
            }
        }
        /* 调用表达式 */
        semanticAnalyzier["call_exp"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val invoke = ExpInvoke()
                if (indexed.exists(1)) {
                    val token = indexed[1].token
                    invoke.name = token
                    val func = query.queryScopeService.getFuncByName(
                            token!!.toRealString())
                    if (func == null) {
                        when {
                            TokenTools.isExternalName(token) -> invoke.extern = token
                            query.queryScopeService
                                    .findDeclaredSymbol(token.toRealString()) -> {
                                invoke.extern = token
                                invoke.isInvoke = true
                            }
                            else -> recorder.add(SemanticError.MISSING_FUNCNAME, token)
                        }
                    } else {
                        invoke.func = func
                    }
                } else if (indexed.exists(3)) {
                    val exp = indexed[3].obj as IExp
                    if (exp is ExpInvoke) {
                        invoke.invokeExp = exp
                    } else {
                        recorder.add(SemanticError.INVALID_FUNCNAME, indexed[4].token!!)
                    }
                } else {
                    invoke.func = indexed[0].toFunc()
                    invoke.name = invoke.func!!.name
                }
                if (indexed.exists(2)) {
                    invoke.params = indexed[2].toExps()
                }
                return invoke
            }
        }
        /* 类方法调用表达式 */
        semanticAnalyzier["invoke"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val invoke = ExpInvokeProperty(indexed[0].token!!)
                invoke.obj = indexed[1].toExp()
                invoke.property = indexed[2].toExp()
                if (indexed.exists(3)) {
                    invoke.params = indexed[3].toExps()
                }
                return invoke
            }
        }
        /* 单词集合 */
        semanticAnalyzier["token_list"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val tokens = if (indexed.exists(1)) {
                    indexed[1].toTokens()
                } else {
                    mutableListOf()
                }
                tokens.add(0, indexed[0].token!!)
                return tokens
            }
        }
        /* 表达式集合 */
        semanticAnalyzier["exp_list"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val exps = if (indexed.exists(1)) {
                    indexed[1].toExps()
                } else {
                    mutableListOf()
                }
                exps.add(0, indexed[0].obj as IExp)
                return exps
            }
        }
        /* 过程 */
        semanticAnalyzier["func"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val token = indexed[1].token
                val func = query.queryScopeService.getFuncByName(
                        token!!.toRealString())
                if (!indexed.exists(10)) {
                    func!!.isYield = true
                    query.queryBlockService.leaveBlock(BlockType.kYield)
                }
                if (indexed.exists(2)) {
                    func!!.params = indexed[2].toTokens()
                }
                if (indexed.exists(0)) {
                    func!!.setDoc(indexed[0].toTokens())
                }

                val ret = StmtReturn()
                if (func!!.isYield) {
                    ret.isYield = true
                }
                if (indexed.exists(3)) {
                    val stmts = mutableListOf<IStmt>()
                    ret.exp = indexed[3].toExp()
                    stmts.add(ret)
                    val block = Block(stmts)
                    func.block = block
                } else {
                    val block = indexed[4].obj as Block
                    val stmts = block.stmts
                    if (func.isYield) {
                        if (stmts.isEmpty()) {
                            stmts.add(ret)
                        } else if (stmts[stmts.size - 1] is StmtReturn) {
                            val preRet = stmts[stmts.size - 1] as StmtReturn
                            if (preRet.exp != null) {
                                stmts.add(ret)
                            }
                        } else {
                            stmts.add(ret)
                        }
                    } else if (stmts.isEmpty() || stmts[stmts.size - 1] !is StmtReturn) {
                        stmts.add(ret)
                    }
                    func.block = block
                }
                return func
            }
        }
        /* 匿名函数 */
        semanticAnalyzier["lambda"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val token = indexed[1].token!!
                val func = query.queryScopeService.lambda
                func.name = token
                if (indexed.exists(2)) {
                    func.params = indexed[2].toTokens()
                }

                val ret = StmtReturn()
                if (indexed.exists(3)) {
                    val stmts = mutableListOf<IStmt>()
                    ret.exp = indexed[3].toExp()
                    stmts.add(ret)
                    val block = Block(stmts)
                    func.block = block
                } else {
                    val block = indexed[4].obj as Block
                    val stmts = block.stmts
                    if (stmts.isEmpty() || stmts[stmts.size - 1] !is StmtReturn)
                        stmts.add(ret)
                    func.block = block
                }
                query.manageService.manageScopeService.clearFutureArgs()
                val exp = ExpFunc()
                exp.func = func
                exp.genClosure()
                return exp
            }
        }
        /* 返回语句 */
        semanticAnalyzier["return"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val ret = StmtReturn()
                if (indexed.exists(0)) {
                    ret.exp = indexed[0].toExp()
                }
                if (query.queryBlockService.isInBlock(BlockType.kYield)) {
                    ret.isYield = true
                }
                return ret
            }
        }
        /* 导入/导出 */
        semanticAnalyzier["port"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val port = StmtPort()
                val token = indexed[0].token
                port.name = token
                if (!indexed.exists(1)) {
                    port.isImported = false
                    val func = query.queryScopeService
                            .getFuncByName(token!!.obj!!.toString())
                    if (func == null) {
                        recorder.add(SemanticError.WRONG_EXTERN_SYMBOL, token)
                    } else {
                        func.isExtern = true
                    }
                }
                return port
            }
        }
        /* 表达式语句 */
        semanticAnalyzier["stmt_exp"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val exp = StmtExp()
                if (indexed.exists(0)) {
                    exp.exp = indexed[0].toExp()
                }
                return exp
            }
        }
        /* 条件语句 */
        semanticAnalyzier["if"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val stmt = StmtIf()
                stmt.exp = indexed[0].toExp()
                stmt.trueBlock = indexed[1].toBlock()
                if (indexed.exists(2)) {
                    stmt.falseBlock = indexed[2].toBlock()
                } else if (indexed.exists(3)) {
                    val block = Block()
                    block.stmts.add(indexed[3].obj as IStmt)
                    stmt.falseBlock = block
                }
                return stmt
            }
        }
        /* 循环语句 */
        semanticAnalyzier["for"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                query.queryBlockService.leaveBlock(BlockType.kCycle)
                val stmt = StmtFor()
                if (indexed.exists(0)) {
                    stmt.variable = indexed[0].toExp()
                }
                if (indexed.exists(1)) {
                    stmt.cond = indexed[1].toExp()
                }
                if (indexed.exists(2)) {
                    stmt.ctrl = indexed[2].toExp()
                }
                stmt.block = indexed[3].toBlock()
                return stmt
            }
        }
        semanticAnalyzier["while"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                query.queryBlockService.leaveBlock(BlockType.kCycle)
                val stmt = StmtWhile()
                stmt.cond = indexed[0].toExp()
                stmt.block = indexed[1].toBlock()
                return stmt
            }
        }
        semanticAnalyzier["foreach"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                query.queryBlockService.leaveBlock(BlockType.kCycle)
                val stmt = StmtForeach()
                stmt.variable = indexed[0].token
                stmt.enumerator = indexed[1].toExp()
                stmt.block = indexed[2].toBlock()
                if (!stmt.enumerator!!.isEnumerable()) {
                    recorder.add(SemanticError.WRONG_ENUMERABLE, stmt.variable!!)
                }
                stmt.enumerator!!.setYield()
                return stmt
            }
        }
        /* 循环控制表达式 */
        semanticAnalyzier["cycle"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val exp = ExpCycleCtrl()
                if (indexed.exists(0)) {
                    exp.name = indexed[0].token
                }
                if (!query.queryBlockService.isInBlock(BlockType.kCycle)) {
                    recorder.add(SemanticError.WRONG_CYCLE, exp.name!!)
                }
                return exp
            }
        }
        /* 块语句 */
        semanticAnalyzier["block_stmt"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val block = StmtBlock()
                block.block = indexed[0].toBlock()
                return block
            }
        }
        /* 数组 */
        semanticAnalyzier["array"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val exp = ExpArray()
                if (indexed.exists(0)) {
                    exp.setParams(indexed[0].toExps())
                }
                return exp
            }
        }
        /* 字典 */
        semanticAnalyzier["map_list"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val exps = if (indexed.exists(0)) {
                    indexed[0].toExps()
                } else {
                    mutableListOf()
                }

                val binop = ExpBinop(indexed[3].token!!)
                val value = ExpValue(indexed[1].token!!)
                binop.leftOperand = value
                binop.rightOperand = indexed[2].toExp()
                exps.add(0, binop.simplify(recorder))
                return exps
            }
        }
        semanticAnalyzier["map"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val exp = ExpMap()
                if (indexed.exists(0)) {
                    exp.params = indexed[0].toExps()
                }
                return exp
            }
        }
        /* 异常处理 */
        semanticAnalyzier["try"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any {
                val stmt = StmtTry(indexed[1].toBlock(), indexed[2].toBlock())
                if (indexed.exists(0)) {
                    stmt.token = indexed[0].token
                }
                return stmt
            }
        }
        semanticAnalyzier["throw"] = object : ISemanticAnalyzer {
            override fun handle(indexed: IIndexedData, query: IQuerySymbol, recorder: ISemanticRecorder): Any =
                    StmtThrow(indexed[0].toExp())
        }
    }

    /**
     * 获得语义分析动作
     *
     * @param name 语义分析动作名称
     * @return 语义分析动作
     */
    fun getSemanticHandler(name: String): ISemanticAnalyzer {
        return semanticAnalyzier[name]!!
    }

    /**
     * 获得语义执行动作
     *
     * @param name 语义执行动作名称
     * @return 语义执行动作
     */
    fun getActionHandler(name: String): ISemanticAction {
        return semanticAction[name]!!
    }
}
