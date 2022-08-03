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
                val bedrag = String.format("%6s", df.format(it.betrag))
                val description = it
                    .description!!
                    .replace("\\s+".toRegex(), " ")
                    .replace("BETAALPAS","")
                println("  ${it.valutaDatum}  ${bedrag}  ${description}")
            }

        }
    }




}

