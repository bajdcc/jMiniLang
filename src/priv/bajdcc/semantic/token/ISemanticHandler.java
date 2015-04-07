package priv.bajdcc.semantic.token;

/**
 *语义分析接口
 *
 * @author bajdcc
 */
public interface ISemanticHandler {

	/**
	 * 语义处理接口
	 * @param indexed 索引包接口
	 * @param factory 单词工厂接口
	 * @param obj 对象
	 * @return 处理后的数据
	 */
	public Object handle(IIndexedData indexed, ITokenFactory factory, Object obj);
}
