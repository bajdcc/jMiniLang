package com.bajdcc.LALR1.grammar.runtime;

import com.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import com.bajdcc.LALR1.grammar.type.TokenTools;
import com.bajdcc.util.lexer.token.Token;

/**
 * 【运行时】运行时辅助类
 *
 * @author bajdcc
 */
public class RuntimeTools {

	public static boolean calcOp(RuntimeRegister reg, RuntimeInst inst,
	                             IRuntimeStack stk) throws RuntimeException {
		switch (inst) {
			case inot:
			case iinv:
			case iinc:
			case idec: {
				RuntimeObject obj = stk.load();
				if (obj == null) {
					throw new RuntimeException(RuntimeError.NULL_OPERATOR,
							reg.execId, "不允许空值运算");
				}
				if (obj.getType() == RuntimeObjectType.kNan) {
					stk.store(obj);
					return true;
				}
				Token tk = Token.createFromObject(obj.getObj());
				if (TokenTools.sin(TokenTools.ins2op(inst), tk)) {
					stk.store(new RuntimeObject(tk.object));
				} else {
					return false;
				}
			}
			break;
			case icl:
			case icg:
			case icle:
			case icge:
			case ice:
			case icne:
			case iadd:
			case iand:
			case iandl:
			case idiv:
			case imod:
			case imul:
			case ior:
			case iorl:
			case ishl:
			case ishr:
			case isub:
			case ixor: {
				RuntimeObject obj2 = stk.load();
				RuntimeObject obj = stk.load();
				if (obj.getType() == RuntimeObjectType.kNan) {
					stk.store(obj);
					return true;
				}
				if (obj2.getType() == RuntimeObjectType.kNan) {
					stk.store(obj2);
					return true;
				}
				Token tk = Token.createFromObject(obj.getObj());
				if (TokenTools.bin(TokenTools.ins2op(inst), tk,
						Token.createFromObject(obj2.getObj()))) {
					stk.store(new RuntimeObject(tk.object));
				} else {
					return false;
				}
			}
			break;
			default:
				break;
		}
		return true;
	}

	public static boolean calcJump(RuntimeRegister reg, RuntimeInst inst,
	                               IRuntimeStack stk) throws RuntimeException {
		switch (inst) {
			case ijmp:
				stk.opJump();
				break;
			case ijt:
				stk.opJumpBool(true);
				break;
			case ijf:
				stk.opJumpBool(false);
				break;
			case ijtx:
				stk.opJumpBoolRetain(true);
				break;
			case ijfx:
				stk.opJumpBoolRetain(false);
				break;
			case ijz:
				stk.opJumpZero(true);
				break;
			case ijnz:
				stk.opJumpZero(false);
				break;
			case ijyld:
				stk.opJumpYield();
				break;
			case ijnan:
				stk.opJumpNan();
				break;
			default:
				return false;
		}
		return true;
	}

	public static boolean calcData(RuntimeRegister reg, RuntimeInst inst,
	                               IRuntimeStack stk) throws Exception {
		switch (inst) {
			case ialloc:
				stk.opStoreDirect();
				break;
			case icall:
				stk.opCall();
				break;
			case ildfun:
				stk.opLoadFunc();
				break;
			case irefun:
				stk.opReloadFunc();
				break;
			case iload:
				stk.opLoad();
				break;
			case iloada:
				stk.opLoadArgs();
				break;
			case iloadv:
				stk.opLoadVar();
				break;
			case inop:
				break;
			case iopena:
				stk.opOpenFunc();
				break;
			case ipop:
				stk.pop();
				break;
			case ipush:
				stk.push();
				break;
			case ipusha:
				stk.opPushArgs();
				break;
			case ipushx:
				stk.opPushNull();
				break;
			case ipushz:
				stk.opPushZero();
				break;
			case ipushn:
				stk.opPushNan();
				break;
			case iret:
				stk.opReturn();
				break;
			case istore:
				stk.opStore();
				break;
			case icopy:
				stk.opStoreCopy();
				break;
			case iimp:
				stk.opImport();
				break;
			case iloadx:
				stk.opLoadExtern();
				break;
			case icallx:
				stk.opCallExtern(false);
				break;
			case ically:
				stk.opCallExtern(true);
				break;
			case iyldi:
				stk.opYield(true);
				break;
			case iyldo:
				stk.opYield(false);
				break;
			case iyldl:
				stk.opYieldSwitch(false);
				break;
			case iyldr:
				stk.opYieldSwitch(true);
				break;
			case iyldx:
				stk.opYieldDestroyContext();
				break;
			case iyldy:
				stk.opYieldCreateContext();
				break;
			case iscpi:
				stk.opScope(true);
				break;
			case iscpo:
				stk.opScope(false);
				break;
			case iarr:
				stk.opArr();
				break;
			case imap:
				stk.opMap();
				break;
			case iidx:
				stk.opIndex();
				break;
			case iidxa:
				stk.opIndexAssign();
				break;
			case itry:
				stk.opTry();
				break;
			case ithrow:
				stk.opThrow();
				break;
			default:
				return false;
		}
		return true;
	}

	public static String getYieldHash(int stackLevel, int funcLevel,
	                                  String pageName, int line) {
		return String.format("%d#%d#%s#%d", stackLevel, funcLevel, pageName,
				line);
	}
}
