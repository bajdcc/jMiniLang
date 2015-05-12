package priv.bajdcc.LALR1.grammar.semantic;

import java.util.ArrayList;
import java.util.HashMap;

import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.LALR1.grammar.symbol.IManageSymbol;
import priv.bajdcc.LALR1.grammar.symbol.IQuerySymbol;
import priv.bajdcc.LALR1.grammar.tree.Block;
import priv.bajdcc.LALR1.grammar.tree.ExpBinop;
import priv.bajdcc.LALR1.grammar.tree.ExpFunc;
import priv.bajdcc.LALR1.grammar.tree.ExpInvoke;
import priv.bajdcc.LALR1.grammar.tree.ExpSinop;
import priv.bajdcc.LALR1.grammar.tree.ExpTriop;
import priv.bajdcc.LALR1.grammar.tree.ExpValue;
import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.LALR1.grammar.tree.IExp;
import priv.bajdcc.LALR1.grammar.tree.IStmt;
import priv.bajdcc.LALR1.grammar.tree.StmtAssign;
import priv.bajdcc.LALR1.grammar.tree.StmtInvoke;
import priv.bajdcc.LALR1.grammar.tree.StmtPort;
import priv.bajdcc.LALR1.grammar.tree.StmtReturn;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.LALR1.semantic.token.IIndexedData;
import priv.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens;
import priv.bajdcc.LALR1.semantic.token.ISemanticAction;
import priv.bajdcc.LALR1.semantic.token.ISemanticAnalyzier;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】语义处理器集合
 *
 * @author bajdcc
 */
public class SemanticHandler {

	/**
	 * 语义分析动作映射表
	 */
	private HashMap<String, ISemanticAnalyzier> mapSemanticAnalyzier = new HashMap<String, ISemanticAnalyzier>();

	/**
	 * 语义执行动作映射表
	 */
	private HashMap<String, ISemanticAction> mapSemanticAction = new HashMap<String, ISemanticAction>();

	public SemanticHandler() {
		initializeAction();
		initializeHandler();
	}

