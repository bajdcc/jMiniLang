package priv.bajdcc.LL1.syntax.prediction;

import priv.bajdcc.LL1.syntax.token.PredictType;

/**
 * 【LL1预测分析】预测分析指令结构
 *
 * @author bajdcc
 */
public class PredictionInstruction {

	/**
	 * 指令类型
	 */
	public PredictType type;

	/**
	 * 指令数据
	 */
	public int inst;

	public PredictionInstruction(PredictType type, int inst) {
		this.type = type;
		this.inst = inst;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PredictionInstruction) {
			PredictionInstruction pi = (PredictionInstruction) obj;
			return type == pi.type && inst == pi.inst;
		}
		return false;
	}

	@Override
	public String toString() {
		return type.getName() + ": " + inst;
	}
}
