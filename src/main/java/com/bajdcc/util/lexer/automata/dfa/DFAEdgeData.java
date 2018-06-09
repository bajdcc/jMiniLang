package com.bajdcc.util.lexer.automata.dfa;

import com.bajdcc.util.lexer.automata.nfa.NFAEdge;
import com.bajdcc.util.lexer.automata.nfa.NFAEdgeData;

import java.util.ArrayList;

/**
 * DFA边数据
 *
 * @author bajdcc
 */
public class DFAEdgeData extends NFAEdgeData {
	/**
	 * NFA边集合
	 */
	public ArrayList<NFAEdge> nfaEdges = new ArrayList<>();
}
