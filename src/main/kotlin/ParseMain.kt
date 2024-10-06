import service.Categories
import service.Loader
import service.Parser
import service.SummaryReport

object ParseMain {
    @JvmStatic
    fun main(args: Array<String>) {

        val transactions = Loader.loadTransactions()

        val categories = Categories.categories

        val fromMonth = 1L
        val toMonth = 12L
        val monthsToProcess = (fromMonth..toMonth).toList()
        SummaryReport.createEmptyFiles(monthsToProcess)

        // parse transactions
        Parser.parse(transactions, categories, toMonth)

        // create reports
        SummaryReport.reportSummary(categories, fromMonth, toMonth)
        monthsToProcess.forEach {
            SummaryReport.reportSummaryForMonth(categories, it)
            SummaryReport.reportTransactions2(categories,it)
            SummaryReport.reportTransactions(categories,it)
        }

        val category  =categories.first { it.category=="Remaining" }
        val aantalMaanden = toMonth - fromMonth +1
        SummaryReport.reportSubcategorieAllPeriod(categories, aantalMaanden)
        SummaryReport.reportSubcategorieAllPeriod(listOf(category), aantalMaanden)

    }
}

