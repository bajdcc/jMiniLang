package priv.bajdcc.LALR1.grammar.type;

import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.ExpBinop;
import priv.bajdcc.LALR1.grammar.tree.ExpSinop;
import priv.bajdcc.LALR1.grammar.tree.ExpTriop;
import priv.bajdcc.LALR1.grammar.tree.ExpValue;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
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

	private static HashMap<TokenType, ITokenConventer> mapConverter = new HashMap<>();
	private static HashMap<OperatorType, RuntimeInst> mapOp2Ins = new HashMap<>();
	private static HashMap<RuntimeInst, OperatorType> mapIns2Op = new HashMap<>();

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
		OperatorType type = (OperatorType) exp.getToken().object;
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
				if (bool.kToken == TokenType.BOOL) {
					bool.object = !((boolean) bool.object);
					return true;
				}
				break;
			case BIT_NOT:
				if (token.kToken == TokenType.INTEGER) {
					token.object = ((BigInteger) token.object).xor(BigInteger.ZERO);
					return true;
				} else if (token.kToken == TokenType.DECIMAL) {
					token.object = ((BigDecimal) token.object).negate();
					return true;
				}
				break;
			case PLUS_PLUS:
				if (token.kToken == TokenType.INTEGER) {
					token.object = ((BigInteger) token.object).add(BigInteger.ONE);
					return true;
				} else if (token.kToken == TokenType.DECIMAL) {
					token.object = ((BigDecimal) token.object).add(BigDecimal.ONE);
					return true;
				}
				break;
			case MINUS_MINUS:
				if (token.kToken == TokenType.INTEGER) {
					token.object = ((BigInteger) token.object)
							.subtract(BigInteger.ONE);
					return true;
				} else if (token.kToken == TokenType.DECIMAL) {
					token.object = ((BigDecimal) token.object).add(BigDecimal.ONE);
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
		OperatorType type = (OperatorType) token.object;
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
		if (lop.kToken == rop.kToken) {// 操作数类型相同
			switch (lop.kToken) {
				case BOOL:
					boolean lbo = (boolean) lop.object;
					boolean rbo = (boolean) rop.object;
					switch (type) {
						case PLUS:
						case LOGICAL_OR:
							lop.object = lbo || rbo;
							break;
						case MINUS:
						case DIVIDE:
							lop.object = lbo && !rbo;
							break;
						case TIMES:
						case LOGICAL_AND:
							lop.object = lbo && rbo;
							break;
						default:
							return false;
					}
					return true;
				case CHARACTER:
					char lch = (char) lop.object;
					char rch = (char) rop.object;
					switch (type) {
						case PLUS:
							lop.object = (char) (lch + rch);
							break;
						case MINUS:
							lop.object = (char) (lch - rch);
							break;
						case TIMES:
							lop.object = (char) (lch * rch);
							break;
						case DIVIDE:
							lop.object = (char) (lch / rch);
							break;
						case MOD:
							lop.object = (char) (lch % rch);
							break;
						case LESS_THAN:
							lop.kToken = TokenType.BOOL;
							lop.object = lch < rch;
							break;
						case LESS_THAN_OR_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lch <= rch;
							break;
						case GREATER_THAN:
							lop.kToken = TokenType.BOOL;
							lop.object = lch > rch;
							break;
						case GREATER_THAN_OR_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lch >= rch;
							break;
						case EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lch == rch;
							break;
						case NOT_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lch != rch;
							break;
						default:
							return false;
					}
					return true;
				case STRING:
					String lstr = (String) lop.object;
					String rstr = (String) rop.object;
					switch (type) {
						case PLUS:
							lop.object = lstr + rstr;
							break;
						case EQUAL:
							lop.object = lstr.equals(rstr);
							lop.kToken = TokenType.BOOL;
							break;
						case NOT_EQUAL:
							lop.object = !lstr.equals(rstr);
							lop.kToken = TokenType.BOOL;
							break;
						default:
							return false;
					}
					return true;
				case DECIMAL:
					BigDecimal ldec = (BigDecimal) lop.object;
					BigDecimal rdec = (BigDecimal) rop.object;
					switch (type) {
						case PLUS:
							lop.object = ldec.add(rdec);
							break;
						case MINUS:
							lop.object = ldec.subtract(rdec);
							break;
						case TIMES:
							lop.object = ldec.multiply(rdec);
							break;
						case DIVIDE:
							lop.object = ldec.divide(rdec, SCALE_NUM,
									BigDecimal.ROUND_HALF_UP);
							break;
						case LEFT_SHIFT:
							lop.object = ldec.movePointLeft(rdec.intValue());
							break;
						case RIGHT_SHIFT:
							lop.object = ldec.movePointRight(rdec.intValue());
							break;
						case LESS_THAN:
							lop.kToken = TokenType.BOOL;
							lop.object = ldec.compareTo(rdec) < 0;
							break;
						case LESS_THAN_OR_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = ldec.compareTo(rdec) <= 0;
							break;
						case GREATER_THAN:
							lop.kToken = TokenType.BOOL;
							lop.object = ldec.compareTo(rdec) > 0;
							break;
						case GREATER_THAN_OR_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = ldec.compareTo(rdec) >= 0;
							break;
						case EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = ldec.compareTo(rdec) == 0;
							break;
						case NOT_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = ldec.compareTo(rdec) != 0;
							break;
						default:
							return false;
					}
					return true;
				case INTEGER:
					BigInteger lint = (BigInteger) lop.object;
					BigInteger rint = (BigInteger) rop.object;
					switch (type) {
						case PLUS:
							lop.object = lint.add(rint);
							break;
						case MINUS:
							lop.object = lint.subtract(rint);
							break;
						case TIMES:
							lop.object = lint.multiply(rint);
							break;
						case DIVIDE:
							lop.object = lint.divide(rint);
							break;
						case MOD:
							lop.object = lint.mod(rint);
							break;
						case LEFT_SHIFT:
							lop.object = lint.shiftLeft(rint.intValue());
							break;
						case RIGHT_SHIFT:
							lop.object = lint.shiftRight(rint.intValue());
							break;
						case BIT_AND:
							lop.object = lint.and(rint);
							break;
						case BIT_OR:
							lop.object = lint.or(rint);
							break;
						case BIT_XOR:
							lop.object = lint.xor(rint);
							break;
						case LESS_THAN:
							lop.kToken = TokenType.BOOL;
							lop.object = lint.compareTo(rint) < 0;
							break;
						case LESS_THAN_OR_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lint.compareTo(rint) <= 0;
							break;
						case GREATER_THAN:
							lop.kToken = TokenType.BOOL;
							lop.object = lint.compareTo(rint) > 0;
							break;
						case GREATER_THAN_OR_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lint.compareTo(rint) >= 0;
							break;
						case EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lint.compareTo(rint) == 0;
							break;
						case NOT_EQUAL:
							lop.kToken = TokenType.BOOL;
							lop.object = lint.compareTo(rint) != 0;
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
		if (op1.object == OperatorType.QUERY
				&& op2.object == OperatorType.COLON) {
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
		if (bool.kToken == TokenType.BOOL) {
			return ((boolean) bool.object) ? 1 : 2;
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
		return promote(lop.kToken, rop);
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
			return token.kToken == type;
		} else {
			return false;
		}
	}

	public static RuntimeInst op2ins(Token token) {
		if (token.kToken == TokenType.OPERATOR) {
			RuntimeInst inst = mapOp2Ins.get(token.object);
			if (inst != null)
				return inst;
		}
		return RuntimeInst.inop;
	}

	public static OperatorType ins2op(RuntimeInst inst) {
		return mapIns2Op.get(inst);
	}

	public static boolean isExternalName(Token token) {
		if (token.kToken == TokenType.ID) {
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
