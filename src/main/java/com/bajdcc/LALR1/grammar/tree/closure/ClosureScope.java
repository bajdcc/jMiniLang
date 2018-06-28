package com.bajdcc.LALR1.grammar.tree.closure;

import java.util.HashSet;
import java.util.Set;

/**
 * 闭包
 *
 * @author bajdcc
 */
public class ClosureScope implements IClosureScope {

	private Set<Object> ref = new HashSet<>();
	private Set<Object> decl = new HashSet<>();

	@Override
	public void addRef(Object obj) {
		ref.add(obj);
	}

	@Override
	public void addDecl(Object obj) {
		decl.add(obj);
	}

	protected Set<Object> getClosure() {
		Set<Object> closure = new HashSet<>(ref);
		closure.removeAll(decl);
		return closure.isEmpty() ? null : closure;
	}
}
