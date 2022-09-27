package extensions

import model.Mt940Entry
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDate

fun String.toList() = this.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

fun List<Mt940Entry>.filterMonth(month: Long): List<Mt940Entry> {
    val day = LocalDate.now().minusMonths(month)
    val dateMonth = day.month
    val year = day.year
    return this.filter { it.valutaDatum!!.month==dateMonth && it.valutaDatum!!.year==year }
}

fun File.println(string: String){
    this.appendText("$string\n")

}

fun File.newFile(): File {
    this.writeText("")
    return this
}

fun Double.formatBedrag(): String{
    val df = DecimalFormat("0.00")
    val totalBedragString = String.format("%8s", df.format(this))
    return totalBedragString
}

