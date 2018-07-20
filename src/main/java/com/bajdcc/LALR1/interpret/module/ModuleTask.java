package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.OP.grammar.error.GrammarException;
import com.bajdcc.OP.grammar.handler.IPatternHandler;
import com.bajdcc.OP.syntax.handler.SyntaxException;
import com.bajdcc.util.ResourceLoader;
import com.bajdcc.util.lexer.error.RegexException;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 【模块】服务模块
 *
 * @author bajdcc
 */
public class ModuleTask implements IInterpreterModule {

	private static ModuleTask instance = new ModuleTask();
	private static Logger logger = Logger.getLogger("task");
	private RuntimeCodePage runtimeCodePage;

	public static ModuleTask getInstance() {
		return instance;
	}

	public static final int TASK_NUM = 16;
	private static final UUID guid = UUID.randomUUID();

	@Override
	public String getModuleName() {
		return "sys.task";
	}

	@Override
	public String getModuleCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.INSTANCE.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildSystemMethod(info);
		buildUtilMethod(info);

		return runtimeCodePage = page;
	}

	private void buildSystemMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_task_get_time", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取当前时间";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String format = String.valueOf(args.get(0).getObj());
				return new RuntimeObject(new SimpleDateFormat(format).format(new Date()));
			}
		});
		info.addExternalFunc("g_task_get_timestamp", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取当前时间戳";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(System.currentTimeMillis());
			}
		});
		info.addExternalFunc("g_task_get_pipe_stat", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取管道信息";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getPipeService().stat(false));
			}
		});
		info.addExternalFunc("g_task_get_share_stat", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取共享信息";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getShareService().stat(false));
			}
		});
		info.addExternalFunc("g_task_get_file_stat", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取文件信息";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getFileService().stat(false));
			}
		});
		info.addExternalFunc("g_task_get_vfs_stat", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取虚拟文件信息";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getFileService().getVfsList(false));
			}
		});
		info.addExternalFunc("g_task_get_user_stat", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取用户服务信息";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getUserService().stat(false));
			}
		});
		info.addExternalFunc("g_task_get_guid", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取GUID";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(guid.toString());
			}
		});
		info.addExternalFunc("g_task_sys_speed", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取虚拟机运行速度";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getProcessService().getSpeed());
			}
		});
	}

	private void buildUtilMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_task_calc", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "四则运算";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String expr = String.valueOf(args.get(0).getObj());
				return new RuntimeObject(util_calc(expr));
			}
		});
	}

	private static String util_calc(String expr) {
		try {
			com.bajdcc.OP.grammar.Grammar grammar = new com.bajdcc.OP.grammar.Grammar(expr);
			grammar.addTerminal("i", TokenType.INTEGER, null);
			grammar.addTerminal("PLUS", TokenType.OPERATOR, OperatorType.PLUS);
			grammar.addTerminal("MINUS", TokenType.OPERATOR, OperatorType.MINUS);
			grammar.addTerminal("TIMES", TokenType.OPERATOR, OperatorType.TIMES);
			grammar.addTerminal("DIVIDE", TokenType.OPERATOR,
					OperatorType.DIVIDE);
			grammar.addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
			grammar.addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
			String[] nons = new String[]{"E", "T", "F"};
			for (String non : nons) {
				grammar.addNonTerminal(non);
			}
			grammar.addPatternHandler("1", new IPatternHandler() {
				@Override
				public Object handle(List<Token> tokens, List<Object> symbols) {
					return Integer.parseInt(tokens.get(0).getObj().toString());
				}

				@Override
				public String getPatternName() {
					return "操作数转换";
				}
			});
			grammar.addPatternHandler("010", new IPatternHandler() {
				@Override
				public Object handle(List<Token> tokens, List<Object> symbols) {
					int lop = (int) symbols.get(0);
					int rop = (int) symbols.get(1);
					Token op = tokens.get(0);
					if (op.getType() == TokenType.OPERATOR) {
						OperatorType kop = (OperatorType) op.getObj();
						switch (kop) {
							case PLUS:
								return lop + rop;
							case MINUS:
								return lop - rop;
							case TIMES:
								return lop * rop;
							case DIVIDE:
								if (rop == 0) {
									return lop;
								} else {
									return lop / rop;
								}
							default:
								return 0;
						}
					} else {
						return 0;
					}
				}

				@Override
				public String getPatternName() {
					return "二元运算";
				}
			});
			grammar.addPatternHandler("101", new IPatternHandler() {
				@Override
				public Object handle(List<Token> tokens, List<Object> symbols) {
					Token ltok = tokens.get(0);
					Token rtok = tokens.get(1);
					Object exp = symbols.get(0);
					if (ltok.getObj() == OperatorType.LPARAN
							&& rtok.getObj() == OperatorType.RPARAN) {// 判断括号
						return exp;
					}
					return null;
				}

				@Override
				public String getPatternName() {
					return "括号运算";
				}
			});
			grammar.infer("E -> E @PLUS T | E @MINUS T | T");
			grammar.infer("T -> T @TIMES F | T @DIVIDE F | F");
			grammar.infer("F -> @LPA E @RPA | @i");
			grammar.initialize("E");
			return String.valueOf(grammar.run());
		} catch (RegexException e) {
			logger.error("#CALC# Error: " + e.getPosition() + "," + e.getMessage());
		} catch (SyntaxException e) {
			logger.error("#CALC#Error: " + e.getPosition() + "," + e.getMessage() + " " + e.getInfo());
		} catch (GrammarException e) {
			logger.error("#CALC#Error: " + e.getPosition() + "," + e.getMessage() + " " + e.getInfo());
		}
		return "#CALC#Error";
	}
}
