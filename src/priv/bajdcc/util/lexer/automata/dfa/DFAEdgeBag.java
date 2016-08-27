package priv.bajdcc.util.lexer.automata.dfa;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.util.lexer.automata.nfa.NFAStatus;

/**
 * DFA边辅助数据结构
 * @author bajdcc
 *
 */
public class DFAEdgeBag extends DFAEdgeData{
	/**
	 * NFA状态集合
	 */
	public HashSet<NFAStatus> nfaStatus = new HashSet<>();
	
	/**
	 * 获得状态编号描述（逗号分隔）
	 */
	public String getStatusString(ArrayList<NFAStatus> nfaStatusList) {
		StringBuilder sb = new StringBuilder();
		for (NFAStatus status : nfaStatus) {
			sb.append(nfaStatusList.indexOf(status)).append(",");
		}
		return sb.toString();
	}
}
