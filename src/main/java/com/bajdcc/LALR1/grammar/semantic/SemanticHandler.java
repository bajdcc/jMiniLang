package com.bajdcc.LALR1.grammar.semantic;

import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import com.bajdcc.LALR1.grammar.symbol.BlockType;
import com.bajdcc.LALR1.grammar.tree.*;
import com.bajdcc.LALR1.grammar.type.TokenTools;
import com.bajdcc.LALR1.semantic.token.ISemanticAction;
import com.bajdcc.LALR1.semantic.token.ISemanticAnalyzer;
import com.bajdcc.util.lexer.token.KeywordType;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bajdcc.util.lexer.token.TokenType.ID;

/**
 * 【语义分析】语义处理器集合
 *
 * @author bajdcc
 */
public class SemanticHandler {

	/**
	 * 语义分析动作映射表
	 */
	private HashMap<String, ISemanticAnalyzer> mapSemanticAnalyzier = new HashMap<>();

	/**
	 * 语义执行动作映射表
	 */
	private HashMap<String, ISemanticAction> mapSemanticAction = new HashMap<>();

	public SemanticHandler() {
		initializeAction();
		initializeHandler();
	}

	/**
	 * 初始化动作
	 */
	private void initializeAction() {
		/* 进入块 */
		mapSemanticAction.put("do_enter_scope", (indexed, manage, access, recorder) -> manage.getManageScopeService().enterScope());
		/* 离开块 */
		mapSemanticAction.put("do_leave_scope", (indexed, manage, access, recorder) -> manage.getManageScopeService().leaveScope());
		/* 声明过程名 */
		mapSemanticAction.put("predeclear_funcname", (indexed, manage, access, recorder) -> {
			Token token = access.relativeGet(0);
			String funcName = token.toRealString();
			if (token.kToken == ID) {
				if (manage.getQueryScopeService().getEntryName()
						.equals(funcName)) {
					recorder.add(SemanticError.DUP_ENTRY, token);
				} else if (manage.getQueryScopeService().isRegisteredFunc(
						funcName)) {
					recorder.add(SemanticError.DUP_FUNCNAME, token);
				}
			}
			Function func = new Function();
			func.setName(token);
			manage.getManageScopeService().registerFunc(func);
			if (token.kToken != ID) {
				token.object = func.getRealName();
				token.kToken = ID;
			}
		});
		/* 声明变量名 */
		mapSemanticAction.put("declear_variable", (indexed, manage, access, recorder) -> {
			KeywordType spec = (KeywordType) access.relativeGet(-1).object;
			Token token = access.relativeGet(0);
			String name = token.toRealString();
			if (spec == KeywordType.VARIABLE) {
				if (!manage.getQueryScopeService().findDeclaredSymbol(name)) {
					if (!manage.getQueryScopeService().isRegisteredFunc(
							name)) {
						manage.getManageScopeService().registerSymbol(name);
					} else {
						recorder.add(SemanticError.VAR_FUN_CONFLICT, token);
					}
				} else if (!TokenTools.isExternalName(name)
						&& manage.getQueryScopeService()
						.findDeclaredSymbolDirect(name)) {
					recorder.add(SemanticError.VARIABLE_REDECLARAED, token);
				}
			} else if (spec == KeywordType.LET) {
				if (!manage.getQueryScopeService().findDeclaredSymbol(name)) {
					recorder.add(SemanticError.VARIABLE_NOT_DECLARAED,
							token);
				}
			}
		});
		/* 声明参数 */
		mapSemanticAction.put("declear_param", (indexed, manage, access, recorder) -> {
			Token token = access.relativeGet(0);
			if (!manage.getManageScopeService().registerFutureSymbol(
					token.toRealString())) {
				recorder.add(SemanticError.DUP_PARAM, token);
			}
		});
		/* 清除参数 */
		mapSemanticAction.put("func_clearargs", (indexed, manage, access, recorder) -> {
			manage.getManageScopeService().clearFutureArgs();
			Token token = access.relativeGet(0);
			KeywordType type = (KeywordType) token.object;
			if (type == KeywordType.YIELD) {
				manage.getQueryBlockService().enterBlock(BlockType.kYield);
			}
		});
		/* CATCH 清除参数 */
		mapSemanticAction.put("clear_catch", (indexed, manage, access, recorder) -> manage.getManageScopeService().clearFutureArgs());
		/* 循环体 */
		mapSemanticAction.put("do_enter_cycle", (indexed, manage, access, recorder) -> manage.getQueryBlockService().enterBlock(BlockType.kCycle));
		/* 匿名函数处理 */
		mapSemanticAction.put("lambda", (indexed, manage, access, recorder) -> {
			Token token = access.relativeGet(0);
			Function func = new Function();
			func.setName(token);
			manage.getManageScopeService().registerLambda(func);
			token.object = func.getRealName();
		});
	}

