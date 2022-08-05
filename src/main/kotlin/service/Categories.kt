package service

import model.Category
import extensions.toList

object Categories {

    val remaining = Category(
        category = "Remaining",
        searchWords = listOf("(Zie transacties)")
    )

    val categories = listOf(
        Category(
            category = "Boodschappen",
            searchWords = """
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
            category = "Verzekering",
            searchWords = """
                        VOOGD EN VOOGD
                        NATIONALE-NEDERLANDEN
                        BLG WONEN
                        ASSURANTIEKANTOOR J C DE LANGE
                        DELA NATURA
                        REAAL
                        TVAARTVERZ
                        """.toList()
//                    REMI
        ),
        Category(
            category = "Belasting",
            searchWords = """
                        GEMEENTELIJKE HEFFINGEN 2022
                        BELASTINGDIENST
                        HOOGHEEMRAADSCHAP
                        """.toList()
        ),
        Category(
            category = "Goede doelen",
            searchWords = """
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
            searchWords = """
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
                        NLE
                        INCASSO ALGEMEEN DOORLOPEND
                        """.toList()
        ),
        Category(
            category = "Inkomsten Karen",
            searchWords = """
                        SALARIS KAREN
                    """.toList()
        ),
        Category(
            category = "Inkomsten Robbert",
            searchWords = """
                        SALARIS ROBBERT
                    """.toList()
        ),
        Category(
            category = "Hobby",
            searchWords = """
                        ATLETIEKVERENIGING
                    """.toList()
        ),
        Category(
            category = "Extra",
            searchWords = """
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

}