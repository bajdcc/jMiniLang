package com.bajdcc.LALR1.syntax.automata.npa

/**
 *
 *
 * 非确定性下推自动机边类型
 *
 * Move ------------ (Start,Epsilon,[Token]) ----&gt; (End,Epsilon)<br></br>
 * Shift ----------- (Start,Epsilon,Epsilon) ----&gt; (End,Start)<br></br>
 * Reduce ---------- (Start,[Status],Epsilon) ---&gt; (End,Epsilon)<br></br>
 * Left Recursion -- (Start,Epsilon,Epsilon) ----&gt; (End,Epsilon)<br></br>
 * Finish ---------- (Start,Epsilon,Epsilon) ----&gt; (Epsilon,Epsilon)<br></br>
 *
 * @author bajdcc
 */
enum class NPAEdgeType constructor(var desc: String?) {
    MOVE("匹配"),
    SHIFT("转移"),
    REDUCE("归约"),
    LEFT_RECURSION("左递归"),
    FINISH("结束")
}
