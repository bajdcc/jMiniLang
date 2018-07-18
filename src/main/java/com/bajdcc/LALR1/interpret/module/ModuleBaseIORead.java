package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;

import java.util.List;
import java.util.Scanner;

/**
 * 【扩展】标准输入流
 *
 * @author bajdcc
 */
public class ModuleBaseIORead implements IRuntimeDebugExec {

	@Override
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
	                                      IRuntimeStatus status) throws Exception {
		switch (type) {
			case kHasNextDouble:
				return new RuntimeObject(scanner.hasNextDouble());
			case kHasNextLong:
				return new RuntimeObject(scanner.hasNextLong());
			case kHasNextBoolean:
				return new RuntimeObject(scanner.hasNextBoolean());
			case kHasNextLine:
				return new RuntimeObject(scanner.hasNextLine());
			case kNextDouble:
				return new RuntimeObject(scanner.nextDouble());
			case kNextLong:
				return new RuntimeObject(scanner.nextLong());
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

	private static Scanner scanner = new Scanner(System.in);
	private String doc;
	private ModuleBaseIOReadType type;

	public ModuleBaseIORead(String doc, ModuleBaseIOReadType type) {
		this.doc = doc;
		this.type = type;
	}

	public enum ModuleBaseIOReadType {
		kHasNextDouble, kHasNextLong, kHasNextBoolean, kHasNextLine, kNextDouble, kNextLong, kNextBoolean, kNextChar, kNextLine,
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
