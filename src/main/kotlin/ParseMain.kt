import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.time.LocalDate

object ParseMain {
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val lineNumberReader = LineNumberReader(FileReader("/tmp/MT940220802191727.STA"))
            val parse = Mt940Parser.parse(lineNumberReader)
            println(parse)
            val transactions = parse.records.flatMap { it?.entries?: emptyList() }

            val last3Months: List<Mt940Entry> = transactions.filter { it.valutaDatum!! > LocalDate.now().minusMonths(3) }
            println(last3Months.size)



            val remaining = Category(
                category = "remaining",
                searchWords =  emptyList()
            )

            val categories = listOf(
                Category(
                    category = "boodschappen",
                    searchWords =  """
                        JUMBO
                        ALBERT HEIJN
                        DIRKVDBROEK
                        DEKAMARKT
                        ARTEKA
                        GR&FR PIET BEENTJES
                        HAARSHOP
                        AHOLD
                        GROENTEMARKT
                        BAKKER MEIJER
                        SIEBE REITSMA BROOD
                        PETS PLACE
                        CCV KANEDA SUSHI & BOW
                        BACKWERK
                        GROENTE EN FRUITBOKSER
                        VISHANDEL
                        FAMILY HEEMSKERK
                        CCV MINU MARKT
                        KRUIDVAT
                        TREKPLEISTER
                        ISTANBUL MARKET
                        SLAGERIJ JOHN DE ROODE
                        VOMAR
                        """.toList()
                ),
                Category(
                    category = "verzekering",
                    searchWords =  """
                        VOOGD EN VOOGD
                        NATIONALE-NEDERLANDEN
                        BLG WONEN
                        ASSURANTIEKANTOOR J C DE LANGE
                        DELA NATURA
                        REMI
                        REAAL
                        TVAARTVERZ
                        """.toList()
                ),
                Category(
                    category = "belasting",
                    searchWords =  """
                        GEMEENTELIJKE HEFFINGEN 2022
                        BELASTINGDIENST
                        HOOGHEEMRAADSCHAP
                        """.toList()
                ),
                Category(
                    category = "goede doelen",
                    searchWords =  """
                        NATUURMONUMENTEN AMERSFOORT
                        OXFAM NOVIB
                        AIDSFONDS
                        KINDERDORPEN
                        UNICEF
                        ARTSEN ZONDER GRENZEN
                        KANKERBESTRIJDING
                        DIABETES FDS
                        """.toList()
                ),
                Category(
                    category = "vaste lasten",
                    searchWords =  """
                        OBVION
                        TINQ
                        DE NEDERLANDSE KLUIS
                        NAT POSTCODE LOTERIJ
                        BRAND NEW DAY BANK NV
                        HEALTH CLUB HEEMSKERK
                        CREDITCARD
                        SHELL
                        WIM BRANDJES
                        THOMAS BARBERSHOP
                        NRC IN DE OCHTENDDIGITAAL
                        """.toList()
                ),
                Category(
                    category = "inkomsten",
                    searchWords =  """
                        SALARIS
                    """.toList()
                ),
                Category(
                    category = "hobby",
                    searchWords =  """
                        ATLETIEKVERENIGING
                    """.toList()
                ),
                Category(
                    category = "extra",
                    searchWords =  """
                        IKEA
                        TIKKIE
                        PHILIPSEN
                        TWISKE             
                        BEKKER
                        BESTELLING
                        ROOM70
                        PALMAVERDE
                        STUDIO ARMIN
                        SEP DE HAVEN VAN TEXEL
                        KOFFIEHUIS
                        VERMAAT LEISURE
                        WE RIDE
                        BOL.COM
                        ZALANDO
                        VITAILY
                        PRAXIS
                        BLOKKER
                        ALOHA
                        RINUS DE RUYTER BLOEM
                        KNOEST ETEN &DRINKEN
                        STUMPEL
                        CCV ROYS
                        GASTERIJ KRUISBERG
                        RAP YOUNG ART FESTIVA
                        JACK S CAFE B.V
                        YUKSEL DELICATESSEN
                        CCV DE AMBACHTELIJKE W
                        BEVER
                        DECATHLON
                        IMMING BIKES
                        SUMUP
                        KRINGLOOP HEEMSKERK
                        XENOS
                        SIMONS UW TOPSLIJTER
                        TRIER
                        MANDERSCHEID
                        GEWOONINWIJKAA
                        GEWOON IN WIJK AAN ZEE
                        BITBURG
                        SCHRAARD
                        SUMUP GOUDEN NAALD
                        LOKAAL
                        VONKELS
                        RESTAURANT  T EETHUY
                        PG CENTRUM-RINGERS
                        TER STAL
                        MCDONALD
                        BEDBURG
                        GEROLSTEIN
                        HERTOGENBOS
                        KIOSK
                        Q PARK
                        LAURENTZ
                        RIJWIELH. T.BEENTJES
                        KOFFIE-DIK
                        BABBERICH
                        OBERHONNEFELD,LAND: DE
                        ELLWANGEN,LAND: DE
                        NASSEREITH,LAND: AT
                        PRAD AM STILF,LAND: IT
                        BORMIO,LAND: IT
                        ATM,LAND: IT
                        NAUDERS/RESCH,LAND: AT
                        DIETMANNSRIED,LAND: DE
                        GELDMAAT
                        STADSSCHOUWB
                        HEILIGENROTH,LAND: DE
                        HEMA
                        CAFE DE HOEK
                        ZOETERMEER
                        S-GRAVENHAGE
                        MAASTRICHT
                        HERTOGENBO
                        PARKEREN
                        SEPA OVERBOEKING
                        VRIJGEZELLENFEEST
                        JULIA S UT
                         HMSHOST STADSKAMER 
                         FALAFEL CITY
                         JEU DE BOULESBAR
                         HAMPTON BY HILTON
                         AVF - LOUNGE
                          HET DORRUP
                          BRUNA
                          FA. A. BOONTJES
                           DE ROSET
                           STRESS VRIJ LICHAAM
                           DESIGUALOMSCHRIJVING
                           SPAKENBURGSE WARME BAK
                           SITEDISH
                           CADEAUTJE
                           DENNEMAN:BRANDSTOF
                           PRIMERA
                    """.toList()

                ),
                remaining
            )

