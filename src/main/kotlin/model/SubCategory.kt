package model

import extensions.filterMonth
import java.time.Month

data class SubCategory(
    val subCategory: String,
    val transactions : MutableList<Mt940Entry> = mutableListOf(),
) {
    fun totalBedrag(): Int {
        val toInt = transactions.map { it.betrag!!.toDouble() }.sum().toInt()
        println(subCategory+":"+toInt)
        return toInt
    }

    fun totalBedragOfMonth(month: Long): Int {
        val toInt = transactions.filterMonth(month).map { it.betrag!!.toDouble() }.sum().toInt()
//        println(subCategory+":::"+toInt)
        return toInt
    }

}