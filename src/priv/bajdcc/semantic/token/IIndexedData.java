package priv.bajdcc.semantic.token;

/**
 * 索引数据操作接口
 *
 * @author bajdcc
 */
public interface IIndexedData {
	
	/**
	 * 获得对应位置的单词包
	 * @param index 索引
	 * @return 单词包
	 */
	public TokenBag get(int index);
}
