package priv.bajdcc.OP.grammar;

import priv.bajdcc.LALR1.semantic.lexer.TokenFactory;
import priv.bajdcc.OP.grammar.error.GrammarException;
import priv.bajdcc.OP.grammar.handler.IPatternHandler;
import priv.bajdcc.OP.syntax.Syntax;
import priv.bajdcc.OP.syntax.handler.SyntaxException;
import priv.bajdcc.OP.syntax.handler.SyntaxException.SyntaxError;
import priv.bajdcc.OP.syntax.precedence.PrecedenceTable;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 【语法分析】语法分析
 *
 * @author bajdcc
 */
public class Grammar extends Syntax {

	/**
	 * 单词流工厂
	 */
	private TokenFactory tokenFactory = null;

	/**
	 * 单词流
	 */
	private ArrayList<Token> arrTokens = null;

	/**
	 * 预测分析表
	 */
	private PrecedenceTable table = null;

	/**
	 * 归约模式映射
	 */
	private HashMap<String, IPatternHandler> mapPattern = new HashMap<>();

	public Grammar(String context) throws RegexException {
		super(true);
		tokenFactory = new TokenFactory(context);// 用于分析的文本
		tokenFactory.discard(TokenType.COMMENT);
		tokenFactory.discard(TokenType.WHITESPACE);
		tokenFactory.discard(TokenType.ERROR);
		tokenFactory.discard(TokenType.MACRO);
		tokenFactory.scan();
	}

	/**
	 * 初始化
	 *
	 * @param startSymbol 开始符号
	 * @throws SyntaxException 词法错误
	 */
	public void initialize(String startSymbol) throws SyntaxException {
		super.initialize(startSymbol);
		generateTable();
	}

	/**
	 * 添加归约模式
	 *
	 * @param pattern 模式串（由0和1组成，0=Vn，1=Vt）
	 * @param handler 处理器
	 * @throws SyntaxException 词法错误
	 */
	public void addPatternHandler(String pattern, IPatternHandler handler)
			throws SyntaxException {
		if (mapPattern.put(pattern, handler) != null) {
			err(SyntaxError.REDECLARATION);
		}
	}

	/**
	 * 生成算符优先分析表
	 */
	private void generateTable() {
		table = new PrecedenceTable(arrNonTerminals, arrTerminals, mapPattern,
				tokenFactory);
	}

	/**
	 * 进行语法分析
	 *
	 * @return 计算后的值
	 * @throws GrammarException 语法错误
	 */
	public Object run() throws GrammarException {
		Object obj = table.run();
		arrTokens = tokenFactory.tokenList();
		return obj;
	}

	/**
	 * 获得算符优先关系描述
	 *
	 * @return 算符优先关系描述
	 */
	public String getPrecedenceString() {
		return table.toString();
	}

	/**
	 * 获得单词流描述
	 *
	 * @return 单词流描述
	 */
	public String getTokenString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 单词流 ####");
		sb.append(System.lineSeparator());
		for (Token token : arrTokens) {
			sb.append(token.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}
}
