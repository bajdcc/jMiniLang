package com.bajdcc.util.intervalTree

import java.util.*

/**
 * An Interval Tree is essentially a map from intervals to objects, which
 * can be queried for all data associated with a particular interval of
 * time
 *
 * @param <Type> the type of objects to associate
 * @author Kevin Dolan
</Type> */
class IntervalTree<Type>(intervalList: List<Interval<Type>>) {

    private var head: IntervalNode<Type>
    private var intervalList: MutableList<Interval<Type>> = ArrayList()
    private var inSync: Boolean = true
    private var size: Int = 0

    init {
        this.head = IntervalNode(intervalList)
        this.intervalList.addAll(intervalList)
        this.size = intervalList.size
    }

    /**
     * Perform a stabbing query, returning the associated data
     * Will rebuild the tree if out of sync
     *
     * @param time the time to stab
     * @return the data associated with all intervals that contain time
     */
    operator fun get(time: Long): List<Type> {
        val intervals = getIntervals(time)
        val result = ArrayList<Type>()
        for (interval in intervals)
            result.add(interval.data!!)
        return result
    }

    /**
     * Perform a stabbing query, returning the interval objects
     * Will rebuild the tree if out of sync
     *
     * @param time the time to stab
     * @return all intervals that contain time
     */
    fun getIntervals(time: Long): List<Interval<Type>> {
        build()
        return head.stab(time)
    }

    /**
     * Perform an interval query, returning the associated data
     * Will rebuild the tree if out of sync
     *
     * @param start the start of the interval to check
     * @param end   the end of the interval to check
     * @return the data associated with all intervals that intersect target
     */
    operator fun get(start: Long, end: Long): List<Type> {
        val intervals = getIntervals(start, end)
        val result = ArrayList<Type>()
        for (interval in intervals)
            result.add(interval.data!!)
        return result
    }

    /**
     * Perform an interval query, returning the interval objects
     * Will rebuild the tree if out of sync
     *
     * @param start the start of the interval to check
     * @param end   the end of the interval to check
     * @return all intervals that intersect target
     */
    fun getIntervals(start: Long, end: Long): List<Interval<Type>> {
        build()
        return head.query(Interval<Type>(start, end, null))
    }

    /**
     * Add an interval object to the interval tree's list
     * Will not rebuild the tree until the next query or call to build
     *
     * @param interval the interval object to add
     */
    fun addInterval(interval: Interval<Type>) {
        intervalList.add(interval)
        inSync = false
    }

    /**
     * Add an interval object to the interval tree's list
     * Will not rebuild the tree until the next query or call to build
     *
     * @param begin the beginning of the interval
     * @param end   the end of the interval
     * @param data  the data to associate
     */
    fun addInterval(begin: Long, end: Long, data: Type) {
        intervalList.add(Interval(begin, end, data))
        inSync = false
    }

    /**
     * Determine whether this interval tree is currently a reflection of all intervals in the interval list
     *
     * @return true if no changes have been made since the last build
     */
    fun inSync(): Boolean {
        return inSync
    }

    /**
     * Build the interval tree to reflect the list of intervals,
     * Will not run if this is currently in sync
     */
    fun build() {
        if (!inSync) {
            head = IntervalNode(intervalList)
            inSync = true
            size = intervalList.size
        }
    }

    /**
     * @return the number of entries in the currently built interval tree
     */
    fun currentSize(): Int {
        return size
    }

    /**
     * @return the number of entries in the interval list, equal to .size() if inSync()
     */
    fun listSize(): Int {
        return intervalList.size
    }

    override fun toString(): String {
        return nodeString(head, 0)
    }

    private fun nodeString(node: IntervalNode<Type>?, level: Int): String {
        if (node == null)
            return ""

        val sb = StringBuilder()
        for (i in 0 until level)
            sb.append("\t")
        sb.append(node).append("\n")
        sb.append(nodeString(node.left, level + 1))
        sb.append(nodeString(node.right, level + 1))
        return sb.toString()
    }
}
