package priv.bajdcc.lexer.automata.dfa;

import java.util.ArrayList;

import priv.bajdcc.lexer.automata.nfa.NFAEdge;
import priv.bajdcc.lexer.automata.nfa.NFAEdgeData;

/**
 * DFA边数据
 * 
 * @author bajdcc
 *
 */
public class DFAEdgeData extends NFAEdgeData {
	/**
	 * NFA边集合
	 */
	public ArrayList<NFAEdge> m_NFAEdges = new ArrayList<NFAEdge>();
}
