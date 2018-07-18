package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.util.ResourceLoader;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 【模块】数学模块
 *
 * @author bajdcc
 */
public class ModuleMath implements IInterpreterModule {

	private static ModuleMath instance = new ModuleMath();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleMath getInstance() {
		return instance;
	}

	private final static long HUNDRED = 100L;
	final static private Random rand = new Random();

	@Override
	public String getModuleName() {
		return "sys.math";
	}

	@Override
	public String getModuleCode() {
		return ResourceLoader.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g_PI", () -> new RuntimeObject(Math.PI));
		info.addExternalValue("g_PI_2", () -> new RuntimeObject(Math.PI * 2.0));
		info.addExternalValue("g_E", () -> new RuntimeObject(Math.E));
		info.addExternalValue("g_random", () -> new RuntimeObject(rand.nextDouble()));
		buildUnaryFunc(info);

		return runtimeCodePage = page;
	}

	private void buildUnaryFunc(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_sqrt", new ModuleMathUnaryFunc("开方",
				ModuleMathUnaryFunc.ModuleMathUnaryFuncType.kSqrt));
		info.addExternalFunc("g_cos", new ModuleMathUnaryFunc("余弦",
				ModuleMathUnaryFunc.ModuleMathUnaryFuncType.kCos));
		info.addExternalFunc("g_sin", new ModuleMathUnaryFunc("正弦",
				ModuleMathUnaryFunc.ModuleMathUnaryFuncType.kSin));
		info.addExternalFunc("g_floor", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "四舍五入";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kReal, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				double d = args.get(0).getDouble();
				BigDecimal decimal = BigDecimal.valueOf(d);
				long n = args.get(1).getLong();
				return new RuntimeObject(decimal.setScale((int) n, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
		});
		info.addExternalFunc("g_atan2", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "atan(y, x)";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kReal, RuntimeObjectType.kReal};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				double y = args.get(0).getDouble();
				double x = args.get(1).getDouble();
				return new RuntimeObject(Math.atan2(y, x));
			}
		});
		info.addExternalFunc("g_random_int", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "随机数";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject((long) (rand.nextInt(args.get(0).getInt())));
			}
		});
	}
}
