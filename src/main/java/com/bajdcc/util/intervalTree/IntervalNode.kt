package com.bajdcc.util.intervalTree

import java.util.*

/**
 * The Node class contains the interval tree information for one single node
 *
 * @author Kevin Dolan
 */
class IntervalNode<Type>(intervalList: List<Interval<Type>>) {

    private var intervals: SortedMap<Interval<Type>, MutableList<Interval<Type>>> = TreeMap()
    var center: Long = 0
    var left: IntervalNode<Type>? = null
    var right: IntervalNode<Type>? = null

    init {
        val endpoints = TreeSet<Long>()
        for (interval in intervalList) {
            endpoints.add(interval.start)
            endpoints.add(interval.end)
        }
        val median = getMedian(endpoints)!!
        center = median
        val left = ArrayList<Interval<Type>>()
        val right = ArrayList<Interval<Type>>()
        intervalList.forEach { interval ->
            when {
                interval.end < median -> left.add(interval)
                interval.start > median -> right.add(interval)
                else -> {
                    val posting = intervals.computeIfAbsent(interval) { _ -> ArrayList() }
                    posting.add(interval)
                }
            }
        }
        if (left.size > 0)
            this.left = IntervalNode(left)
        if (right.size > 0)
            this.right = IntervalNode(right)
    }

    /**
     * Perform a stabbing query on the node
     *
     * @param time the time to query at
     * @return all intervals containing time
     */
    fun stab(time: Long): List<Interval<Type>> {
        val result = ArrayList<Interval<Type>>()
        for ((key, value) in intervals) {
            if (key.contains(time))
                result.addAll(value)
            else if (key.start > time)
                break
        }

        if (time < center && left != null)
            result.addAll(left!!.stab(time))
        else if (time > center && right != null)
            result.addAll(right!!.stab(time))
        return result
    }

    /**
     * Perform an interval intersection query on the node
     *
     * @param target the interval to intersect
     * @return all intervals containing time
     */
    fun query(target: Interval<*>): List<Interval<Type>> {
        val result = ArrayList<Interval<Type>>()

        for ((key, value) in intervals) {
            if (key.intersects(target))
                result.addAll(value)
            else if (key.start > target.end)
                break
        }

        if (target.start < center && left != null)
            result.addAll(left!!.query(target))
        if (target.end > center && right != null)
            result.addAll(right!!.query(target))
        return result
    }

    /**
     * @param set the set to look on
     * @return the median of the set, not interpolated
     */
    private fun getMedian(set: SortedSet<Long>): Long? {
        var i = 0
        val middle = set.size / 2
        for (point in set) {
            if (i == middle)
                return point
            i++
        }
        return null
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(center).append(": ")
        for ((key, value) in intervals) {
            sb.append("[").append(key.start).append(",").append(key.end).append("]:{")
            for (interval in value) {
                sb.append("(").append(interval.start).append(",").append(interval.end).append(",").append(interval.data).append(")")
            }
            sb.append("} ")
        }
        return sb.toString()
    }
}
