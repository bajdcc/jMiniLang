package com.bajdcc.LL1.syntax.prediction

import com.bajdcc.LL1.syntax.token.PredictType

/**
 * 【LL1预测分析】预测分析指令结构
 *
 * @author bajdcc
 */
class PredictionInstruction(var type: PredictType,
                            var inst: Int) {

    override fun equals(other: Any?): Boolean {
        if (other is PredictionInstruction) {
            val pi = other as PredictionInstruction?
            return type == pi!!.type && inst == pi.inst
        }
        return false
    }

    override fun toString(): String {
        return type.desc + ": " + inst
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + inst
        return result
    }
}
