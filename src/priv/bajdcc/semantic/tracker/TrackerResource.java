package priv.bajdcc.semantic.tracker;

import java.util.ArrayList;
import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.utility.ObjectFactory;

/**
 * 跟踪器资源（链表）
 *
 * @author bajdcc
 */
public class TrackerResource {
	/**
	 * 指令记录集
	 */
	private ArrayList<InstructionRecord> m_arrInstRecords = new ArrayList<InstructionRecord>();

	/**
	 * 错误记录集
	 */
	private ArrayList<ErrorRecord> m_arrErrorRecords = new ArrayList<ErrorRecord>();

	/**
	 * 跟踪器池
	 */
	private PoolService<Tracker> m_poolTrackers = new ConcurrentLinkedPool<Tracker>(
			new ObjectFactory<Tracker>() {
				public Tracker create() {
					return new Tracker();
				};
			}, 10, 1000, false);

	/**
	 * 跟踪器链表头
	 */
	public Tracker m_headTracker = null;

	/**
	 * 跟踪器链表尾
	 */
	public Tracker m_tailTracker = null;

	/**
	 * 添加指令记录
	 * 
	 * @param prev
	 *            前驱记录
	 * @return 新的指令记录
	 */
	public InstructionRecord addInstRecord(InstructionRecord prev) {
		InstructionRecord record = new InstructionRecord(prev);
		m_arrInstRecords.add(record);
		return record;
	}

	/**
	 * 添加错误记录
	 * 
	 * @param prev
	 *            前驱记录
	 * @return 新的错误记录
	 */
	public ErrorRecord addErrorRecord(ErrorRecord prev) {
		ErrorRecord record = new ErrorRecord(prev);
		m_arrErrorRecords.add(record);
		return record;
	}

	/**
	 * 添加跟踪器
	 * 
	 * @return 新的跟踪器
	 * 
	 */
	public Tracker addTracker() {
		Tracker tracker = m_poolTrackers.take();
		if (m_headTracker != null) {
			/* 将新的跟踪器插入表首 */
			tracker.m_nextTracker = m_headTracker;
			m_headTracker.m_prevTracker = tracker;
			m_headTracker = tracker;
		} else {
			m_headTracker = m_tailTracker = tracker;
		}
		return tracker;
	}

	/**
	 * 释放跟踪器
	 * 
	 * @param tracker
	 *            不需要的跟踪器
	 */
	public void freeTracker(Tracker tracker) {
		if (tracker == m_headTracker) {
			if (tracker == m_tailTracker) {
				m_headTracker = m_tailTracker = null;// 删除链表中的唯一项
			} else {
				m_headTracker = m_headTracker.m_nextTracker;
				m_headTracker.m_prevTracker = null;
			}
		} else if (tracker == m_tailTracker) {
			m_tailTracker = m_tailTracker.m_prevTracker;
			m_tailTracker.m_nextTracker = null;
		} else {
			tracker.m_nextTracker.m_prevTracker = tracker.m_prevTracker;
			tracker.m_prevTracker.m_nextTracker = tracker.m_nextTracker;
		}
		m_poolTrackers.restore(tracker);
	}
}
