package priv.bajdcc.syntax.automata.npa;

import java.util.ArrayList;
import java.util.Iterator;

import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.syntax.automata.nga.NGA;
import priv.bajdcc.syntax.exp.RuleExp;
import priv.bajdcc.utility.ObjectFactory;

/**
 * <p>
 * <strong>非确定性下推自动机</strong>（<b>NPA</b>）构成算法
 * </p>
 * 
 * @author bajdcc
 *
 */
public class NPA extends NGA {

	/**
	 * 边对象池
	 */
	private PoolService<NPAEdge> m_EdgesPool = new ConcurrentLinkedPool<NPAEdge>(
			new ObjectFactory<NPAEdge>() {
				public NPAEdge create() {
					return new NPAEdge();
				};
			}, 1024, 10240, false);

	/**
	 * 状态对象池
	 */
	private PoolService<NPAStatus> m_StatusPool = new ConcurrentLinkedPool<NPAStatus>(
			new ObjectFactory<NPAStatus>() {
				public NPAStatus create() {
					return new NPAStatus();
				};
			}, 1024, 10240, false);

	public NPA(ArrayList<RuleExp> nonterminals) {
		super(nonterminals);
		generateNPA();
	}

	/**
	 * 连接两个状态
	 * 
	 * @param begin
	 *            初态
	 * @param end
	 *            终态
	 * @return 新的边
	 */
	protected NPAEdge connect(NPAStatus begin, NPAStatus end) {
		NPAEdge edge = m_EdgesPool.take();// 申请一条新边
		edge.m_Begin = begin;
		edge.m_End = end;
		begin.m_OutEdges.add(edge);// 添加进起始边的出边
		end.m_InEdges.add(edge);// 添加进结束边的入边
		return edge;
	}

	/**
	 * 断开某个状态和某条边
	 * 
	 * @param status
	 *            某状态
	 * @param edge
	 *            某条边
	 */
	protected void disconnect(NPAStatus status, NPAEdge edge) {
		edge.m_End.m_InEdges.remove(edge);// 当前边的结束状态的入边集合去除当前边
		m_EdgesPool.restore(edge);
	}

	/**
	 * 断开某个状态和所有边
	 * 
	 * @param begin
	 *            某状态
	 */
	protected void disconnect(NPAStatus status) {
		/* 清除所有入边 */
		for (Iterator<NPAEdge> it = status.m_InEdges.iterator(); it.hasNext();) {
			NPAEdge edge = it.next();
			it.remove();
			disconnect(edge.m_Begin, edge);
		}
		/* 清除所有出边 */
		for (Iterator<NPAEdge> it = status.m_OutEdges.iterator(); it.hasNext();) {
			NPAEdge edge = it.next();
			it.remove();
			disconnect(status, edge);
		}
		m_StatusPool.restore(status);
	}

	/**
	 * 产生下推自动机
	 */
	private void generateNPA() {
		
	}
}
