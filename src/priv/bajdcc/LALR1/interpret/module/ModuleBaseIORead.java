package priv.bajdcc.LALR1.interpret.module;

import java.util.List;
import java.util.Scanner;

import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;

/**
 * 【扩展】标准输入流
 *
 * @author bajdcc
 */
public class ModuleBaseIORead implements IRuntimeDebugExec {

	public enum ModuleBaseIOReadType {
		kHasNextBigDecimal, kHasNextBigInteger, kHasNextBoolean, kHasNextLine, kNextBigDecimal, kNextBigInteger, kNextBoolean, kNextChar, kNextLine,
	}

	private static Scanner scanner = new Scanner(System.in);
	private String doc = null;
	private ModuleBaseIOReadType type = null;

	public ModuleBaseIORead(String doc, ModuleBaseIOReadType type) {
		this.doc = doc;
		this.type = type;
	}

	@Override
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			IRuntimeStatus status) throws Exception {
		switch (type) {
		case kHasNextBigDecimal:
			return new RuntimeObject(scanner.hasNextBigDecimal());
		case kHasNextBigInteger:
			return new RuntimeObject(scanner.hasNextBigInteger());
		case kHasNextBoolean:
			return new RuntimeObject(scanner.hasNextBoolean());
		case kHasNextLine:
			return new RuntimeObject(scanner.hasNextLine());
		case kNextBigDecimal:
			return new RuntimeObject(scanner.nextBigDecimal());
		case kNextBigInteger:
			return new RuntimeObject(scanner.nextBigInteger());
		case kNextBoolean:
			return new RuntimeObject(scanner.nextBoolean());
		case kNextChar:
			return new RuntimeObject((char) System.in.read());
		case kNextLine:
			return new RuntimeObject(scanner.nextLine());
		default:
			break;
		}
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
