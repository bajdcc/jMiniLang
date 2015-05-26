package priv.bajdcc.LALR1.interpret.module;

import java.math.BigDecimal;
import java.util.List;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import priv.bajdcc.LALR1.grammar.type.TokenTools;

/**
 * 【扩展】数学一元运算
 *
 * @author bajdcc
 */
public class ModuleMathUnaryFunc implements IRuntimeDebugExec {

	public enum ModuleMathUnaryFuncType {
		kSqrt
	}

	private String doc = null;
	private ModuleMathUnaryFuncType type = null;

	public ModuleMathUnaryFunc(String doc, ModuleMathUnaryFuncType type) {
		this.doc = doc;
		this.type = type;
	}

	@Override
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			IRuntimeStatus status) throws Exception {
		switch (type) {
		case kSqrt:
			return new RuntimeObject(ModuleMath.sqrt((BigDecimal) args.get(0)
					.getObj(), TokenTools.SCALE_NUM));
		default:
			break;
		}
		return null;
	}

	@Override
	public RuntimeObjectType[] getArgsType() {
		return new RuntimeObjectType[] { RuntimeObjectType.kReal };
	}

	@Override
	public String getDoc() {
		return doc;
	}
}
