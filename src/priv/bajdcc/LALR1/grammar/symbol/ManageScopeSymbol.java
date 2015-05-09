package priv.bajdcc.LALR1.grammar.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import priv.bajdcc.LALR1.grammar.error.SemanticException;
import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.util.HashListMap;
import priv.bajdcc.util.HashListMapEx;
import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

/**
 * 命名空间管理
 *
 * @author bajdcc
 */
public class ManageScopeSymbol implements IManageScopeSymbol, IQueryScopeSymbol {

	private static String ENTRY_NAME = "main";
	private static String LAMBDA_PREFIX = "~lambda#";
	private int lambdaId = 0;
	private HashListMap<String> symbolList = new HashListMap<String>();
	private HashListMapEx<String, Function> funcMap = new HashListMapEx<String, Function>();
	private ArrayList<HashSet<String>> stkScope = new ArrayList<HashSet<String>>();

	public ManageScopeSymbol() {
		enterScope();
	}

	@Override
	public void enterScope() {
		stkScope.add(0, new HashSet<String>());
	}

	@Override
	public void leaveScope() {
		stkScope.remove(0);
	}

	@Override
	public boolean findDeclaredSymbol(String name) {
		for (HashSet<String> hashSet : stkScope) {
			if (hashSet.contains(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isUniqueSymbolOfBlock(String name) {
		return stkScope.get(0).contains(name);
	}

	@Override
	public String getEntryName() {
		return ENTRY_NAME;
	}

	@Override
	public Token getEntryToken() {
		Token token = new Token();
		token.kToken = TokenType.ID;
		token.object = getEntryName();
		token.position = new Position();
		return token;
	}

	@Override
	public Function getFuncByName(String name) {
		return funcMap.get(name);
	}

	@Override
	public void registerSymbol(String name) {
		stkScope.get(0).add(name);
		symbolList.add(name);
	}

	@Override
	public void registeFunc(String name, Function func) {
		if (func.getName().kToken == TokenType.ID) {
			func.setRealName(func.getName().toRealString());
			symbolList.add(func.getRealName());
		} else {
			func.setRealName(LAMBDA_PREFIX + lambdaId);
		}
		funcMap.add(func.getRealName(), func);
	}

	@Override
	public boolean isRegisteredFunc(String name) {
		Function func = funcMap.get(name);
		if (func == null) {
			return false;
		}
		return func.getRealName() != null;
	}

	public String getSymbolString() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 符号表 ####");
		sb.append(System.getProperty("line.separator"));
		int i = 0;
		for (String symbol : symbolList.list) {
			sb.append(i + ": " + symbol);
			sb.append(System.getProperty("line.separator"));
			i++;
		}
		return sb.toString();
	}

	public String getFuncString() {
		StringBuffer sb = new StringBuffer();
		sb.append("#### 过程表 ####");
		sb.append(System.getProperty("line.separator"));
		int i = 0;
		for (Function func : funcMap.list) {
			sb.append(System.getProperty("line.separator"));
			sb.append("----==== #" + i + " ====----");
			sb.append(System.getProperty("line.separator"));
			sb.append(func.toString());
			sb.append(System.getProperty("line.separator"));
			i++;
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getSymbolString());
		sb.append(getFuncString());
		return sb.toString();
	}
}
