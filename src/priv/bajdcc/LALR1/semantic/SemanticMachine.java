package priv.bajdcc.LALR1.semantic;

import java.util.List;

import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.symbol.IManageSymbol;
import priv.bajdcc.LALR1.grammar.symbol.IQuerySymbol;
import priv.bajdcc.LALR1.semantic.token.IRandomAccessOfTokens;
import priv.bajdcc.LALR1.semantic.token.ISemanticAction;
import priv.bajdcc.LALR1.semantic.token.ISemanticAnalyzier;
import priv.bajdcc.LALR1.semantic.token.ParsingStack;
import priv.bajdcc.LALR1.semantic.tracker.Instruction;
import priv.bajdcc.LALR1.syntax.rule.RuleItem;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】语义指令运行时机器
 *
 * @author bajdcc
 */
public class SemanticMachine implements IRandomAccessOfTokens {

	/**
	 * 单词索引
	 */
	private int idxToken = 0;

	/**
	 * 语义动作接口
	 */
	private ISemanticAction action = null;

	/**
	 * 语义处理接口
	 */
	private ISemanticAnalyzier handler = null;

	/**
	 * 规则集合
	 */
	private List<RuleItem> items = null;

	/**
	 * 语义动作集合
	 */
	private List<ISemanticAction> actions = null;

	/**
	 * 单词流
	 */
	private List<Token> tokens = null;

	/**
	 * 数据处理堆栈
	 */
	private ParsingStack ps = new ParsingStack();

	/**
	 * 符号表查询接口
	 */
	private IQuerySymbol query = null;

	/**
	 * 符号表管理接口
	 */
	private IManageSymbol manage = null;

	/**
	 * 语义错误处理接口
	 */
	private ISemanticRecorder recorder = null;

	/**
	 * 调试
	 */
	private boolean bDebug = false;

	/**
	 * 结果
	 */
	private Object object = null;

	public SemanticMachine(List<RuleItem> items, List<ISemanticAction> actions,
			List<Token> tokens, IQuerySymbol query, IManageSymbol manage,
			ISemanticRecorder recorder, boolean debug) {
		this.items = items;
		this.actions = actions;
		this.tokens = tokens;
		this.query = query;
		this.manage = manage;
		this.recorder = recorder;
	}

	/**
	 * 运行一个指令
	 * 
	 * @param inst
	 *            指令
	 */
	public void run(Instruction inst) {
		/* 重置处理机制 */
		handler = null;
		action = null;
		/* 处理前 */
		if (inst.iHandler != -1) {
			switch (inst.inst) {
			case PASS:
			case READ:
			case SHIFT:
				action = actions.get(inst.iHandler);
				break;
			default:
				handler = items.get(inst.iHandler).handler;
				break;
			}
		}
		if (action != null) {
			action.handle(ps, manage, this, recorder);
		}
		switch (inst.inst) {
		case PASS:
			idxToken++;
			break;
		case READ:
			ps.set(inst.iIndex, tokens.get(idxToken));
			idxToken++;
			break;
		case SHIFT:
			ps.push();
			break;
		default:
			break;
		}
		/* 处理时 */
		if (handler != null) {
			object = handler.handle(ps, query, recorder);
		}
		/* 处理后 */
		switch (inst.inst) {
		case LEFT_RECURSION:
			ps.pop();// 先pop再push为了让栈层成为current的引用
			ps.push();
			ps.set(inst.iIndex, object);
			break;
		case LEFT_RECURSION_DISCARD:
			ps.pop();
			ps.push();
			break;
		case PASS:
			break;
		case READ:
			break;
		case SHIFT:
			break;
		case TRANSLATE:
			ps.pop();
			ps.set(inst.iIndex, object);
			break;
		case TRANSLATE_DISCARD:
			ps.pop();
			break;
		case TRANSLATE_FINISH:
			ps.pop();
			break;
		default:
			break;
		}
		/* 打印调试信息 */
		if (bDebug) {
			System.err.println("#### " + inst.toString() + " ####");
			System.err.println(ps.toString());
			System.err.println();
		}
	}

	public Object getObject() {
		return object;
	}

	@Override
	public Token relativeGet(int index) {
		return tokens.get(idxToken + index);
	}

	@Override
	public Token positiveGet(int index) {
		return tokens.get(index);
	}

	@Override
	public void relativeSet(int index, Token token) {
		tokens.set(idxToken + index, token);
	}

	@Override
	public void positiveSet(int index, Token token) {
		tokens.set(index, token);
	}
}
