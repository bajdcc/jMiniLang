package priv.bajdcc.lexer.automata.dfa;

import java.util.ArrayList;
import java.util.HashSet;

import priv.bajdcc.lexer.automata.nfa.NFAStatus;

/**
 * DFA边辅助数据结构
 * @author bajdcc
 *
 */
public class DFAEdgeBag extends DFAEdgeData{
	/**
	 * NFA状态集合
	 */
	public HashSet<NFAStatus> m_NFAStatus = new HashSet<NFAStatus>();
	
	/**
	 * 获得状态编号描述（逗号分隔）
	 */
	public String getStatusString(ArrayList<NFAStatus> nfaStatusList) {
		StringBuilder sb = new StringBuilder();
		for (NFAStatus status : m_NFAStatus) {
			sb.append(nfaStatusList.indexOf(status) + ",");
		}
		return sb.toString();
	}
}
