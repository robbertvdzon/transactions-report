import model.Category
import model.Mt940Entry
import service.Categories
import service.Loader
import service.Parser
import service.SummaryReport
import java.io.IOException
import java.time.LocalDate

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

            Parser.parse(transactions, categories, 9)

//            SummaryReport.reportSummary(categories,0,9)
//            SummaryReport.reportSummary(categories,1)
            SummaryReport.reportTransactions2(categories,1)

    }
}

