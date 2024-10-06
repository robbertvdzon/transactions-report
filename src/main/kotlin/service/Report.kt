package service

import extensions.filterMonth
import extensions.formatBedrag
import extensions.newFile
import extensions.println
import model.Category
import model.Mt940Entry
import service.SummaryReport.parseDescription
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDate

object SummaryReport {

    fun reportSummaryForMonth(categories: List<Category>, month: Long) {
        val file = getMonthFile(month)
        file.println("""
            ************************************************************************************
            ** Category                                                                       **
            ************************************************************************************
            
        """.trimIndent())

        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month)
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            file.println("${name.fixedSize(13)} ${totalBedrag.formatBedrag()} euro")
        }
    }

    fun reportSummary(categories: List<Category>, monthStart: Long, monthEnd: Long) {
        val file = getSummaryFile()

        // cache the average bedragen
        val averageCache: MutableMap<Category, Double> = mutableMapOf()
        val aantalMaanden = monthEnd - monthStart + 1
        categories.forEach { category ->
            val transactionsThisCategory: MutableList<Mt940Entry> = mutableListOf()
            for (month in monthEnd downTo  monthStart){
                transactionsThisCategory.addAll(category.transactions.filterMonth(month))
            }
            val totalBedrag = transactionsThisCategory.map { it.betrag!!.toDouble() }.sum()
            val gemiddeldBedrag = totalBedrag/aantalMaanden
            averageCache.set(category, gemiddeldBedrag)
        }



        // header: alle maanden
        var header = "".fixedSize(31)
        for (month in monthEnd downTo  monthStart){
            val monthName = LocalDate.now().minusMonths(month).month.name
            header = "$header ${monthName.fixedSize(10)}"
        }
        header = "$header GEMIDDELD"
        println(header)
        file.println(header)

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
            val df = DecimalFormat("0.00")
            val gemiddeld = averageCache.get(category)?:"?"
            val gemiddeldStr = df.format(gemiddeld).fixedSize(10)
            bedragen = "$bedragen $gemiddeldStr"

            println("${name.fixedSize(30)} $bedragen")
            file.println("${name.fixedSize(30)} $bedragen")
        }

        // line
        var line = "---------------------------------------------------------------------------".fixedSize(31)
        for (month in (monthEnd+1) downTo  monthStart){
            line += "------------------------------".fixedSize(11)
        }
        println(line)
        file.println(line)

        // summary
        var summaryBedragen = "Totaal".fixedSize(31)
        for (month in monthEnd downTo  monthStart){
            var summary: Double = 0.0
            categories.forEach {category ->
                val transactionsThisMonth = category.transactions.filterMonth(month)
                val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
                summary += totalBedrag
            }
            summaryBedragen = "$summaryBedragen ${summary.toInt().toString().fixedSize(10)}"

        }
        val gemiddeldSum = averageCache.values.map { it.toFloat() }.sum()
        summaryBedragen = "$summaryBedragen ${gemiddeldSum.toInt().toString().fixedSize(10)}"

        println(summaryBedragen)
        file.println(summaryBedragen)

        // summary (zonder inkomsten)
        var summaryBedragenZonderInkomsten = "Totaal zonder inkomsten".fixedSize(31)
        for (month in monthEnd downTo  monthStart){
            var summaryZonderInkomsten: Double = 0.0
            categories.forEach {category ->
                if (category.category!="Inkomsten Karen" && category.category!="Inkomsten Robbert" && category.category!="Interne overboeking") {
                    val transactionsThisMonth = category.transactions.filterMonth(month)
                    val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
                    summaryZonderInkomsten += totalBedrag
                }
            }
            summaryBedragenZonderInkomsten = "$summaryBedragenZonderInkomsten ${summaryZonderInkomsten.toInt().toString().fixedSize(10)}"

        }
        val gemiddeldSumZonderInkomsten = averageCache.filter { it.key.category!="Inkomsten Karen" && it.key.category!="Inkomsten Robbert" && it.key.category!="Interne overboeking" }.values.map { it.toFloat() }.sum()
        summaryBedragenZonderInkomsten = "$summaryBedragenZonderInkomsten ${gemiddeldSumZonderInkomsten.toInt().toString().fixedSize(10)}"
        println(summaryBedragenZonderInkomsten)
        file.println(summaryBedragenZonderInkomsten)


    }




    fun reportTransactions(categories: List<Category>, month: Long) {
        val file = getMonthFile(month)
        file.println("""
                        
                        
            ************************************************************************************
            ** Alle transacties deze maand                                                    **
            ************************************************************************************
        """.trimIndent())
        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month).sortedBy { it.betrag }
            val transactionCount = transactionsThisMonth.size
            val df = DecimalFormat("0.00")
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            file.println("\n${name.fixedSize(30)} $transactionCount transacties ${df.format(totalBedrag)} euro")
            transactionsThisMonth.forEach {
                val bedrag = String.format("%8s", df.format(it.betrag))
                val description = it.description?.parseDescription()?:"(GEEN OMSCHRIJVING!)"
                file.println("  ${it.valutaDatum}  ${bedrag}  ${description}")
            }

        }
    }

    fun reportTransactions2(categories: List<Category>, month: Long) {
        val file = getMonthFile(month)
        file.println("""
                        
                        
            ************************************************************************************
            ** Uitgewerkt per subcategory                                                     **
            ************************************************************************************
        """.trimIndent())

        categories.forEach {category ->
            val name = category.category
            val transactionsThisMonth = category.transactions.filterMonth(month)
            val transactionCount = transactionsThisMonth.size
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            val df = DecimalFormat("0.00")
            val totalBedragString = String.format("%8s", df.format(totalBedrag))

            file.println("\n${name.fixedSize(15)} $transactionCount transacties $totalBedragString euro")
            val subCategories = category.subCategories.sortedBy { it.totalBedragOfMonth(month) }
            subCategories.forEach { subCategory ->
                val name = subCategory.subCategory
                val transactionsThisMonth = subCategory.transactions.filterMonth(month)
                val transactionCount = transactionsThisMonth.size
                val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
                val df = DecimalFormat("0.00")
                val totalBedragString = String.format("%8s", df.format(totalBedrag))

                val countString = String.format("%2s", transactionCount)

                if (transactionCount>0) {
                    file.println("- ${name.fixedSize(20)} $countString transacties ${totalBedragString} euro")
                }
            }

        }
    }


    fun reportSubcategorieAllPeriod(categories: List<Category>, totalMonths: Long) {
        categories.forEach { category ->
            println("\n\nSUB CATEGORIES FROM ${category.category}")

            category.transactions.forEach {
                val bedrag = it.betrag?.toDouble()?.formatBedrag()
                val description = it.description?.parseDescription()?:"(GEEN OMSCHRIJVING!)"
//                println("  ${it.valutaDatum}  ${bedrag}  ${description}")
//                        println( it.description)
            }

            val subCategories = category.subCategories
            subCategories.forEach { subCategory ->
                val total = subCategory.transactions.mapNotNull { it.betrag?.toDouble() }.sum()
                val perMonth = total/totalMonths
                println("  ${subCategory.getDescription().fixedSize(30)} : ${subCategory.transactions.size} transactions, total: ${total.formatBedrag()}  per maand: ${perMonth.formatBedrag()}")
                subCategory.transactions.forEach {
                    val bedrag = it.betrag?.toDouble()?.formatBedrag()
                    val description = it.description?.parseDescription()?:"(GEEN OMSCHRIJVING!)"
//                        println("     ${it.valutaDatum}  ${bedrag}  ${description}")
                    println("     ${it.valutaDatum}  ${bedrag}  ${it.getSummary()}")
                }
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
        if (result.contains("SEPA IDEAL IBAN:")) {
            val splitted = result.split(":")
            result = splitted[3]
        }

        if (result.contains("BEA, BETAALPAS")) {
            val splitted = result.split(",")
            result = splitted[1].replace("BETAALPAS","").trim()
        }
        if (result.contains("BEA NR:")) {
            val splitted = result.split("/")
            result = splitted[1]
        }
        if (result.contains("SEPA INCASSO ALGEMEEN DOORLOPEND INCASSANT")) {
            val splitted = result.split(":")
            result = splitted[2]
        }
        if (result.contains("SEPA PERIODIEKE OVERB")) {
            val splitted = result.split(":")
            result = splitted[splitted.size-1]
        }

        return result.trim().fixedSize(100)  + " ==> " + this
    }

    fun getSummaryFile(): File {
        return File("reports/summary.txt")
    }


    fun getMonthFile(month: Long): File {
        val day = LocalDate.now().minusMonths(month)

        val dateMonthNr = day.month.value
        val df = DecimalFormat("00")
        val monthNr = df.format(dateMonthNr).fixedSize(2)
        val year = day.year
        val name = "reports/$year-$monthNr.txt"
        return File(name)
    }

    fun createEmptyFiles(months: List<Long> ){
        File("reports").mkdirs()
        months.forEach {
            getMonthFile(it).newFile()
        }
        getSummaryFile().newFile()
    }



}

fun String.fixedSize(size: Int): String{
    var result = this
    if (result.length>=size) result = result.substring(0,size)

    return result.padEnd(size)

}

