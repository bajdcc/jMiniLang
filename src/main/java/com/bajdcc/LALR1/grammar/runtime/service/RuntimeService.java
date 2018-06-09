package com.bajdcc.LALR1.grammar.runtime.service;

import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess;

/**
 * 【运行时】运行时服务
 *
 * @author bajdcc
 */
public class RuntimeService implements IRuntimeService {

	private RuntimeProcess process;
	private RuntimePipeService pipe;
	private RuntimeShareService share;
	private RuntimeFileService file;
	private RuntimeDialogService dialog;

	public RuntimeService(RuntimeProcess process) {
		this.process = process;
		this.pipe = new RuntimePipeService(this);
		this.share = new RuntimeShareService(this);
		this.file = new RuntimeFileService(this);
		this.dialog = new RuntimeDialogService(this);
	}

	@Override
	public IRuntimePipeService getPipeService() {
		return pipe;
	}

	@Override
	public IRuntimeShareService getShareService() {
		return share;
	}

	@Override
	public IRuntimeProcessService getProcessService() {
		return process;
	}

	@Override
	public IRuntimeFileService getFileService() {
		return file;
	}

	@Override
	public IRuntimeDialogService getDialogService() {
		return dialog;
	}
}
