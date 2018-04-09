package priv.bajdcc.LALR1.grammar.symbol;

/**
 * 块查询接口
 *
 * @author bajdcc
 */
public interface IQueryBlockSymbol {

	/**
	 * 进入循环体
	 *
	 * @param type 块类型
	 */
	void enterBlock(BlockType type);

	/**
	 * 离开循环体
	 *
	 * @param type 块类型
	 */
	void leaveBlock(BlockType type);

	/**
	 * 是否在循环体内
	 *
	 * @param type 块类型
	 * @return 在循环体内则为真
	 */
	boolean isInBlock(BlockType type);
}