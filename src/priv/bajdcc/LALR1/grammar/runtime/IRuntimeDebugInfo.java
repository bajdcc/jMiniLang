package priv.bajdcc.LALR1.grammar.runtime;

import java.util.HashMap;

/**
 * 【扩展】调试、本地化开发接口
 *
 * @author bajdcc
 */
public interface IRuntimeDebugInfo {

	/**
	 * 返回数据存储
	 *
	 * @return 数据存储
	 */
	HashMap<String, Object> getDataMap();
	
	/**
	 * 根据当前指令页地址找到函数名
	 * 
	 * @param addr
	 *            地址
	 * @return 函数名
	 */
	String getFuncNameByAddress(int addr);

	/**
	 * 根据导出的函数名找到地址
	 * 
	 * @param name
	 *            函数名
	 * @return 地址
	 */
	int getAddressOfExportFunc(String name);

	/**
	 * 根据自定义参数名称找到本地值导出接口
	 * 
	 * @param name
	 *            自定义参数名称
	 * @return 本地值导出接口
	 */
	IRuntimeDebugValue getValueCallByName(String name);

	/**
	 * 根据自定义参数名称找到本地过程导出接口
	 * 
	 * @param name
	 *            自定义参数名称
	 * @return 本地过程导出接口
	 */
	IRuntimeDebugExec getExecCallByName(String name);

	/**
	 * 添加外部变量
	 * 
	 * @param name
	 *            变量名
	 * @param func
	 *            调用过程
	 * @return 是否冲突
	 */
	boolean addExternalValue(String name, IRuntimeDebugValue func);

	/**
	 * 添加外部过程
	 * 
	 * @param name
	 *            过程名
	 * @param func
	 *            调用过程
	 * @return 是否冲突
	 */
	boolean addExternalFunc(String name, IRuntimeDebugExec func);
}
