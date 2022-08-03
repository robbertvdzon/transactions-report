package service

import extensions.filterMonth
import model.Category
import model.Mt940Entry
import java.text.DecimalFormat
import java.time.LocalDate

object SummaryReport {

    fun reportSummary(categories: List<Category>, month: Long) {
        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month)
            val transactionCount = transactionsThisMonth.size
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            println("CATEGORY: $name $transactionCount transacties $totalBedrag euro")
        }
    }

    fun reportTransactions(categories: List<Category>, month: Long) {
        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month)
            val transactionCount = transactionsThisMonth.size
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            println("\n\nCATEGORY: $name $transactionCount transacties $totalBedrag euro")
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

        val maxChars = 100
        if (result.length>=maxChars) result = result.substring(0,maxChars)

        return result.padEnd(maxChars)  + " ==> " + this
    }




}