	/**
	 * 初始化动作
	 */
	private void initializeAction() {
		/* 进入块 */
		mapSemanticAction.put("do_enter_scope", new ISemanticAction() {
			@Override
			public void handle(IIndexedData indexed, IManageSymbol manage,
					IRandomAccessOfTokens access, ISemanticRecorder recorder) {
				manage.getManageScopeService().enterScope();
			}
		});
		/* 离开块 */
		mapSemanticAction.put("do_leave_scope", new ISemanticAction() {
			@Override
			public void handle(IIndexedData indexed, IManageSymbol manage,
					IRandomAccessOfTokens access, ISemanticRecorder recorder) {
				manage.getManageScopeService().leaveScope();
			}
		});
		/* 声明过程名 */
		mapSemanticAction.put("predeclear_funcname", new ISemanticAction() {
			@Override
			public void handle(IIndexedData indexed, IManageSymbol manage,
					IRandomAccessOfTokens access, ISemanticRecorder recorder) {
				Token token = access.relativeGet(0);
				String funcName = token.toRealString();
				if (token.kToken == TokenType.ID) {
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
				manage.getManageScopeService().registeFunc(
						token.toRealString(), func);
				if (token.kToken != TokenType.ID) {
					token.object = func.getRealName();
					token.kToken = TokenType.ID;
				}
			}
		});
		/* 声明变量名 */
		mapSemanticAction.put("declear_variable", new ISemanticAction() {
			@Override
			public void handle(IIndexedData indexed, IManageSymbol manage,
					IRandomAccessOfTokens access, ISemanticRecorder recorder) {
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
					} else if (!TokenTools.isExternalName(name)) {
						recorder.add(SemanticError.VARIABLE_REDECLARAED, token);
					}
				} else if (spec == KeywordType.LET) {
					if (!manage.getQueryScopeService().findDeclaredSymbol(name)) {
						recorder.add(SemanticError.VARIABLE_NOT_DECLARAED,
								token);
					}
				}
			}
		});
		/* 声明参数 */
		mapSemanticAction.put("declear_param", new ISemanticAction() {
			@Override
			public void handle(IIndexedData indexed, IManageSymbol manage,
					IRandomAccessOfTokens access, ISemanticRecorder recorder) {
				Token token = access.relativeGet(0);
				if (!manage.getManageScopeService().registerFutureSymbol(
						token.toRealString())) {
					recorder.add(SemanticError.DUP_PARAM, token);
				}
			}
		});
	}

	/**
	 * 初始化语义
	 */
	private void initializeHandler() {
		/* 复制 */
		mapSemanticAnalyzier.put("copy", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				return indexed.get(0).object;
			}
		});
		/* 表达式 */
		mapSemanticAnalyzier.put("exp", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				if (indexed.exists(2)) {// 双目运算
					ExpBinop binop = new ExpBinop();
					binop.setToken(indexed.get(2).token);
					binop.setLeftOperand((IExp) indexed.get(1).object);
					binop.setRightOperand((IExp) indexed.get(0).object);
					return binop.simplify(recorder);
				} else if (indexed.exists(3)) {// 单目运算
					ExpSinop sinop = new ExpSinop();
					sinop.setToken(indexed.get(3).token);
					sinop.setOperand((IExp) indexed.get(1).object);
					return sinop.simplify(recorder);
				} else if (indexed.exists(4)) {// 三目运算
					ExpTriop triop = new ExpTriop();
					triop.setFirstToken(indexed.get(4).token);
					triop.setSecondToken(indexed.get(5).token);
					triop.setFirstOperand((IExp) indexed.get(0).object);
					triop.setSecondOperand((IExp) indexed.get(6).object);
					triop.setThirdOperand((IExp) indexed.get(7).object);
					return triop.simplify(recorder);
				} else {
					Object obj = indexed.get(0).object;
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
					return indexed.get(0).object;
				}
			}
		});
		/* 基本数据结构 */
		mapSemanticAnalyzier.put("type", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				if (indexed.exists(1)) {
					return indexed.get(1).object;
				} else {
					ExpValue value = new ExpValue();
					Token token = indexed.get(0).token;
					value.setToken(token);
					return value;
				}
			}
		});
		/* 入口 */
		mapSemanticAnalyzier.put("main", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				Function func = new Function();
				func.setName(query.getQueryScopeService().getEntryToken());
				func.setRealName(func.getName().toRealString());
				Block block = new Block();
				block.setStmts((ArrayList<IStmt>) indexed.get(0).object);
				block.getStmts().add(new StmtReturn());
				func.setBlock(block);
				return func;
			}
		});
		/* 块 */
		mapSemanticAnalyzier.put("block", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				Block block = new Block();
				if (indexed.exists(0)) {
					block.setStmts((ArrayList<IStmt>) indexed.get(0).object);
				}
				return block;
			}
		});
		/* 语句集合 */
		mapSemanticAnalyzier.put("stmt_list", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				ArrayList<IStmt> stmts;
				if (indexed.exists(1)) {
					stmts = (ArrayList<IStmt>) indexed.get(1).object;
				} else {
					stmts = new ArrayList<IStmt>();
				}
				stmts.add(0, (IStmt) indexed.get(0).object);
				return stmts;
			}
		});
		/* 变量定义 */
		mapSemanticAnalyzier.put("var", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				StmtAssign assign = new StmtAssign();
				Token token = indexed.get(0).token;
				assign.setName(token);
				if (indexed.exists(11)) {
					assign.setDecleared(true);
					assign.setSpec(indexed.get(11).token);
				} else {
					assign.setDecleared(false);
					assign.setSpec(indexed.get(12).token);
				}
				if (indexed.exists(1)) {
					ExpFunc func = new ExpFunc();
					func.setFunc((Function) indexed.get(1).object);
					if (assign.isDecleared()) {
						func.getFunc().setRealName(token.toRealString());
					}
					assign.setExp(func);
				} else {
					assign.setExp((IExp) indexed.get(2).object);
				}
				return assign;
			}
		});
		/* 调用表达式 */
		mapSemanticAnalyzier.put("call_exp", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				ExpInvoke invoke = new ExpInvoke();
				if (indexed.exists(1)) {
					Token token = indexed.get(1).token;
					invoke.setName(token);
					Function func = query.getQueryScopeService().getFuncByName(
							token.toRealString());
					if (func == null) {
						if (TokenTools.isExternalName(token)) {
							invoke.setExtern(token);
						} else {
							recorder.add(SemanticError.MISSING_FUNCNAME, token);
						}
					} else {
						invoke.setFunc(func);
					}
				} else {
					invoke.setFunc((Function) indexed.get(0).object);
					invoke.setName(invoke.getFunc().getName());
				}
				if (indexed.exists(2)) {
					invoke.setParams((ArrayList<IExp>) indexed.get(2).object);
				}
				return invoke;
			}
		});
		/* 调用语句 */
		mapSemanticAnalyzier.put("call_stmt", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				StmtInvoke invoke = new StmtInvoke();
				invoke.setExp((ExpInvoke) indexed.get(0).object);
				return invoke;
			}
		});
		/* 单词集合 */
		mapSemanticAnalyzier.put("token_list", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				ArrayList<Token> tokens;
				if (indexed.exists(1)) {
					tokens = (ArrayList<Token>) indexed.get(1).object;
				} else {
					tokens = new ArrayList<Token>();
				}
				tokens.add(0, indexed.get(0).token);
				return tokens;
			}
		});
		/* 表达式集合 */
		mapSemanticAnalyzier.put("exp_list", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				ArrayList<IExp> exps;
				if (indexed.exists(1)) {
					exps = (ArrayList<IExp>) indexed.get(1).object;
				} else {
					exps = new ArrayList<IExp>();
				}
				exps.add(0, (IExp) indexed.get(0).object);
				return exps;
			}
		});
		/* 过程 */
		mapSemanticAnalyzier.put("func", new ISemanticAnalyzier() {
			@SuppressWarnings("unchecked")
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				Token token = indexed.get(1).token;
				Function func = query.getQueryScopeService().getFuncByName(
						token.toRealString());
				if (indexed.exists(2)) {
					func.setParams((ArrayList<Token>) indexed.get(2).object);
				}
				if (indexed.exists(0)) {
					func.setDoc((ArrayList<String>) indexed.get(0).object);
				}
				if (indexed.exists(3)) {
					Block block = new Block();
					ArrayList<IStmt> stmts = new ArrayList<IStmt>();
					StmtReturn ret = new StmtReturn();
					ret.setExp((IExp) indexed.get(3).object);
					stmts.add(ret);
					block.setStmts(stmts);
					block.getStmts().add(new StmtReturn());
					func.setBlock(block);
				} else {
					Block block = (Block) indexed.get(4).object;
					block.getStmts().add(new StmtReturn());
					func.setBlock(block);
				}
				return func;
			}
		});
		/* 返回语句 */
		mapSemanticAnalyzier.put("return", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				StmtReturn ret = new StmtReturn();
				if (indexed.exists(0)) {
					ret.setExp((IExp) indexed.get(0).object);
				}
				return ret;
			}
		});
		/* 导入/导出 */
		mapSemanticAnalyzier.put("port", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol query,
					ISemanticRecorder recorder) {
				StmtPort port = new StmtPort();
				Token token = indexed.get(0).token;
				port.setName(token);
				if (indexed.exists(1)) {
					Token spec = indexed.get(1).token;
					port.setSpec(spec);
				} else {
					Token spec = indexed.get(2).token;
					port.setSpec(spec);
					Function func = query.getQueryScopeService()
							.getFuncByRealName(token.object.toString());
					if (func == null) {
						recorder.add(SemanticError.WRONG_EXTERN_SYMBOL, token);
					} else {
						func.setExtern(true);
					}
				}
				return port;
			}
		});
	}

	/**
	 * 获得语义分析动作
	 * 
	 * @param name
	 *            语义分析动作名称
	 * @return 语义分析动作
	 */
	public ISemanticAnalyzier getSemanticHandler(String name) {
		ISemanticAnalyzier obj = mapSemanticAnalyzier.get(name);
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

	/**
	 * 获得语义执行动作
	 * 
	 * @param name
	 *            语义执行动作名称
	 * @return 语义执行动作
	 */
	public ISemanticAction getActionHandler(String name) {
		ISemanticAction obj = mapSemanticAction.get(name);
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}
}
