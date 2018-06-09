package com.bajdcc.util.lexer.automata.dfa;

import com.bajdcc.util.lexer.automata.nfa.NFAStatus;
import com.bajdcc.util.lexer.automata.nfa.NFAStatusData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * DFA状态数据
 *
 * @author bajdcc
 */
public class DFAStatusData extends NFAStatusData {
	/**
	 * NFA状态集合
	 */
	public ArrayList<NFAStatus> nfaStatus = new ArrayList<>();

	/**
	 * 获得状态编号描述（逗号分隔）
	 *
	 * @param dfaStatusList 状态表
	 * @return 状态编号描述
	 */
	public String getStatusString(ArrayList<NFAStatus> dfaStatusList) {
		if (nfaStatus.isEmpty()) {
			return "";
		}
		int[] orders = new int[nfaStatus.size()];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = dfaStatusList.indexOf(nfaStatus.get(i));
		}
		Arrays.sort(orders);
		StringBuilder sb = new StringBuilder();
		for (int order : orders) {
			sb.append(order).append(",");
		}
		return sb.toString();
	}
}
