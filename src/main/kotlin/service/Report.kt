package service

import extensions.filterMonth
import extensions.newFile
import extensions.println
import model.Category
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
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
            val df = DecimalFormat("0.00")
            val bedrag = df.format(totalBedrag).fixedSize(8)

            file.println("${name.fixedSize(13)} $bedrag euro")
        }
    }

    fun reportSummary(categories: List<Category>, monthStart: Long, monthEnd: Long) {
        val file = getSummaryFile()

        // header: alle maanden
        var header = "".fixedSize(14)
        for (month in monthEnd downTo  monthStart){
            val monthName = LocalDate.now().minusMonths(month).month.name
            header = "$header ${monthName.fixedSize(10)}"
        }
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

            println("${name.fixedSize(13)} $bedragen")
            file.println("${name.fixedSize(13)} $bedragen")
        }

        // line
        var line = "-----------------------------".fixedSize(14+10)
        for (month in monthEnd downTo  monthStart){
            line += "-----------------------------".fixedSize(10)
        }
        println(line)
        file.println(line)

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
        file.println(summaryBedragen)

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
            val totalBedrag = transactionsThisMonth.map { it.betrag!!.toDouble() }.sum()
            file.println("\n${name.fixedSize(10)} $transactionCount transacties $totalBedrag euro")
            transactionsThisMonth.forEach {
                val df = DecimalFormat("0.00")
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

