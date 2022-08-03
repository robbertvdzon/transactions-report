package extensions

import model.Mt940Entry
import java.time.LocalDate

fun String.toList() = this.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

fun List<Mt940Entry>.filterMonth(month: Long): List<Mt940Entry> {
    val day = LocalDate.now().minusMonths(month)
    val dateMonth = day.month
    val year = day.year
    return this.filter { it.valutaDatum!!.month==dateMonth && it.valutaDatum!!.year==year }
}