package priv.bajdcc.LALR1.grammar.codegen;

import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;

import java.util.List;

/**
 * 【扩展】扩展方法文档
 *
 * @author bajdcc
 */
public class CodegenFuncDoc implements IRuntimeDebugExec {

	private String doc = null;

	public CodegenFuncDoc(String doc) {
		this.doc = doc;
	}

	@Override
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
	                                      IRuntimeStatus status) throws Exception {
		return null;
	}

	@Override
	public RuntimeObjectType[] getArgsType() {
		return null;
	}

	@Override
	public String getDoc() {
		return doc;
	}
}
