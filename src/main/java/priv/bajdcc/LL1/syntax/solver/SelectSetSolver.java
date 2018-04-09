package priv.bajdcc.LL1.syntax.solver;

import priv.bajdcc.LL1.syntax.ISyntaxComponentVisitor;
import priv.bajdcc.LL1.syntax.exp.BranchExp;
import priv.bajdcc.LL1.syntax.exp.RuleExp;
import priv.bajdcc.LL1.syntax.exp.SequenceExp;
import priv.bajdcc.LL1.syntax.exp.TokenExp;
import priv.bajdcc.LL1.syntax.token.PredictType;
import priv.bajdcc.util.VisitBag;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.Collection;

/**
 * 求解一个产生式的Select集合
 *
 * @author bajdcc
 */
public abstract class SelectSetSolver implements ISyntaxComponentVisitor {

	/**
	 * 是否为产生式的第一个终结符
	 */
	private boolean firstSymbol = false;

	/**
	 * 是否需要把当前符号添加进指令集
	 */
	private boolean insertSymbol = false;

	/**
	 * 当前产生式从左到右片段是否产生空串
	 */
	private boolean epsilon = true;

	/**
	 * 当前产生式规则是否可以推导出空串
	 *
	 * @return 是否可以推导出空串
	 */
	protected abstract boolean isEpsilon();

	/**
	 * 获得当前产生式左部的Follow集
	 *
	 * @return Follow集
	 */
	protected abstract Collection<TokenExp> getFollow();

	/**
	 * 设置预测分析表的某一项为当前产生式
	 *
	 * @param token 列索引，终结符
	 */
	protected abstract void setCellToRuleId(int token);

	/**
	 * 当前处理的规则ID自增（一条产生式有多个规则）
	 */
	protected abstract void addRule();

	/**
	 * 给当前处理的规则添加非终结符指令（预测分析表中的产生式规则）
	 *
	 * @param type 指令类型（VT或VN）
	 * @param inst 指令ID（索引）
	 */
	protected abstract void addInstToRule(PredictType type, int inst);

	@Override
	public void visitBegin(TokenExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (node.kType == TokenType.EOF) {// Epsilon
			addRule();// 空串，则指令集为空（不压入新状态）
			for (TokenExp token : getFollow()) {// 有空串，添加Follow集
				setCellToRuleId(token.id);
			}
			firstSymbol = false;
		} else if (firstSymbol) {
			addRule();// 需要添加指令集
			setCellToRuleId(node.id);
			firstSymbol = false;
			insertSymbol = true;
		}
		if (insertSymbol) {
			addInstToRule(PredictType.TERMINAL, node.id);
		}
		if (node.kType != TokenType.EOF) {
			epsilon = false;
		}
	}

	@Override
	public void visitBegin(RuleExp node, VisitBag bag) {
		bag.bVisitChildren = false;
		bag.bVisitEnd = false;
		if (firstSymbol) {
			addRule();// 需要添加指令集
			insertSymbol = true;
			firstSymbol = false;
		}
		if (insertSymbol) {
			if (epsilon) {
				// 添加First集
				node.rule.arrFirsts
						.stream()
						.filter(token -> token.kType != TokenType.EOF)
						.forEach(token -> setCellToRuleId(token.id));
			}
		}
		addInstToRule(PredictType.NONTERMINAL, node.id);
		if (!node.rule.epsilon) {
			epsilon = false;
		}
	}

	@Override
	public void visitBegin(SequenceExp node, VisitBag bag) {
		bag.bVisitEnd = false;
		firstSymbol = true;
		insertSymbol = false;
		epsilon = true;
	}

	@Override
	public void visitBegin(BranchExp node, VisitBag bag) {
		bag.bVisitEnd = false;
	}

	@Override
	public void visitEnd(TokenExp node) {

	}

	@Override
	public void visitEnd(RuleExp node) {

	}

	@Override
	public void visitEnd(SequenceExp node) {

	}

	@Override
	public void visitEnd(BranchExp node) {

	}
}
