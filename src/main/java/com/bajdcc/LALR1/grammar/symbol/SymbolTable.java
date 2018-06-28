package com.bajdcc.LALR1.grammar.symbol;

import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;

/**
 * 【语义分析】符号表
 *
 * @author bajdcc
 */
public class SymbolTable implements IQuerySymbol, IManageSymbol {

	/**
	 * 符号表管理
	 */
	private ManageScopeSymbol manageScopeSymbol = new ManageScopeSymbol();

	@Override
	public IManageScopeSymbol getManageScopeService() {
		return manageScopeSymbol;
	}

	@Override
	public IQueryScopeSymbol getQueryScopeService() {
		return manageScopeSymbol;
	}

	@Override
	public IManageDataSymbol getManageDataService() {
		return manageScopeSymbol;
	}

	public void check(ISemanticRecorder recorder) {
		manageScopeSymbol.check(recorder);
	}

	@Override
	public String toString() {
		return manageScopeSymbol.toString();
	}

	@Override
	public IQueryBlockSymbol getQueryBlockService() {
		return manageScopeSymbol;
	}

	@Override
	public IManageSymbol getManageService() {
		return this;
	}
}
