package priv.bajdcc.LALR1.grammar.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import priv.bajdcc.LALR1.grammar.error.SemanticException.SemanticError;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.ExpBinop;
import priv.bajdcc.LALR1.grammar.tree.ExpSinop;
import priv.bajdcc.LALR1.grammar.tree.ExpTriop;
import priv.bajdcc.LALR1.grammar.tree.ExpValue;
import priv.bajdcc.util.lexer.token.OperatorType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 【语义分析】一元表达式辅助计算工具
 *
 * @author bajdcc
 */
public class TokenTools {

	/**
	 * 精度
	 */
	private static int SCALE_NUM = 10;

	private static HashMap<TokenType, ITokenConventer> mapConverter = new HashMap<TokenType, ITokenConventer>();

	static {
		mapConverter.put(TokenType.BOOL, new ConvertToBoolean());
		mapConverter.put(TokenType.CHARACTER, new ConvertToChar());
		mapConverter.put(TokenType.INTEGER, new ConvertToInt());
		mapConverter.put(TokenType.DECIMAL, new ConvertToDecimal());
	}

	/**
	 * 单目运算
	 * 
	 * @param recorder
	 *            错误记录
	 * @param type
	 *            操作符
	 * @param exp
	 *            表达式
	 * @return 运算是否合法
	 */
	public static boolean sinop(ISemanticRecorder recorder, ExpSinop exp) {
		ExpValue value = (ExpValue) exp.getOperand();
		Token token = value.getToken();
		OperatorType type = (OperatorType) exp.getToken().object;
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
		recorder.add(SemanticError.INVALID_OPERATOR, token);
		return false;
	}

	/**
	 * 双目运算
	 * 
	 * @param recorder
	 *            错误记录
	 * @param type
	 *            操作符
	 * @param exp
	 *            表达式
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

	/**
	 * 三目运算（当前只有一种形式）
	 * 
	 * @param recorder
	 *            错误记录
	 * @param type
	 *            操作符
	 * @param exp
	 *            表达式
	 * @return 运算是否合法
	 */
	public static int triop(ISemanticRecorder recorder, ExpTriop exp) {
		ExpValue firstValue = (ExpValue) exp.getFirstOperand();
		Token firstToken = exp.getFirstToken();
		Token secondToken = exp.getSecondToken();
		OperatorType type1 = (OperatorType) firstToken.object;
		OperatorType type2 = (OperatorType) secondToken.object;
		if (type1 == OperatorType.QUERY && type2 == OperatorType.COLON) {
			Token bool = mapConverter.get(TokenType.BOOL).convert(
					firstValue.getToken());
			if (bool.kToken == TokenType.BOOL) {
				return ((boolean) bool.object) ? 1 : 2;
			}
		}
		recorder.add(SemanticError.INVALID_OPERATOR, firstToken);
		return 0;
	}

	private static boolean bin(OperatorType type, Token lop, Token rop) {
		return bin(type, lop, rop, true);
	}

	/**
	 * 二元运算（包含向上转换）
	 * 
	 * @param lop
	 *            左操作数
	 * @param lop
	 *            左操作数
	 * @param rop
	 *            右操作数
	 * @param init
	 *            操作数默认转型
	 * @return 运算是否合法
	 */
	private static boolean bin(OperatorType type, Token lop, Token rop,
			boolean init) {
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
					lop.object = lch + rch;
					break;
				case MINUS:
					lop.object = lch - rch;
					break;
				case TIMES:
					lop.object = lch * rch;
					break;
				case DIVIDE:
					lop.object = lch / rch;
					break;
				case MOD:
					lop.object = lch % rch;
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
	 * 操作数提升（即向上转换），提升主要看左操作数（这样即隐含类型转换）
	 * 
	 * @param lop
	 *            左操作数
	 * @param lop
	 *            左操作数
	 * @param rop
	 *            右操作数
	 * @return 运算是否合法
	 */
	private static boolean promote(Token lop, Token rop) {
		ITokenConventer conventer = mapConverter.get(lop.kToken);
		if (conventer != null) {
			Token token = mapConverter.get(lop.kToken).convert(rop);
			return token.kToken == lop.kToken;
		} else {
			return false;
		}
	}
}
