package com.bajdcc.util.lexer.automata.dfa;

import com.bajdcc.util.lexer.automata.nfa.NFAStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DFA边辅助数据结构
 *
 * @author bajdcc
 */
public class DFAEdgeBag extends DFAEdgeData {
	/**
	 * NFA状态集合
	 */
	public Set<NFAStatus> nfaStatus = new HashSet<>();

	/**
	 * 获得状态编号描述（逗号分隔）
	 *
	 * @param dfaStatusList 状态表
	 * @return 状态编号描述
	 */
	public String getStatusString(List<NFAStatus> dfaStatusList) {
		StringBuilder sb = new StringBuilder();
		for (NFAStatus status : nfaStatus) {
			sb.append(dfaStatusList.indexOf(status)).append(",");
		}
		return sb.toString();
	}
}
