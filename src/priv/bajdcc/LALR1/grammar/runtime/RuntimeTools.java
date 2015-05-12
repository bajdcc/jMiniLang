package priv.bajdcc.LALR1.grammar.runtime;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.util.lexer.token.Token;

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
			if (obj.isReadonly()
					&& (inst == RuntimeInst.iinc || inst == RuntimeInst.idec)) {
				return false;
			}
			Token tk = Token.createFromObject(obj.getObj());
			if (TokenTools.sin(TokenTools.ins2op(inst), tk)) {
				obj.setObj(tk.object);
				stk.store(obj);
			} else {
				return false;
			}
		}
			break;
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
			RuntimeObject obj = stk.load();
			Token tk = Token.createFromObject(obj.getObj());
			if (TokenTools.bin(TokenTools.ins2op(inst), tk,
					Token.createFromObject(stk.load().getObj()))) {
				obj.setObj(tk.object);
				stk.store(obj);
			} else {
				return false;
			}
		}
			break;
		case icond: {
			RuntimeObject obj = stk.load();
			Token tk = Token.createFromObject(obj.getObj());
			RuntimeObject f = stk.load();
			RuntimeObject t = stk.load();
			int branch = TokenTools.tri(tk);
			if (branch != 0) {
				stk.store(branch == 1 ? t : f);
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
		case ije:
			break;
		case ijf:
			break;
		case ijg:
			break;
		case ijge:
			break;
		case ijl:
			break;
		case ijle:
			break;
		case ijmp:
			stk.opJmp();
			break;
		case ijnz:
			break;
		case ijt:
			break;
		case ijz:
			break;
		default:
			return false;
		}
		return true;
	}

	public static boolean calcData(RuntimeRegister reg, RuntimeInst inst,
			IRuntimeStack stk) throws RuntimeException {
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
		case iret:
			stk.opReturn();
			break;
		case istore:
			stk.opStore();
			break;
		case iimp:
			stk.opImport();
			break;
		case iloadx:
			stk.opLoadExtern();
			break;
		case icallx:
			stk.opCallExtern();
			break;
		default:
			return false;
		}
		return true;
	}
}
