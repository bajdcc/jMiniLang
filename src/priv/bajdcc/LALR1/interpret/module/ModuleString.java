package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.util.ResourceLoader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【模块】字符串模块
 *
 * @author bajdcc
 */
public class ModuleString implements IInterpreterModule {

	private static ModuleString instance = new ModuleString();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleString getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.string";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildStringUtils(info);

		return runtimeCodePage = page;
	}

	private void buildStringUtils(IRuntimeDebugInfo info) {
        info.addExternalFunc("g_string_replace", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串替换";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kString, RuntimeObjectType.kString};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                String str = (String) args.get(0).getObj();
                String pat = (String) args.get(1).getObj();
                String sub = (String) args.get(2).getObj();
                RuntimeArray arr = new RuntimeArray();
                return new RuntimeObject(str.replaceAll(pat, sub));
            }
        });
        info.addExternalFunc("g_string_split", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串分割";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString, RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				String split = (String) args.get(1).getObj();
				RuntimeArray arr = new RuntimeArray();
				for (String item : str.split(split)) {
					arr.add(new RuntimeObject(item));
				}
				return new RuntimeObject(arr);
			}
		});
		info.addExternalFunc("g_string_splitn", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串分割";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString, RuntimeObjectType.kString, RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				String split = (String) args.get(1).getObj();
				int n = (int) args.get(1).getObj();
				RuntimeArray arr = new RuntimeArray();
				for (String item : str.split(split, n)) {
					arr.add(new RuntimeObject(item));
				}
				return new RuntimeObject(arr);
			}
		});
		info.addExternalFunc("g_string_trim", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串去除头尾空白";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				return new RuntimeObject(str.trim());
			}
		});
		info.addExternalFunc("g_string_length", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串长度";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				return new RuntimeObject(BigInteger.valueOf(str.length()));
			}
		});
		info.addExternalFunc("g_string_char", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串遍历";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				BigInteger index = (BigInteger) args.get(1).getObj();
				return new RuntimeObject(args.get(0).getObj().toString().charAt(index.intValue()));
			}
		});
		info.addExternalFunc("g_string_empty", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串是否为空";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(args.get(0).getObj().toString().isEmpty());
			}
		});
		info.addExternalFunc("g_string_get", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串查询";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString, RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				BigInteger index = (BigInteger) args.get(1).getObj();
				return new RuntimeObject(str.charAt(index.intValue()));
			}
		});
		info.addExternalFunc("g_string_regex", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串正则匹配";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString, RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				String regex = (String) args.get(1).getObj();
				Matcher m = Pattern.compile(regex).matcher(str);
				RuntimeArray arr = new RuntimeArray();
				while (m.find()) {
					arr.add(new RuntimeObject(m.group()));
				}
				return new RuntimeObject(arr);
			}
		});
		info.addExternalFunc("g_string_build", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "从字节数组构造字符串";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kArray };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				StringBuilder sb =  new StringBuilder();
				for (Object obj : array.toList()) {
					sb.append(obj);
				}
				return new RuntimeObject(sb.toString());
			}
		});
		info.addExternalFunc("g_string_atoi", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串转换成数字";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				try {
					BigInteger bi = new BigInteger(str);
					return new RuntimeObject(new BigInteger(str));
				} catch (NumberFormatException e) {
					return new RuntimeObject(BigInteger.valueOf(-1));
				}
			}
		});
		info.addExternalFunc("g_string_atoi_s", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串转换成数字（安全版本）";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				try {
					BigInteger bi = new BigInteger(str);
					return new RuntimeObject(new BigInteger(str));
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});
		info.addExternalFunc("g_string_join_array", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串数组连接";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kArray, RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeArray arr = (RuntimeArray) args.get(0).getObj();
				String delim = (String) args.get(1).getObj();
				return new RuntimeObject(String.join(delim, arr.toStringList()));
			}
		});
		info.addExternalFunc("g_string_toupper", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串大写";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String delim = (String) args.get(0).getObj();
				return new RuntimeObject(delim.toUpperCase());
			}
		});
		info.addExternalFunc("g_string_tolower", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串小写";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String delim = (String) args.get(0).getObj();
				return new RuntimeObject(delim.toLowerCase());
			}
		});
		info.addExternalFunc("g_string_rep", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串重复构造";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String str = (String) args.get(0).getObj();
				BigInteger dup = (BigInteger) args.get(1).getObj();
				int n = dup.intValue();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < n; i++) {
					sb.append(str);
				}
				return new RuntimeObject(sb.toString());
			}
		});
        info.addExternalFunc("g_string_to_number", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串转换数字";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kString};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                String str = (String) args.get(0).getObj();
                try {
                    return new RuntimeObject(new BigInteger(str));
                } catch (Exception e1) {
                    try {
                        return new RuntimeObject(new BigDecimal(str));
                    } catch (Exception e2) {
                        return null;
                    }
                }
            }
        });
        info.addExternalFunc("g_string_equal", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串相等比较";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kObject, RuntimeObjectType.kString};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                if (args.get(0).getType() == RuntimeObjectType.kString) {
                    String str = (String) args.get(0).getObj();
                    String cmp = (String) args.get(1).getObj();
                    return new RuntimeObject(str.compareTo(cmp) == 0);
                }
                return new RuntimeObject(false);
            }
        });
        info.addExternalFunc("g_string_not_equal", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串不等比较";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kObject, RuntimeObjectType.kString};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                if (args.get(0).getType() == RuntimeObjectType.kString) {
                    String str = (String) args.get(0).getObj();
                    String cmp = (String) args.get(1).getObj();
                    return new RuntimeObject(str.compareTo(cmp) != 0);
                }
                return new RuntimeObject(true);
            }
        });
        info.addExternalFunc("g_string_start_with", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串开头比较";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kString};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                String str = (String) args.get(0).getObj();
                String cmp = (String) args.get(1).getObj();
                return new RuntimeObject(str.startsWith(cmp));
            }
        });
        info.addExternalFunc("g_string_substr", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串子串";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kInt, RuntimeObjectType.kInt};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                String str = (String) args.get(0).getObj();
                BigInteger a = (BigInteger) args.get(1).getObj();
                BigInteger b = (BigInteger) args.get(2).getObj();
                return new RuntimeObject(str.substring(a.intValue(), b.intValue()));
            }
        });
        info.addExternalFunc("g_string_left", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串左子串";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kInt};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                String str = (String) args.get(0).getObj();
                BigInteger a = (BigInteger) args.get(1).getObj();
                return new RuntimeObject(str.substring(0, a.intValue()));
            }
        });
        info.addExternalFunc("g_string_right", new IRuntimeDebugExec() {
            @Override
            public String getDoc() {
                return "字符串右子串";
            }

            @Override
            public RuntimeObjectType[] getArgsType() {
                return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kInt};
            }

            @Override
            public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
                                                  IRuntimeStatus status) throws Exception {
                String str = (String) args.get(0).getObj();
                BigInteger a = (BigInteger) args.get(1).getObj();
                return new RuntimeObject(str.substring(a.intValue()));
            }
        });
    }
}
