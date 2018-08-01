package com.bajdcc.util.intervalTree

import java.io.Serializable

/**
 * The Interval class maintains an interval with some associated data
 *
 * @param <Type> The type of data being stored
 * @author Kevin Dolan
</Type> */
class Interval<Type>(var start: Long, var end: Long, var data: Type?) : Comparable<Interval<Type>>, Serializable {

    /**
     * @param time data
     * @return true if this interval contains time (invlusive)
     */
    operator fun contains(time: Long): Boolean {
        return time in (start + 1)..(end - 1)
    }

    /**
     * @param other other interval tree
     * @return return true if this interval intersects other
     */
    fun intersects(other: Interval<*>): Boolean {
        return other.end > start && other.start < end
    }

    /**
     * Return -1 if this interval's start time is less than the other, 1 if greater
     * In the event of a tie, -1 if this interval's end time is less than the other, 1 if greater, 0 if same
     *
     * @param other other interval tree
     * @return 1 or -1
     */
    override fun compareTo(other: Interval<Type>): Int {
        return if (start < other.start)
            -1
        else if (start > other.start)
            1
        else
            java.lang.Long.compare(end, other.end)
    }
}
