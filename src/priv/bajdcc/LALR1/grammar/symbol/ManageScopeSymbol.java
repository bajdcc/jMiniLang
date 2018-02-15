package priv.bajdcc.LALR1.grammar.symbol;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.LALR1.grammar.type.TokenTools;
import priv.bajdcc.util.HashListMap;
import priv.bajdcc.util.HashListMapEx;
import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * 命名空间管理
 *
 * @author bajdcc
 */
public class ManageScopeSymbol implements IQueryScopeSymbol, IQueryBlockSymbol,
		IManageDataSymbol, IManageScopeSymbol {

	private static String ENTRY_NAME = "main";
	private static String LAMBDA_PREFIX = "~lambda#";
	private int lambdaId = 0;
	private HashListMap<Object> symbolList = new HashListMap<>();
	private HashListMapEx<String, Function> funcMap = new HashListMapEx<>();
	private ArrayList<HashSet<String>> stkScope = new ArrayList<>();
	private Stack<Integer> stkLambdaId = new Stack<>();
	private Stack<Integer> stkLambdaLine = new Stack<>();
	private HashSet<String> symbolsInFutureBlock = new HashSet<>();
	private HashMap<BlockType, Integer> blockLevel = new HashMap<>();
	private Stack<BlockType> blockStack = new Stack<>();

	public ManageScopeSymbol() {
		enterScope();
		funcMap.add(ENTRY_NAME, new Function());
		for (BlockType type : BlockType.values()) {
			blockLevel.put(type, 0);
		}
	}

	@Override
	public void enterScope() {
		stkScope.add(0, new HashSet<>());
		symbolsInFutureBlock.forEach(this::registerSymbol);
		clearFutureArgs();
	}

	@Override
	public void leaveScope() {
		stkScope.remove(0);
		clearFutureArgs();
	}

	@Override
	public void clearFutureArgs() {
		symbolsInFutureBlock.clear();
	}

	@Override
	public boolean findDeclaredSymbol(String name) {
		if (symbolsInFutureBlock.contains(name)) {
			return true;
		}
		for (HashSet<String> hashSet : stkScope) {
			if (hashSet.contains(name)) {
				return true;
			}
		}
		if (TokenTools.isExternalName(name)) {
			registerSymbol(name);
			return true;
		}
		return false;
	}

	@Override
	public boolean findDeclaredSymbolDirect(String name) {
		return symbolsInFutureBlock.contains(name) || stkScope.get(0).contains(name);
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
		Function func = funcMap.get(name);
		if (func != null) {
			return func;
		}
		int count = 0;
		for (Function function : funcMap.list) {
			String funcName = function.getRealName();
			if (funcName != null && funcName.equals(name)) {
				func = function;
				count++;
			}
		}
		// count=1 即无歧义, count>1 有歧义
		return (count == 1) ? func : null;
	}

	@Override
	public Function getFuncByRealName(String name) {
		for (Function func : funcMap.list) {
			if (name.equals(func.getRealName())) {
				return func;
			}
		}
		return null;
	}

	@Override
	public Function getLambda() {
		int lambdaId = stkLambdaId.pop();
		int lambdaLine = stkLambdaLine.pop();
		return funcMap.get(LAMBDA_PREFIX + lambdaId + "!" + lambdaLine);
	}

	@Override
	public void registerSymbol(String name) {
		stkScope.get(0).add(name);
		symbolList.add(name);
	}

	@Override
	public void registerFunc(Function func) {
		if (func.getName().kToken == TokenType.ID) {
			func.setRealName(func.getName().toRealString());
			symbolList.add(func.getRealName());
		} else {
			func.setRealName(LAMBDA_PREFIX + lambdaId++);
		}
		funcMap.add(func.getRealName(), func);
	}

	@Override
	public void registerLambda(Function func) {
		stkLambdaId.push(lambdaId);
		stkLambdaLine.push(func.getName().position.iLine);
		func.getName().kToken = TokenType.ID;
		func.setRealName(LAMBDA_PREFIX + (lambdaId++) + "!" + stkLambdaLine.peek());
		funcMap.add(func.getRealName(), func);
	}

	@Override
	public boolean isRegisteredFunc(String name) {
		Function func = funcMap.get(name);
		return func != null && func.getRealName() != null;
	}

	@Override
	public boolean registerFutureSymbol(String name) {
		return symbolsInFutureBlock.add(name);
	}

	public void check(ISemanticRecorder recorder) {
		for (Function func : funcMap.list) {
			func.analysis(recorder);
		}
	}

	@Override
	public HashListMap<Object> getSymbolList() {
		return symbolList;
	}

	@Override
	public HashListMapEx<String, Function> getFuncMap() {
		return funcMap;
	}

	public String getSymbolString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 符号表 ####");
		sb.append(System.lineSeparator());
		int i = 0;
		for (Object symbol : symbolList.list) {
			sb.append(i).append(": ").append("[").append(RuntimeObject.fromObject(symbol).getName()).append("] ").append(symbol);
			sb.append(System.lineSeparator());
			i++;
		}
		return sb.toString();
	}

	public String getFuncString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### 过程表 ####");
		sb.append(System.lineSeparator());
		int i = 0;
		for (Function func : funcMap.list) {
			sb.append("----==== #").append(i).append(" ====----");
			sb.append(System.lineSeparator());
			sb.append(func.toString());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			i++;
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		String sb = getSymbolString() +
				getFuncString();
		return sb;
	}

	@Override
	public void enterBlock(BlockType type) {
		switch (type) {
			case kCycle:
				int level = blockLevel.get(type);
				blockLevel.put(type, level + 1);
				break;
			case kFunc:
			case kYield:
				blockStack.push(type);
				break;
			default:
				break;

		}
	}

	@Override
	public void leaveBlock(BlockType type) {
		switch (type) {
			case kCycle:
				int level = blockLevel.get(type);
				blockLevel.put(type, level - 1);
				break;
			case kFunc:
			case kYield:
				if (blockStack.peek() == type) {
					blockStack.pop();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public boolean isInBlock(BlockType type) {
		switch (type) {
			case kCycle:
				return blockLevel.get(type) > 0;
			case kFunc:
			case kYield:
				return !blockStack.isEmpty() && (blockStack.peek() == type);
			default:
				break;
		}
		return false;
	}
}
