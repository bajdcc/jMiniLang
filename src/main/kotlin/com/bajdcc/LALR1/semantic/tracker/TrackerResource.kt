package com.bajdcc.LALR1.semantic.tracker

/**
 * 跟踪器资源（链表）
 *
 * @author bajdcc
 */
class TrackerResource {
    /**
     * 跟踪器链表头
     */
    var head: Tracker? = null

    /**
     * 跟踪器链表尾
     */
    var tail: Tracker? = null

    /**
     * 添加指令记录
     *
     * @param prev 前驱记录
     * @return 新的指令记录
     */
    fun addInstRecord(prev: InstructionRecord?): InstructionRecord {
        return InstructionRecord(prev)
    }

    /**
     * 添加错误记录
     *
     * @param prev 前驱记录
     * @return 新的错误记录
     */
    fun addErrorRecord(prev: ErrorRecord?): ErrorRecord {
        return ErrorRecord(prev)
    }

    /**
     * 添加跟踪器
     *
     * @return 新的跟踪器
     */
    fun addTracker(): Tracker {
        val tracker = Tracker()
        if (head != null) {
            /* 将新的跟踪器插入表首 */
            tracker.next = head
            head!!.prev = tracker
            head = tracker
        } else {
            tail = tracker
            head = tail
        }
        return tracker
    }

    /**
     * 释放跟踪器
     *
     * @param tracker 不需要的跟踪器
     */
    fun freeTracker(tracker: Tracker) {
        if (tracker === head) {
            if (tracker === tail) {
                tail = null
                head = tail// 删除链表中的唯一项
            } else {
                head = head!!.next
                head!!.prev = null
            }
        } else if (tracker === tail) {
            tail = tail!!.prev
            tail!!.next = null
        } else {
            tracker.next!!.prev = tracker.prev
            tracker.prev!!.next = tracker.next
        }
    }
}