            // TODO: create report for each month (only the summary)
            // TODO: create report for each month (with all transactions of each catagory, to check)
            // TODO: Separate DEBET and CREDET? Or not? (tikkie is special)



            val mutableTransactions = last3Months.toMutableList()
            println("size before:"+mutableTransactions.size)
            categories.forEach {
                    category -> processCategory(mutableTransactions, category)
            }
            remaining.transactions.addAll(mutableTransactions)


            println("size after:"+mutableTransactions.size)


            remaining.transactions.forEach {
                println(it.description)
            }


            categories.forEach {category ->
                println("CATEGORY:"+category.category+":"+category.transactions.size)

//                if (category.category=="extra") {
//                    category.transactions.forEach { transaction ->
//                    println(transaction.description)
//                    }
//                }

            }





        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun processCategory(mutableTransactions: MutableList<Mt940Entry>, category: Category) {
        val transactions = mutableTransactions.filter { it.description?.containsAnyOfIgnoreCase(category.searchWords) ?: false }
        mutableTransactions.removeAll(transactions)
        category.transactions.addAll(transactions)
    }

    private fun check(transaction: Mt940Entry, category: List<String>) {
        if (transaction.description?.containsAnyOfIgnoreCase(category) ?: false) {
//            println("" + transaction.valutaDatum + " " + transaction.betrag + " " + transaction.mehrzweckfeld)
        }
    }

    fun String.containsAnyOfIgnoreCase(keywords: List<String>): Boolean {
        for (keyword in keywords) {
            if (this.contains(keyword, true)) return true
        }
        return false
    }
}

fun String.toList() = this.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

data class Category(
    val category: String,
    val transactions : MutableList<Mt940Entry> = mutableListOf(),
    val searchWords: List<String>
)