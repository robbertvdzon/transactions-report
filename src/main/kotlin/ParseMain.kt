import model.Category
import model.Mt940Entry
import service.Categories
import service.Loader
import service.Parser
import service.SummaryReport
import java.io.IOException
import java.time.LocalDate

object ParseMain {

    // per catagorie:
    //     totaal bedrag
    //     ook totaal bedrag per woord (alleen als details toggle aan staat)
    // sorteer op bedrag (zowel alle losse transacties, als de gegoepeerde transacties)
    // vergelijk met oude begroting
    // check alle extra kosten

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

