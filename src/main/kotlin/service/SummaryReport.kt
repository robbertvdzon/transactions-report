package service

import extensions.filterMonth
import model.Category
import model.Mt940Entry
import service.SummaryReport.parseDescription
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate

object SummaryReport {

    fun reportSummary(categories: List<Category>, month: Long) {
        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month)
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            val df = DecimalFormat("0.00")
            val bedrag = df.format(totalBedrag).fixedSize(8)

            println("${name.fixedSize(13)} $bedrag euro")
        }
    }

    fun reportSummary(categories: List<Category>, monthStart: Long, monthEnd: Long) {

        // header: alle maanden
        var header = "".fixedSize(14)
        for (month in monthEnd downTo  monthStart){
            val monthName = LocalDate.now().minusMonths(month).month.name
            header = "$header ${monthName.fixedSize(10)}"
        }
        println(header)

        // alle bedragen per category
        categories.forEach {category ->
            val name = category.category
            var bedragen = ""
            for (month in monthEnd downTo  monthStart){
                val transactionsThisMonth = category.transactions.filterMonth(month)
                val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
                val df = DecimalFormat("0.00")
                val bedrag = df.format(totalBedrag).fixedSize(10)
                bedragen = "$bedragen $bedrag"
            }

            println("${name.fixedSize(13)} $bedragen")
        }

        // line
        var line = "-----------------------------".fixedSize(14+10)
        for (month in monthEnd downTo  monthStart){
            line += "-----------------------------".fixedSize(10)
        }
        println(line)

        // summary
        var summaryBedragen = "Totaal".fixedSize(14)
        for (month in monthEnd downTo  monthStart){
            var summary: Double = 0.0
            categories.forEach {category ->
                val transactionsThisMonth = category.transactions.filterMonth(month)
                val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
                summary += totalBedrag
            }
            summaryBedragen = "$summaryBedragen ${summary.toInt().toString().fixedSize(10)}"

        }
        println(summaryBedragen)

        // summary (zonder inkomsten)
        var summaryBedragenZonderInkomsten = "Totaal zonder inkomsten".fixedSize(14)
        for (month in monthEnd downTo  monthStart){
            var summaryZonderInkomsten: Double = 0.0
            categories.forEach {category ->
                if (category.category!="Inkomsten") {
                    val transactionsThisMonth = category.transactions.filterMonth(month)
                    val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
                    summaryZonderInkomsten += totalBedrag
                }
            }
            summaryBedragenZonderInkomsten = "$summaryBedragenZonderInkomsten ${summaryZonderInkomsten.toInt().toString().fixedSize(10)}"

        }
        println(summaryBedragenZonderInkomsten)


    }


    fun reportTransactions(categories: List<Category>, month: Long) {
        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month)
            val transactionCount = transactionsThisMonth.size
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            println("\n\n${name.fixedSize(10)} $transactionCount transacties $totalBedrag euro")
            transactionsThisMonth.forEach {
                val df = DecimalFormat("0.00")
                val bedrag = String.format("%8s", df.format(it.betrag))
                val description = it.description!!.parseDescription()
                println("  ${it.valutaDatum}  ${bedrag}  ${description}")
            }

        }
    }

    fun String.parseDescription(): String{
        var result = this

        result = result.replace("\\s+".toRegex(), " ")

        if (result.contains("/TRTP/SEPA")) {
            val splitted = result.split("/")
            result = splitted[6] + " : " + splitted[10]
        }

        if (result.contains("/TRTP/IDEAL")) {
            val splitted = result.split("/")
            result = splitted[8] + " : " + splitted[10]
        }

        if (result.contains("BEA, BETAALPAS")) {
            val splitted = result.split(",")
            result = splitted[1].replace("BETAALPAS","").trim()
        }

        return result.fixedSize(100)  + " ==> " + this
    }




}

fun String.fixedSize(size: Int): String{
    var result = this
    if (result.length>=size) result = result.substring(0,size)

    return result.padEnd(size)

}

