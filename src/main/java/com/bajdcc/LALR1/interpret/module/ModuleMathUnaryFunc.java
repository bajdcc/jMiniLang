package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import com.bajdcc.LALR1.grammar.type.TokenTools;

import java.math.BigDecimal;
import java.util.List;

/**
 * 【扩展】数学一元运算
 *
 * @author bajdcc
 */
public class ModuleMathUnaryFunc implements IRuntimeDebugExec {

	public enum ModuleMathUnaryFuncType {
		kSqrt,
		kSqrtDouble,
		kCos,
		kSin,
	}

	private String doc = null;
	private ModuleMathUnaryFuncType type = null;

	public ModuleMathUnaryFunc(String doc, ModuleMathUnaryFuncType type) {
		this.doc = doc;
		this.type = type;
	}

	@Override
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
	                                      IRuntimeStatus status) {
		switch (type) {
			case kSqrt:
				return new RuntimeObject(ModuleMath.sqrt((BigDecimal) args.get(0)
						.getObj(), TokenTools.SCALE_NUM));
			case kSqrtDouble:
				return new RuntimeObject(BigDecimal.valueOf(Math.sqrt(((BigDecimal) args.get(0)
						.getObj()).doubleValue())));
			case kCos:
				return new RuntimeObject(BigDecimal.valueOf(Math.cos(((BigDecimal) args.get(0)
						.getObj()).doubleValue())));
			case kSin:
				return new RuntimeObject(BigDecimal.valueOf(Math.sin(((BigDecimal) args.get(0)
						.getObj()).doubleValue())));
			default:
				break;
		}
		return null;
	}

	@Override
	public RuntimeObjectType[] getArgsType() {
		return new RuntimeObjectType[]{RuntimeObjectType.kReal};
	}

	@Override
	public String getDoc() {
		return doc;
	}
}
