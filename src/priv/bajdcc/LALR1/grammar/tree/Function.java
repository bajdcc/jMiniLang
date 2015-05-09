package priv.bajdcc.LALR1.grammar.tree;

import java.util.ArrayList;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.util.lexer.token.KeywordType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】函数
 *
 * @author bajdcc
 */
public class Function implements IExp {

	/**
	 * 名称
	 */
	private Token name = null;

	/**
	 * 真实名称（Lambda记号）
	 */
	private String realName = null;

	/**
	 * 参数
	 */
	private ArrayList<Token> params = new ArrayList<Token>();

	/**
	 * 函数主体Block
	 */
	private Block block = null;

	/**
	 * 文档
	 */
	private ArrayList<String> doc = null;

	public Token getName() {
		return name;
	}

	public void setName(Token name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public ArrayList<Token> getParams() {
		return params;
	}

	public void setParams(ArrayList<Token> params) {
		this.params = params;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public ArrayList<String> getDoc() {
		return doc;
	}

	public void setDoc(ArrayList<String> doc) {
		this.doc = doc;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {

	}

	@Override
	public void genCode(ICodegen codegen) {

	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(KeywordType.FUNCTION.getName() + " " + realName);
		sb.append("(");
		for (Token param : params) {
			sb.append(param.toRealString());
			sb.append(",");
		}
		if (!params.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(") ");
		sb.append(block.print(prefix));
		return sb.toString();
	}
}
