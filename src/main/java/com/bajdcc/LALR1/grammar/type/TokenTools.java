package com.bajdcc.LALR1.grammar.type;

import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.ExpBinop;
import com.bajdcc.LALR1.grammar.tree.ExpSinop;
import com.bajdcc.LALR1.grammar.tree.ExpTriop;
import com.bajdcc.LALR1.grammar.tree.ExpValue;
import com.bajdcc.util.lexer.token.OperatorType;
import com.bajdcc.util.lexer.token.Token;
import com.bajdcc.util.lexer.token.TokenType;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 【语义分析】一元表达式辅助计算工具
 *
 * @author bajdcc
 */
public class TokenTools {

	/**
	 * 精度
	 */
	public static final int SCALE_NUM = 10;

	private static Map<TokenType, ITokenConventer> mapConverter = new HashMap<>();
	private static Map<OperatorType, RuntimeInst> mapOp2Ins = new HashMap<>();
	private static Map<RuntimeInst, OperatorType> mapIns2Op = new HashMap<>();

	static {
		mapConverter.put(TokenType.BOOL, new ConvertToBoolean());
		mapConverter.put(TokenType.CHARACTER, new ConvertToChar());
		mapConverter.put(TokenType.INTEGER, new ConvertToInt());
		mapConverter.put(TokenType.DECIMAL, new ConvertToDecimal());
		mapConverter.put(TokenType.STRING, new ConvertToString());
	}

	static {
		mapOp2Ins.put(OperatorType.LOGICAL_NOT, RuntimeInst.inot);
		mapOp2Ins.put(OperatorType.BIT_NOT, RuntimeInst.iinv);
		mapOp2Ins.put(OperatorType.PLUS_PLUS, RuntimeInst.iinc);
		mapOp2Ins.put(OperatorType.MINUS_MINUS, RuntimeInst.idec);
		mapOp2Ins.put(OperatorType.PLUS, RuntimeInst.iadd);
		mapOp2Ins.put(OperatorType.MINUS, RuntimeInst.isub);
		mapOp2Ins.put(OperatorType.TIMES, RuntimeInst.imul);
		mapOp2Ins.put(OperatorType.DIVIDE, RuntimeInst.idiv);
		mapOp2Ins.put(OperatorType.MOD, RuntimeInst.imod);
		mapOp2Ins.put(OperatorType.LEFT_SHIFT, RuntimeInst.ishl);
		mapOp2Ins.put(OperatorType.RIGHT_SHIFT, RuntimeInst.ishr);
		mapOp2Ins.put(OperatorType.LOGICAL_AND, RuntimeInst.iandl);
		mapOp2Ins.put(OperatorType.LOGICAL_OR, RuntimeInst.iorl);
		mapOp2Ins.put(OperatorType.BIT_AND, RuntimeInst.iand);
		mapOp2Ins.put(OperatorType.BIT_OR, RuntimeInst.ior);
		mapOp2Ins.put(OperatorType.BIT_XOR, RuntimeInst.ixor);
		mapOp2Ins.put(OperatorType.LESS_THAN, RuntimeInst.icl);
		mapOp2Ins.put(OperatorType.LESS_THAN_OR_EQUAL, RuntimeInst.icle);
		mapOp2Ins.put(OperatorType.GREATER_THAN, RuntimeInst.icg);
		mapOp2Ins.put(OperatorType.GREATER_THAN_OR_EQUAL, RuntimeInst.icge);
		mapOp2Ins.put(OperatorType.EQUAL, RuntimeInst.ice);
		mapOp2Ins.put(OperatorType.NOT_EQUAL, RuntimeInst.icne);
		for (Entry<OperatorType, RuntimeInst> entry : mapOp2Ins.entrySet()) {
			mapIns2Op.put(entry.getValue(), entry.getKey());
		}
		mapOp2Ins.put(OperatorType.PLUS_ASSIGN, RuntimeInst.iadd);
		mapOp2Ins.put(OperatorType.MINUS_ASSIGN, RuntimeInst.isub);
		mapOp2Ins.put(OperatorType.TIMES_ASSIGN, RuntimeInst.imul);
		mapOp2Ins.put(OperatorType.DIV_ASSIGN, RuntimeInst.idiv);
		mapOp2Ins.put(OperatorType.MOD_ASSIGN, RuntimeInst.imod);
		mapOp2Ins.put(OperatorType.AND_ASSIGN, RuntimeInst.iand);
		mapOp2Ins.put(OperatorType.OR_ASSIGN, RuntimeInst.ior);
		mapOp2Ins.put(OperatorType.XOR_ASSIGN, RuntimeInst.ixor);
		mapOp2Ins.put(OperatorType.EQ_ASSIGN, RuntimeInst.ice);
	}

