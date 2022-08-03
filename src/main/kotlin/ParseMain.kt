import model.Category
import model.Mt940Entry
import service.Categories
import service.Loader
import service.Parser
import service.SummaryReport
import java.io.IOException
import java.time.LocalDate

object ParseMain {

    // Separate DEBET and CREDET? Or not? (tikkie is special)
    // voor /TRTP/SEPA regels: extract omschrijving
    // per catagorie:
    //     totaal bedrag
    //     ook totaal bedrag per woord (alleen als details toggle aan staat)
    // per categorie: laten zien hoeveel uitgegeven voor elke maand (zodat je kan zie of dat hetzelfde blijft)

    @JvmStatic
    fun main(args: Array<String>) {

            val transactions = Loader.loadTransactions()

            val categories = Categories.categories

            Parser.parse(transactions, categories, 3)

            SummaryReport.reportSummary(categories,1)
            SummaryReport.reportTransactions(categories,1)

    }
}

