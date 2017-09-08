package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import priv.bajdcc.util.ResourceLoader;

import java.math.BigInteger;
import java.util.List;

/**
 * 【模块】列表模块
 *
 * @author bajdcc
 */
public class ModuleList implements IInterpreterModule {

	private static ModuleList instance = new ModuleList();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleList getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.list";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g_new_array", () -> new RuntimeObject(new RuntimeArray()));
		info.addExternalValue("g_new_map", () -> new RuntimeObject(new RuntimeMap()));
		buildMethod(info);

		return runtimeCodePage = page;
	}

	private void buildMethod(IRuntimeDebugInfo info) {
		// 数组
		buildArrayMethod(info);
		// 字典
		buildMapMethod(info);
	}

	/**
	 * 数组操作
	 * @param info 信息
     */
	private void buildArrayMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_array_add", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组添加元素";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray, RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				args.get(1).setReadonly(false);
				array.add(args.get(1));
				return args.get(0);
			}
		});
        info.addExternalFunc("g_array_insert", new IRuntimeDebugExec() {
            @Override
			public String getDoc() {
                return "数组添加元素";
            }

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray, RuntimeObjectType.kInt, RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
                BigInteger n = (BigInteger) args.get(1).getObj();
                args.get(2).setReadonly(false);
                array.insert(n.intValue(), args.get(2));
                return args.get(0);
            }
		});
        info.addExternalFunc("g_array_set", new IRuntimeDebugExec() {
            @Override
			public String getDoc() {
                return "数组设置元素";
            }

			@Override
			public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kArray, RuntimeObjectType.kInt, RuntimeObjectType.kObject};
            }

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
                BigInteger index = (BigInteger) args.get(1).getObj();
                args.get(2).setReadonly(false);
                if (!array.set(index.intValue(), args.get(2))) {
                    status.err(RuntimeException.RuntimeError.INVALID_INDEX, "array.set");
                }
                return null;
            }
		});
		info.addExternalFunc("g_array_pop", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组弹出元素";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kArray};
            }

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				return new RuntimeObject(array.pop());
			}
		});
		info.addExternalFunc("g_array_clear", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组清除元素";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				array.clear();
				return new RuntimeObject(array);
			}
		});
		info.addExternalFunc("g_array_reverse", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组反转";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				array.reverse();
				return new RuntimeObject(array);
			}
		});
		info.addExternalFunc("g_array_get", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组查询";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray, RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				BigInteger index = (BigInteger) args.get(1).getObj();
				return new RuntimeObject(array.get(index.intValue()));
			}
		});
		info.addExternalFunc("g_array_get_ex", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组查询";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kArray, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				BigInteger index = (BigInteger) args.get(1).getObj();
				return new RuntimeObject(array.get(index.intValue(), status));
			}
		});
		info.addExternalFunc("g_array_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组长度";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				return array.size();
			}
		});
		info.addExternalFunc("g_array_remove", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组移除";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray, RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				BigInteger index = (BigInteger) args.get(1).getObj();
				return new RuntimeObject(array.remove(index.intValue()));
			}
		});
		info.addExternalFunc("g_array_empty", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组为空";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kArray};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				return new RuntimeObject(array.isEmpty());
			}
		});
		info.addExternalFunc("g_array_range", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "产生连续整数数组";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kInt, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = new RuntimeArray();
				int a = ((BigInteger) args.get(0).getObj()).intValue();
				int b = ((BigInteger) args.get(1).getObj()).intValue();
				for (int i = a; i <= b; i++) {
					array.add(new RuntimeObject(BigInteger.valueOf(i)));
				}
				return new RuntimeObject(array);
			}
		});
	}

	/**
	 * 字典操作
	 * @param info 信息
     */
	private void buildMapMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_map_put", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字典设置元素";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kMap, RuntimeObjectType.kString, RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				String key = args.get(1).getObj().toString();
				map.put(key, args.get(2));
				return null;
			}
		});
		info.addExternalFunc("g_map_contains", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "数组查找键";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kMap, RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				String key = args.get(1).getObj().toString();
				return new RuntimeObject(map.contains(key));
			}
		});
		info.addExternalFunc("g_map_get", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字典查询";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kMap, RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				String key = args.get(1).getObj().toString();
				return new RuntimeObject(map.get(key));
			}
		});
		info.addExternalFunc("g_map_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字典长度";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kMap };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				return map.size();
			}
		});
		info.addExternalFunc("g_map_remove", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字典移除";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kMap, RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				String key = args.get(1).getObj().toString();
				return new RuntimeObject(map.remove(key));
			}
		});
		info.addExternalFunc("g_map_clear", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字典清除元素";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kMap};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				map.clear();
				return new RuntimeObject(map);
			}
		});
		info.addExternalFunc("g_map_empty", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字典为空";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kMap};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				return new RuntimeObject(map.isEmpty());
			}
		});
	}
}
