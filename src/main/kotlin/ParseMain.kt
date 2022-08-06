import service.Categories
import service.Loader
import service.Parser
import service.SummaryReport

object ParseMain {
/*
Bestanden:
- summary (alle maanden)
-  per maand
    - korte summary
    - summary per subcategory
    - import lijst

- vergelijk met oude begroting
- check extra kosten
 */

    @JvmStatic
    fun main(args: Array<String>) {

        val transactions = Loader.loadTransactions()

        val categories = Categories.categories

        val totalMonthsToProcess = 17L
        val monthsToProcess = (0L..totalMonthsToProcess).toList()
        SummaryReport.createEmptyFiles(monthsToProcess)

        // parse transactions
        Parser.parse(transactions, categories, totalMonthsToProcess)

        // create reports
        SummaryReport.reportSummary(categories, 0, totalMonthsToProcess)
        monthsToProcess.forEach {
            SummaryReport.reportSummaryForMonth(categories, it)
            SummaryReport.reportTransactions2(categories,it)
            SummaryReport.reportTransactions(categories,it)
        }

    }
}