	/**
	 * 单目运算
	 *
	 * @param recorder 错误记录
	 * @param exp      表达式
	 * @return 运算是否合法
	 */
	public static boolean sinop(ISemanticRecorder recorder, ExpSinop exp) {
		ExpValue value = (ExpValue) exp.getOperand();
		Token token = value.getToken();
		OperatorType type = (OperatorType) exp.getToken().getObj();
		if (sin(type, token)) {
			return true;
		}
		recorder.add(SemanticError.INVALID_OPERATOR, token);
		return false;
	}

	/**
	 * 单目运算
	 *
	 * @param type  操作符
	 * @param token 操作数
	 * @return 运算是否合法
	 */
	public static boolean sin(OperatorType type, Token token) {
		switch (type) {
			case LOGICAL_NOT:
				Token bool = mapConverter.get(TokenType.BOOL).convert(token);
				if (bool.getType() == TokenType.BOOL) {
					bool.setObj(!((boolean) bool.getObj()));
					return true;
				}
				break;
			case BIT_NOT:
				if (token.getType() == TokenType.INTEGER) {
					token.setObj(~((long) token.getObj()));
					return true;
				} else if (token.getType() == TokenType.DECIMAL) {
					token.setObj(-((double) token.getObj()));
					return true;
				}
				break;
			case PLUS_PLUS:
				if (token.getType() == TokenType.INTEGER) {
					token.setObj(((long) token.getObj()) + 1L);
					return true;
				} else if (token.getType() == TokenType.DECIMAL) {
					token.setObj(((double) token.getObj()) + 1D);
					return true;
				}
				break;
			case MINUS_MINUS:
				if (token.getType() == TokenType.INTEGER) {
					token.setObj(((long) token.getObj()) - 1L);
					return true;
				} else if (token.getType() == TokenType.DECIMAL) {
					token.setObj(((double) token.getObj()) - 1D);
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}

	/**
	 * 双目运算
	 *
	 * @param recorder 错误记录
	 * @param exp      表达式
	 * @return 运算是否合法
	 */
	public static boolean binop(ISemanticRecorder recorder, ExpBinop exp) {
		ExpValue leftValue = (ExpValue) exp.getLeftOperand();
		ExpValue rightValue = (ExpValue) exp.getRightOperand();
		Token token = exp.getToken();
		Token leftToken = leftValue.getToken();
		Token rightToken = rightValue.getToken();
		OperatorType type = (OperatorType) token.getObj();
		if (bin(type, leftToken, rightToken)) {
			return true;
		}
		recorder.add(SemanticError.INVALID_OPERATOR, token);
		return false;
	}

	public static boolean bin(OperatorType type, Token lop, Token rop) {
		return bin(type, lop, rop, true);
	}

	/**
	 * 二元运算（包含向上转换）
	 *
	 * @param lop  左操作数
	 * @param rop  右操作数
	 * @param init 操作数默认转型
	 * @return 运算是否合法
	 */
	private static boolean bin(OperatorType type, Token lop, Token rop,
	                           boolean init) {
		if (type == OperatorType.DOT) {
			ITokenConventer s = mapConverter.get(TokenType.STRING);
			s.convert(rop);
			return true;
		}
		if (init) {
			switch (type) {
				case LOGICAL_AND:
				case LOGICAL_OR:
					ITokenConventer bool = mapConverter.get(TokenType.BOOL);
					bool.convert(lop);
					bool.convert(rop);
					break;
				case BIT_AND:
				case BIT_OR:
				case BIT_XOR:
					ITokenConventer integer = mapConverter.get(TokenType.INTEGER);
					integer.convert(lop);
					integer.convert(rop);
					break;
				case LEFT_SHIFT:
				case RIGHT_SHIFT:
					integer = mapConverter.get(TokenType.INTEGER);
					integer.convert(rop);
				default:
					break;
			}
		}
		if (lop.getType() == rop.getType()) {// 操作数类型相同
			switch (lop.getType()) {
				case BOOL:
					boolean lbo = (boolean) lop.getObj();
					boolean rbo = (boolean) rop.getObj();
					switch (type) {
						case PLUS:
						case LOGICAL_OR:
							lop.setObj(lbo || rbo);
							break;
						case MINUS:
						case DIVIDE:
							lop.setObj(lbo && !rbo);
							break;
						case TIMES:
						case LOGICAL_AND:
							lop.setObj(lbo && rbo);
							break;
						default:
							return false;
					}
					return true;
				case CHARACTER:
					char lch = (char) lop.getObj();
					char rch = (char) rop.getObj();
					switch (type) {
						case PLUS:
							lop.setObj((char) (lch + rch));
							break;
						case MINUS:
							lop.setObj((char) (lch - rch));
							break;
						case TIMES:
							lop.setObj((char) (lch * rch));
							break;
						case DIVIDE:
							lop.setObj((char) (lch / rch));
							break;
						case MOD:
							lop.setObj((char) (lch % rch));
							break;
						case LESS_THAN:
							lop.setType(TokenType.BOOL);
							lop.setObj(lch < rch);
							break;
						case LESS_THAN_OR_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lch <= rch);
							break;
						case GREATER_THAN:
							lop.setType(TokenType.BOOL);
							lop.setObj(lch > rch);
							break;
						case GREATER_THAN_OR_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lch >= rch);
							break;
						case EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lch == rch);
							break;
						case NOT_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lch != rch);
							break;
						default:
							return false;
					}
					return true;
				case STRING:
					String lstr = (String) lop.getObj();
					String rstr = (String) rop.getObj();
					switch (type) {
						case PLUS:
							lop.setObj(lstr + rstr);
							break;
						case EQUAL:
							lop.setObj(lstr.equals(rstr));
							lop.setType(TokenType.BOOL);
							break;
						case NOT_EQUAL:
							lop.setObj(!lstr.equals(rstr));
							lop.setType(TokenType.BOOL);
							break;
						default:
							return false;
					}
					return true;
				case DECIMAL:
					Double ldec = (Double) lop.getObj();
					Double rdec = (Double) rop.getObj();
					switch (type) {
						case PLUS:
							lop.setObj(ldec + rdec);
							break;
						case MINUS:
							lop.setObj(ldec - rdec);
							break;
						case TIMES:
							lop.setObj(ldec * rdec);
							break;
						case DIVIDE:
							lop.setObj(ldec / rdec);
							break;
						case LESS_THAN:
							lop.setType(TokenType.BOOL);
							lop.setObj(ldec.compareTo(rdec) < 0);
							break;
						case LESS_THAN_OR_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(ldec.compareTo(rdec) <= 0);
							break;
						case GREATER_THAN:
							lop.setType(TokenType.BOOL);
							lop.setObj(ldec.compareTo(rdec) > 0);
							break;
						case GREATER_THAN_OR_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(ldec.compareTo(rdec) >= 0);
							break;
						case EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(ldec.compareTo(rdec) == 0);
							break;
						case NOT_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(ldec.compareTo(rdec) != 0);
							break;
						default:
							return false;
					}
					return true;
				case INTEGER:
					Long lint = (Long) lop.getObj();
					Long rint = (Long) rop.getObj();
					switch (type) {
						case PLUS:
							lop.setObj(lint + rint);
							break;
						case MINUS:
							lop.setObj(lint - rint);
							break;
						case TIMES:
							lop.setObj(lint * rint);
							break;
						case DIVIDE:
							lop.setObj(lint / rint);
							break;
						case MOD:
							lop.setObj(lint % rint);
							break;
						case LEFT_SHIFT:
							lop.setObj(lint << rint);
							break;
						case RIGHT_SHIFT:
							lop.setObj(lint >> rint);
							break;
						case BIT_AND:
							lop.setObj(lint & rint);
							break;
						case BIT_OR:
							lop.setObj(lint | rint);
							break;
						case BIT_XOR:
							lop.setObj(lint ^ rint);
							break;
						case LESS_THAN:
							lop.setType(TokenType.BOOL);
							lop.setObj(lint.compareTo(rint) < 0);
							break;
						case LESS_THAN_OR_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lint.compareTo(rint) <= 0);
							break;
						case GREATER_THAN:
							lop.setType(TokenType.BOOL);
							lop.setObj(lint.compareTo(rint) > 0);
							break;
						case GREATER_THAN_OR_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lint.compareTo(rint) >= 0);
							break;
						case EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lint.compareTo(rint) == 0);
							break;
						case NOT_EQUAL:
							lop.setType(TokenType.BOOL);
							lop.setObj(lint.compareTo(rint) != 0);
							break;
						default:
							return false;
					}
					return true;
				default:
					break;
			}
		} else {// 操作数类型不同，需要提升
			if (promote(lop, rop)) {
				return bin(type, lop, rop, false);
			}
		}
		return false;
	}

	/**
	 * 三目运算（当前只有一种形式）
	 *
	 * @param recorder 错误记录
	 * @param exp      表达式
	 * @return 运算是否合法
	 */
	public static int triop(ISemanticRecorder recorder, ExpTriop exp) {
		ExpValue firstValue = (ExpValue) exp.getFirstOperand();
		Token firstToken = exp.getFirstToken();
		Token secondToken = exp.getSecondToken();
		int branch = tri(firstToken, secondToken, firstValue.getToken());
		if (branch != 0) {
			return branch;
		}
		recorder.add(SemanticError.INVALID_OPERATOR, firstToken);
		return 0;
	}

	/**
	 * 三目运算（当前只有一种形式）
	 *
	 * @param op1   操作符1
	 * @param op2   操作符2
	 * @param token 操作数
	 * @return 运算是否合法
	 */
	public static int tri(Token op1, Token op2, Token token) {
		if (op1.getObj() == OperatorType.QUERY
				&& op2.getObj() == OperatorType.COLON) {
			return tri(token);
		}
		return 0;
	}

	/**
	 * 三目运算（当前只有一种形式）
	 *
	 * @param token 操作数
	 * @return 运算是否合法
	 */
	public static int tri(Token token) {
		Token bool = mapConverter.get(TokenType.BOOL).convert(token);
		if (bool.getType() == TokenType.BOOL) {
			return ((boolean) bool.getObj()) ? 1 : 2;
		}
		return 0;
	}

	/**
	 * 操作数提升（即向上转换），提升主要看左操作数（这样即隐含类型转换）
	 *
	 * @param lop 左操作数
	 * @param rop 右操作数
	 * @return 运算是否合法
	 */
	private static boolean promote(Token lop, Token rop) {
		return promote(lop.getType(), rop);
	}

	/**
	 * 操作数提升（即向上转换），提升主要看左操作数（这样即隐含类型转换）
	 *
	 * @param type 左操作数类型
	 * @param rop  右操作数
	 * @return 运算是否合法
	 */
	public static boolean promote(TokenType type, Token rop) {
		ITokenConventer conventer = mapConverter.get(type);
		if (conventer != null) {
			Token token = mapConverter.get(type).convert(rop);
			return token.getType() == type;
		} else {
			return false;
		}
	}

	public static RuntimeInst op2ins(Token token) {
		if (token.getType() == TokenType.OPERATOR) {
			RuntimeInst inst = mapOp2Ins.get(token.getObj());
			if (inst != null)
				return inst;
		}
		return RuntimeInst.inop;
	}

	public static OperatorType ins2op(RuntimeInst inst) {
		return mapIns2Op.get(inst);
	}

	public static boolean isExternalName(Token token) {
		if (token.getType() == TokenType.ID) {
			String name = token.toRealString();
			return isExternalName(name);
		}
		return false;
	}

	public static boolean isExternalName(String name) {
		return name.startsWith("g_");
	}

	public static boolean isAssignment(OperatorType op) {
		switch (op) {
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case TIMES_ASSIGN:
			case DIV_ASSIGN:
			case AND_ASSIGN:
			case OR_ASSIGN:
			case XOR_ASSIGN:
			case MOD_ASSIGN:
			case EQ_ASSIGN:
				return true;
		}
		return false;
	}
}