	/**
	 * 初始化语义
	 */
	private void initializeHandler() {
		/* 复制 */
		mapSemanticAnalyzier.put("copy", (indexed, query, recorder) -> indexed.get(0).getObject());
		/* 表达式 */
		mapSemanticAnalyzier.put("exp", (indexed, query, recorder) -> {
			if (indexed.exists(2)) {// 双目运算
				Token token = indexed.get(2).getToken();
				if (token.kToken == TokenType.OPERATOR) {
					if (token.object == OperatorType.DOT && indexed.get(0).getObject() instanceof ExpInvokeProperty) {
						ExpInvokeProperty invoke = (ExpInvokeProperty) indexed.get(0).getObject();
						invoke.setObj((IExp) indexed.get(1).getObject());
						return invoke;
					} else if (TokenTools.isAssignment((OperatorType) token.object)) {
						if (indexed.get(1).getObject() instanceof ExpBinop) {
							ExpBinop bin = (ExpBinop) indexed.get(1).getObject();
							if (bin.getToken().object == OperatorType.DOT) {
								ExpAssignProperty assign = new ExpAssignProperty();
								assign.setToken(token);
								assign.setObj(bin.getLeftOperand());
								assign.setProperty(bin.getRightOperand());
								assign.setExp((IExp) indexed.get(0).getObject());
								return assign;
							}
						} else if (indexed.get(1).getObject() instanceof ExpIndex) {
							ExpIndex bin = (ExpIndex) indexed.get(1).getObject();
							ExpIndexAssign assign = new ExpIndexAssign();
							assign.setToken(token);
							assign.setExp(bin.getExp());
							assign.setIndex(bin.getIndex());
							assign.setObj((IExp) indexed.get(0).getObject());
							return assign;
						}
					}
				}
				ExpBinop binop = new ExpBinop();
				binop.setToken(indexed.get(2).getToken());
				binop.setLeftOperand((IExp) indexed.get(1).getObject());
				binop.setRightOperand((IExp) indexed.get(0).getObject());
				return binop.simplify(recorder);
			} else if (indexed.exists(3)) {// 单目运算
				Token token = indexed.get(3).getToken();
				if (token.kToken == TokenType.OPERATOR) {
					if ((token.object == OperatorType.PLUS_PLUS || token.object == OperatorType.MINUS_MINUS)
							&& indexed.get(1).getObject() instanceof ExpBinop) {
						ExpBinop bin = (ExpBinop) indexed.get(1).getObject();
						if (bin.getToken().object == OperatorType.DOT) {
							ExpAssignProperty assign = new ExpAssignProperty();
							assign.setToken(token);
							assign.setObj(bin.getLeftOperand());
							assign.setProperty(bin.getRightOperand());
							return assign;
						}
					}
				}
				ExpSinop sinop = new ExpSinop();
				sinop.setToken(indexed.get(3).getToken());
				sinop.setOperand((IExp) indexed.get(1).getObject());
				return sinop.simplify(recorder);
			} else if (indexed.exists(4)) {// 三目运算
				ExpTriop triop = new ExpTriop();
				triop.setFirstToken(indexed.get(4).getToken());
				triop.setSecondToken(indexed.get(5).getToken());
				triop.setFirstOperand((IExp) indexed.get(0).getObject());
				triop.setSecondOperand((IExp) indexed.get(6).getObject());
				triop.setThirdOperand((IExp) indexed.get(7).getObject());
				return triop.simplify(recorder);
			} else if (indexed.exists(5)) {
				ExpIndex exp = new ExpIndex();
				exp.setExp((IExp) indexed.get(1).getObject());
				exp.setIndex((IExp) indexed.get(5).getObject());
				return exp;
			} else if (!indexed.exists(10)) {
				Object obj = indexed.get(0).getObject();
				if (obj instanceof ExpValue) {
					ExpValue value = (ExpValue) obj;
					if (!value.isConstant()
							&& !query
							.getQueryScopeService()
							.findDeclaredSymbol(
									value.getToken().toRealString())) {
						recorder.add(SemanticError.VARIABLE_NOT_DECLARAED,
								value.getToken());
					}
				}
				return obj;
			} else {
				Token token = indexed.get(10).getToken();
				Token num = new Token();
				if (token.kToken == TokenType.INTEGER) {
					BigInteger n = (BigInteger) token.object;
					if (n.signum() != -1) {
						recorder.add(SemanticError.INVALID_OPERATOR,
								token);
						return indexed.get(0).getObject();
					}
					num.object = n.negate();
					num.kToken = TokenType.INTEGER;
				} else {
					BigDecimal n = (BigDecimal) token.object;
					if (n.signum() != -1) {
						recorder.add(SemanticError.INVALID_OPERATOR,
								token);
						return indexed.get(0).getObject();
					}
					num.object = n.negate();
					num.kToken = TokenType.DECIMAL;
				}
				ExpBinop binop = new ExpBinop();
				Token minus = new Token();
				minus.object = OperatorType.MINUS;
				minus.kToken = TokenType.OPERATOR;
				minus.position = token.position;
				binop.setToken(minus);
				binop.setLeftOperand((IExp) indexed.get(0).getObject());
				num.position = token.position;
				num.position.iColumn++;
				ExpValue value = new ExpValue();
				value.setToken(num);
				binop.setRightOperand(value);
				return binop.simplify(recorder);
			}
		});
		/* 基本数据结构 */
		mapSemanticAnalyzier.put("type", (indexed, query, recorder) -> {
			if (indexed.exists(1)) {
				return indexed.get(1).getObject();
			} else if (indexed.exists(2)) {
				return indexed.get(2).getObject();
			} else if (indexed.exists(3)) {
				Token token = indexed.get(0).getToken();
				if (token.kToken == ID) {
					ExpInvoke invoke = new ExpInvoke();
					invoke.setName(token);
					Function func = query.getQueryScopeService().getFuncByName(
							token.toRealString());
					if (func == null) {
						if (TokenTools.isExternalName(token)) {
							invoke.setExtern(token);
						} else if (query.getQueryScopeService()
								.findDeclaredSymbol(token.toRealString())) {
							invoke.setExtern(token);
							invoke.setInvoke(true);
						} else {
							recorder.add(SemanticError.MISSING_FUNCNAME, token);
						}
					} else {
						invoke.setFunc(func);
					}
					if (indexed.exists(4)) {
						invoke.setParams((List<IExp>) indexed.get(4).getObject());
					}
					return invoke;
				} else {
					ExpInvokeProperty invoke = new ExpInvokeProperty();
					invoke.setToken(token);
					ExpValue value = new ExpValue();
					value.setToken(token);
					invoke.setProperty(value);
					if (indexed.exists(4)) {
						invoke.setParams((List<IExp>) indexed.get(4).getObject());
					}
					return invoke;
				}
			} else {
				ExpValue value = new ExpValue();
				Token token = indexed.get(0).getToken();
				value.setToken(token);
				return value;
			}
		});
		/* 入口 */
		mapSemanticAnalyzier.put("main", (indexed, query, recorder) -> {
			Function func = new Function();
			func.setName(query.getQueryScopeService().getEntryToken());
			func.setRealName(func.getName().toRealString());
			Block block = new Block((List<IStmt>) indexed.get(0).getObject());
			block.getStmts().add(new StmtReturn());
			func.setBlock(block);
			return func;
		});
		/* 块 */
		mapSemanticAnalyzier.put("block", (indexed, query, recorder) -> {
			if (!indexed.exists(0)) {
				return new Block();
			}
			return new Block((List<IStmt>) indexed.get(0).getObject());
		});
		/* 语句集合 */
		mapSemanticAnalyzier.put("stmt_list", (indexed, query, recorder) -> {
			List<IStmt> stmts;
			if (indexed.exists(1)) {
				stmts = (List<IStmt>) indexed.get(1).getObject();
			} else {
				stmts = new ArrayList<>();
			}
			stmts.add(0, (IStmt) indexed.get(0).getObject());
			return stmts;
		});
		/* 变量定义 */
		mapSemanticAnalyzier.put("var", (indexed, query, recorder) -> {
			ExpAssign assign = new ExpAssign();
			Token token = indexed.get(0).getToken();
			assign.setName(token);
			if (indexed.exists(11)) {
				assign.setDecleared(true);
			}
			if (indexed.exists(1)) {
				ExpFunc func = new ExpFunc();
				func.setFunc((Function) indexed.get(1).getObject());
				func.genClosure();
				if (assign.isDecleared()) {
					String funcName = func.getFunc().getRealName();
					if (!query.getQueryScopeService().isLambda(funcName) && !funcName.equals(token.toRealString())) {
						recorder.add(SemanticError.DIFFERENT_FUNCNAME, token);
					}
					func.getFunc().setRealName(token.toRealString());
				}
				assign.setExp(func);
			} else {
				assign.setExp((IExp) indexed.get(2).getObject());
			}
			return assign;
		});
		/* 属性设置 */
		mapSemanticAnalyzier.put("set", (indexed, query, recorder) -> {
			ExpAssignProperty assign = new ExpAssignProperty();
			assign.setObj((IExp) indexed.get(3).getObject());
			assign.setProperty((IExp) indexed.get(4).getObject());
			assign.setExp((IExp) indexed.get(2).getObject());
			return assign;
		});
		/* 调用表达式 */
		mapSemanticAnalyzier.put("call_exp", (indexed, query, recorder) -> {
			ExpInvoke invoke = new ExpInvoke();
			if (indexed.exists(1)) {
				Token token = indexed.get(1).getToken();
				invoke.setName(token);
				Function func = query.getQueryScopeService().getFuncByName(
						token.toRealString());
				if (func == null) {
					if (TokenTools.isExternalName(token)) {
						invoke.setExtern(token);
					} else if (query.getQueryScopeService()
							.findDeclaredSymbol(token.toRealString())) {
						invoke.setExtern(token);
						invoke.setInvoke(true);
					} else {
						recorder.add(SemanticError.MISSING_FUNCNAME, token);
					}
				} else {
					invoke.setFunc(func);
				}
			} else if (indexed.exists(3)) {
				IExp exp = (IExp) indexed.get(3).getObject();
				if (exp instanceof ExpInvoke) {
					ExpInvoke invokeExp = (ExpInvoke) exp;
					invoke.setInvokeExp(invokeExp);
				} else {
					recorder.add(SemanticError.INVALID_FUNCNAME, indexed.get(4).getToken());
				}
			} else {
				invoke.setFunc((Function) indexed.get(0).getObject());
				invoke.setName(invoke.getFunc().getName());
			}
			if (indexed.exists(2)) {
				invoke.setParams((List<IExp>) indexed.get(2).getObject());
			}
			return invoke;
		});
		/* 类方法调用表达式 */
		mapSemanticAnalyzier.put("invoke", (indexed, query, recorder) -> {
			ExpInvokeProperty invoke = new ExpInvokeProperty();
			invoke.setToken(indexed.get(0).getToken());
			invoke.setObj((IExp) indexed.get(1).getObject());
			invoke.setProperty((IExp) indexed.get(2).getObject());
			if (indexed.exists(3)) {
				invoke.setParams((List<IExp>) indexed.get(3).getObject());
			}
			return invoke;
		});
		/* 单词集合 */
		mapSemanticAnalyzier.put("token_list", (indexed, query, recorder) -> {
			List<Token> tokens;
			if (indexed.exists(1)) {
				tokens = (List<Token>) indexed.get(1).getObject();
			} else {
				tokens = new ArrayList<>();
			}
			tokens.add(0, indexed.get(0).getToken());
			return tokens;
		});
		/* 表达式集合 */
		mapSemanticAnalyzier.put("exp_list", (indexed, query, recorder) -> {
			List<IExp> exps;
			if (indexed.exists(1)) {
				exps = (List<IExp>) indexed.get(1).getObject();
			} else {
				exps = new ArrayList<>();
			}
			exps.add(0, (IExp) indexed.get(0).getObject());
			return exps;
		});
		/* 过程 */
		mapSemanticAnalyzier.put("func", (indexed, query, recorder) -> {
			Token token = indexed.get(1).getToken();
			Function func = query.getQueryScopeService().getFuncByName(
					token.toRealString());
			if (!indexed.exists(10)) {
				func.setYield(true);
				query.getQueryBlockService().leaveBlock(BlockType.kYield);
			}
			if (indexed.exists(2)) {
				func.setParams((List<Token>) indexed.get(2).getObject());
			}
			if (indexed.exists(0)) {
				func.setDoc((List<Token>) indexed.get(0).getObject());
			}
			StmtReturn ret = new StmtReturn();
			if (func.isYield()) {
				ret.setYield(true);
			}
			if (indexed.exists(3)) {
				List<IStmt> stmts = new ArrayList<>();
				ret.setExp((IExp) indexed.get(3).getObject());
				stmts.add(ret);
				Block block = new Block(stmts);
				func.setBlock(block);
			} else {
				Block block = (Block) indexed.get(4).getObject();
				List<IStmt> stmts = block.getStmts();
				if (func.isYield()) {
					if (stmts.isEmpty()) {
						stmts.add(ret);
					} else if (stmts.get(stmts.size() - 1) instanceof StmtReturn) {
						StmtReturn preRet = (StmtReturn) stmts.get(stmts.size() - 1);
						if (preRet.getExp() != null) {
							stmts.add(ret);
						}
					} else {
						stmts.add(ret);
					}
 				} else if (stmts.isEmpty() || !(stmts.get(stmts.size() - 1) instanceof StmtReturn)) {
					stmts.add(ret);
				}
				func.setBlock(block);
			}
			return func;
		});
		/* 匿名函数 */
		mapSemanticAnalyzier.put("lambda", (indexed, query, recorder) -> {
			Token token = indexed.get(1).getToken();
			Function func = query.getQueryScopeService().getLambda();
			if (indexed.exists(2)) {
				func.setParams((List<Token>) indexed.get(2).getObject());
			}
			StmtReturn ret = new StmtReturn();
			if (indexed.exists(3)) {
				List<IStmt> stmts = new ArrayList<>();
				ret.setExp((IExp) indexed.get(3).getObject());
				stmts.add(ret);
				Block block = new Block(stmts);
				func.setBlock(block);
			} else {
				Block block = (Block) indexed.get(4).getObject();
				List<IStmt> stmts = block.getStmts();
				if (stmts.isEmpty() || !(stmts.get(stmts.size() - 1) instanceof StmtReturn))
					stmts.add(ret);
				func.setBlock(block);
			}
			query.getManageService().getManageScopeService().clearFutureArgs();
			ExpFunc exp = new ExpFunc();
			exp.setFunc(func);
			exp.genClosure();
			return exp;
		});
		/* 返回语句 */
		mapSemanticAnalyzier.put("return", (indexed, query, recorder) -> {
			StmtReturn ret = new StmtReturn();
			if (indexed.exists(0)) {
				ret.setExp((IExp) indexed.get(0).getObject());
			}
			if (query.getQueryBlockService().isInBlock(BlockType.kYield)) {
				ret.setYield(true);
			}
			return ret;
		});
		/* 导入/导出 */
		mapSemanticAnalyzier.put("port", (indexed, query, recorder) -> {
			StmtPort port = new StmtPort();
			Token token = indexed.get(0).getToken();
			port.setName(token);
			if (!indexed.exists(1)) {
				port.setImported(false);
				Function func = query.getQueryScopeService()
						.getFuncByName(token.object.toString());
				if (func == null) {
					recorder.add(SemanticError.WRONG_EXTERN_SYMBOL, token);
				} else {
					func.setExtern(true);
				}
			}
			return port;
		});
		/* 表达式语句 */
		mapSemanticAnalyzier.put("stmt_exp", (indexed, query, recorder) -> {
			StmtExp exp = new StmtExp();
			if (indexed.exists(0)) {
				exp.setExp((IExp) indexed.get(0).getObject());
			}
			return exp;
		});
		/* 条件语句 */
		mapSemanticAnalyzier.put("if", (indexed, query, recorder) -> {
			StmtIf stmt = new StmtIf();
			stmt.setExp((IExp) indexed.get(0).getObject());
			stmt.setTrueBlock((Block) indexed.get(1).getObject());
			if (indexed.exists(2)) {
				stmt.setFalseBlock((Block) indexed.get(2).getObject());
			} else if (indexed.exists(3)) {
				Block block = new Block();
				block.getStmts().add((IStmt) indexed.get(3).getObject());
				stmt.setFalseBlock(block);
			}
			return stmt;
		});
		/* 循环语句 */
		mapSemanticAnalyzier.put("for", (indexed, query, recorder) -> {
			query.getQueryBlockService().leaveBlock(BlockType.kCycle);
			StmtFor stmt = new StmtFor();
			if (indexed.exists(0)) {
				stmt.setVar((IExp) indexed.get(0).getObject());
			}
			if (indexed.exists(1)) {
				stmt.setCond((IExp) indexed.get(1).getObject());
			}
			if (indexed.exists(2)) {
				stmt.setCtrl((IExp) indexed.get(2).getObject());
			}
			stmt.setBlock((Block) indexed.get(3).getObject());
			return stmt;
		});
		mapSemanticAnalyzier.put("while", (indexed, query, recorder) -> {
			query.getQueryBlockService().leaveBlock(BlockType.kCycle);
			StmtWhile stmt = new StmtWhile();
			stmt.setCond((IExp) indexed.get(0).getObject());
			stmt.setBlock((Block) indexed.get(1).getObject());
			return stmt;
		});
		mapSemanticAnalyzier.put("foreach", (indexed, query, recorder) -> {
			query.getQueryBlockService().leaveBlock(BlockType.kCycle);
			StmtForeach stmt = new StmtForeach();
			stmt.setVar(indexed.get(0).getToken());
			stmt.setEnumerator((IExp) indexed.get(1).getObject());
			stmt.setBlock((Block) indexed.get(2).getObject());
			if (!stmt.getEnumerator().isEnumerable()) {
				recorder.add(SemanticError.WRONG_ENUMERABLE, stmt.getVar());
			}
			stmt.getEnumerator().setYield();
			return stmt;
		});
		/* 循环控制表达式 */
		mapSemanticAnalyzier.put("cycle", (indexed, query, recorder) -> {
			ExpCycleCtrl exp = new ExpCycleCtrl();
			if (indexed.exists(0)) {
				exp.setName(indexed.get(0).getToken());
			}
			if (!query.getQueryBlockService().isInBlock(BlockType.kCycle)) {
				recorder.add(SemanticError.WRONG_CYCLE, exp.getName());
			}
			return exp;
		});
		/* 块语句 */
		mapSemanticAnalyzier.put("block_stmt", (indexed, query, recorder) -> {
			StmtBlock block = new StmtBlock();
			block.setBlock((Block) indexed.get(0).getObject());
			return block;
		});
		/* 数组 */
		mapSemanticAnalyzier.put("array", (indexed, query, recorder) -> {
			ExpArray exp = new ExpArray();
			if (indexed.exists(0)) {
				exp.setParams((List<IExp>) indexed.get(0).getObject());
			}
			return exp;
		});
		/* 字典 */
		mapSemanticAnalyzier.put("map_list", (indexed, query, recorder) -> {
			List<IExp> exps;
			if (indexed.exists(0)) {
				exps = (List<IExp>) indexed.get(0).getObject();
			} else {
				exps = new ArrayList<>();
			}
			ExpBinop binop = new ExpBinop();
			binop.setToken(indexed.get(3).getToken());
			ExpValue value = new ExpValue();
			value.setToken(indexed.get(1).getToken());
			binop.setLeftOperand(value);
			binop.setRightOperand((IExp) indexed.get(2).getObject());
			exps.add(0, binop.simplify(recorder));
			return exps;
		});
		mapSemanticAnalyzier.put("map", (indexed, query, recorder) -> {
			ExpMap exp = new ExpMap();
			if (indexed.exists(0)) {
				exp.setParams((List<IExp>) indexed.get(0).getObject());
			}
			return exp;
		});
		/* 异常处理 */
		mapSemanticAnalyzier.put("try", (indexed, query, recorder) -> {
			StmtTry stmt = new StmtTry();
			stmt.setTryBlock((Block) indexed.get(1).getObject());
			stmt.setCatchBlock((Block) indexed.get(2).getObject());
			if (indexed.exists(0)) {
				stmt.setToken(indexed.get(0).getToken());
			}
			return stmt;
		});
		mapSemanticAnalyzier.put("throw", (indexed, query, recorder) -> {
			StmtThrow stmt = new StmtThrow();
			stmt.setExp((IExp) indexed.get(0).getObject());
			return stmt;
		});
	}

	/**
	 * 获得语义分析动作
	 *
	 * @param name 语义分析动作名称
	 * @return 语义分析动作
	 */
	public ISemanticAnalyzer getSemanticHandler(String name) {
		ISemanticAnalyzer obj = mapSemanticAnalyzier.get(name);
		if (obj == null) {
			throw new NullPointerException(name);
		}
		return obj;
	}

	/**
	 * 获得语义执行动作
	 *
	 * @param name 语义执行动作名称
	 * @return 语义执行动作
	 */
	public ISemanticAction getActionHandler(String name) {
		ISemanticAction obj = mapSemanticAction.get(name);
		if (obj == null) {
			throw new NullPointerException(name);
		}
		return obj;
	}
}
