package priv.bajdcc.LALR1.grammar.runtime.service;

/**
 * 【运行时】运行时服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeService {

	IRuntimePipeService getPipeService();

	IRuntimeShareService getShareService();

	IRuntimeProcessService getProcessService();

	IRuntimeFileService getFileService();

	IRuntimeDialogService getDialogService();
}
