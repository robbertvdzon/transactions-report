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
            category = "Zakelijke kosten Karen",
            searchWords = """
                ABN AMRO BANK KOSTEN@475081803#ABN AMRO BANK
                ABN AMRO BANK VERZEKERING@475081803#ABN AMRO VERZ
                TELE2@475081803#TELE2
                OVERIG@475081803#DEBIT#!SALARIS
                OVERIG@475081803#DEBIT#!OVER
                    """.toList()
        ),
        Category(
            category = "Inkomsten Karen",
            searchWords = """
                INKOMSTEN@475081803#CREDIT#!SALARIS
                    """.toList()
        ),
        Category(
            category = "Interne overboeking",
            searchWords = """
                    SALARIS KAREN@SALARIS KAREN
                    SALARIS ROBBERT@SALARIS ROBBERT
                    OVERBOEKING@OMSCHRIJVING: OVER#RC VAN DER ZON
                    OVERBOEKING@NL50ABNA0113259964BIC
                    OVERBOEKING@RC VAN DER ZON CJ#SEPA PERIODIEKE OVERB.#!REAAL
                    """.toList()
        ),
        Category(
            category = "Investeringen",
            searchWords = """
                    Brand New Day@BND#!PENSIOENREKENING
                    Bitcoin@BITVAVO
                    """.toList()
        ),
        Category(
            category = "Pensioen Robbert",
            searchWords = """
                    Brand New Day@BND#PENSIOENREKENING
                        """.toList()
        ),
        Category(
            category = "Pensioen Karen",
            searchWords = """
                    Reaal@Reaal#SEPA PERIODIEKE OVERB.
                    BND@NL34ABNA0548027153BIC
                        """.toList()
        ),
        Category(
            category = "Familie hypotheek",
            searchWords = """
                    VDZON@KERSTKADO#A.G. VAN DER ZON
                    VDZON@HYPOTHEEK
                    VLEUGEL@F.C. VLEUGEL#!ZONNEPANEE
                    """.toList()
        ),
        Category(
            category = "Ontvangen giften",
            searchWords = """
                    VLEUGEL@F.C. VLEUGEL#ZONNEPANEE
                    """.toList()
        ),
        Category(
            category = "Zakelijke kosten Robbert",
            searchWords = """
                ABN AMRO BANK KOSTEN@532750330#ABN AMRO BANK
                VODAFONE EN ZIGGO@532750330#VODAFONE
                BUUR & BRAKENHOFF@BUUR & BRAKENHOFF
                MOVIR@MOVIR
                NLJUG@NLJUG
                VOOGD & VOOGD AANSPRAKELIJKHEIDSVERZ@532750330#VOOGD
                    """.toList()
        ),

        Category(
            category = "Verzekering",
            searchWords = """
                VOOGD & VOOGD VOLKSWAGEN VERZ@541739336#VOOGD#VZ-616-T
                VOOGD & VOOGD HUYANDAI VERZ@541739336#VOOGD#81-NDX-8
                VOOGD & VOOGD REISVERZEKERING@541739336#VOOGD#REIS POLIS 
                VOOGD & VOOGD VOORDEELPAKKET@541739336#VOOGD#VOORDEELPAKKET
                NATIONALE-NEDERLANDEN@NATIONALE-NEDERLANDEN
                BLG WONEN 0852@BLG WONEN#0852
                BLG WONEN 0853 NL25@BLG WONEN#0853#NL25
                BLG WONEN 0853 NL35@BLG WONEN#0853#NL35
                BLG WONEN@BLG WONEN
                J C DE LANGE, UITVAART@TVAARTVERZ
                DELA NATURA@DELA NATURA
                REAAL@REAAL
                        """.toList()
        ),
        Category(
            category = "Belasting",
            searchWords = """
                        VZ-616-T@BELASTINGDIENST#VZ-616-T
                        81-NDX-8@BELASTINGDIENST#81-NDX-8
                        HOOGHEEMRAADSCHAP@HOOGHEEMRAADSCHAP
                        GEMEENTELIJKE HEFFINGEN@GEMEENTELIJKE HEFFINGEN
                        ZVW 2022
                        IB/PVV
                        BELASTINGDIENST
                        """.toList()
        ),
        Category(
            category = "Loonbelasting",
            searchWords = """
                        ZVW 2022@ZVW 2022
                        ZVW 2021@ZVW 2021
                        IB/PVV@IB/PVV
                        BELASTINGDIENST OVERIG@BELASTINGDIENST#!SEPA OVERBOEKING
                        """.toList()
        ),//
        Category(
            category = "BTW",
            searchWords = """
                        BELASTINGDIENST OVERIG@BELASTINGDIENST#SEPA OVERBOEKING
                        """.toList()
        ),//
        Category(
            category = "Inkomsten Robbert",
            searchWords = """
                INKOMSTEN@532750330#CREDIT#!SALARIS#!A.G. VAN DER ZON
                INKOMSTEN@HARVEY NASH
                    """.toList()
        ),

        Category(
            category = "creditcard",
            searchWords = """
                        INT CARD SERVICES@INT CARD SERVICES
                        """.toList()
        ),
        Category(
            category = "benzine",
            searchWords = """
                        TINQ@TINQ
                        SHELL@SHELL
                        SERV.STAT. DE BAANDERT@SERV.STAT. DE BAANDERT
                        """.toList()
        ),
        Category(
            category = "Goede doelen",
            searchWords = """
                        OXFAM NOVIB@OXFAM NOVIB
                        AIDSFONDS@AIDSFONDS
                        KINDERDORPEN@KINDERDORPEN
                        UNICEF@UNICEF
                        ARTSEN ZONDER GRENZEN@ARTSEN ZONDER GRENZEN
                        KANKERBESTRIJDING@KANKERBESTRIJDING
                        DIABETES FDS@DIABETES FDS
                        CORDAID@STICHTING CORDAID MACHTIGING
                        """.toList()
        ),
        Category(
            category = "Boodschappen",
            searchWords = """
                        JUMBO@JUMBO
                        ALBERT HEIJN@ALBERT HEIJN
                        DIRKVDBROEK@DIRKVDBROEK
                        DEKAMARKT@DEKAMARKT
                        ARTEKA@ARTEKA
                        GR&FR PIET BEENTJES@GR&FR PIET BEENTJES
                        HAARSHOP@HAARSHOP
                        AHOLD@AHOLD
                        GROENTEMARKT@GROENTEMARKT
                        BAKKER MEIJER@BAKKER MEIJER
                        SIEBE REITSMA BROOD@SIEBE REITSMA BROOD
                        PETS PLACE@PETS PLACE
                        CCV KANEDA SUSHI & BOW@CCV KANEDA SUSHI & BOW
                        BACKWERK@BACKWERK
                        GROENTE EN FRUITBOKSER@GROENTE EN FRUITBOKSER
                        VISHANDEL@VISHANDEL
                        CCV MINU MARKT@CCV MINU MARKT
                        KRUIDVAT@KRUIDVAT
                        TREKPLEISTER@TREKPLEISTER
                        ISTANBUL MARKET@ISTANBUL MARKET
                        SLAGERIJ JOHN DE ROODE@SLAGERIJ JOHN DE ROODE
                        VOMAR@VOMAR
                        LUNCHROOM-FRIETHUYS@LUNCHROOM-FRIETHUYS
                        MAKRO@MAKRO
                        SLAGERIJ DE ROODE@SLAGERIJ DE ROODE
                        DE GROENTE & FRUIT@DE GROENTE & FRUIT 
                        ACTION@ACTION 1321
                        WIM KOELMAN@WIM KOELMAN
                        """.toList()
        ),



        Category(
            category = "vaste lasten",
            searchWords = """
                        OBVION HYPOTHEEK@OBVION
                        DE NEDERLANDSE KLUIS@DE NEDERLANDSE KLUIS
                        NAT POSTCODE LOTERIJ@NAT POSTCODE LOTERIJ
                        MYLIFE KAREN@HEALTH CLUB HEEMSKERK#MYLIFE LIFE
                        MYLIFE ROBBERT@HEALTH CLUB HEEMSKERK#MYLIFE FIT
                        WIM BRANDJES@WIM BRANDJES
                        THOMAS BARBERSHOP@THOMAS BARBERSHOP
                        NRC IN DE OCHTENDDIGITAAL@NRC IN DE OCHTENDDIGITAAL
                        NLE@NLE
                        OSTEOPATHIE@OSTEOPATHIE
                        VAN SOMEREN DIERENW@VAN SOMEREN DIERENW
                        CHIN EN TSAI@CHIN EN TSAI
                        VIDEOLAND@VIDEOLAND
                        VITAILY@VITAILY
                        NRC MEDIA@NRC MEDIA
                        HARMONY SERVICE CENTER(ICare)@HARMONY SERVICE CENTER
                        WATERLEIDINGBEDRIJF@WATERLEIDINGBEDRIJF
                        BEVERWIJKSE RENNERSCLUB@BEVERWIJKSE RENNERSCLUB
                        NOORDHOLLANDS DAGB@NOORDHOLLANDS DAGB
                        VOLKSKRANT@VOLKSKRANT
                        STICHTING HISTORISCHE KRIN@STICHTING HISTORISCHE KRIN
                        ATLETIEKVERENIGING@ATLETIEKVERENIGING
                        NATUURMONUMENTEN@NATUURMONUMENTEN
                        OMROEPVERENIGING VPRO@OMROEPVERENIGING VPRO
                        BUDGET ENERGIE@BUDGET ENERGIE
                        ZIGGO@ZIGGO
                        MUSEUMKAART@STICHTING MUSEUMKAARTOMSCHRIJVING
                        HARMONY INSURANCES@HARMONY INSURANCES
                        ABN AMRO@ABN AMRO BANK N.V.
                        AMAZON@AMAZON EU
                        MAERE APOTHEEK@MAERE APOTHEEK
                        CHIROPRACTIE@CHIROPRACTIE
                        """.toList()
        ),
        remaining
    )

}