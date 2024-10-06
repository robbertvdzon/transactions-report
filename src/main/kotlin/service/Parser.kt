package service

import model.Category
import model.Mt940Entry
import java.time.LocalDate

object Parser {

    fun parse(transactions: List<Mt940Entry>, categories: List<Category>, months: Long) {
        val lastMonths: List<Mt940Entry> = transactions.filter { it.valutaDatum!! > LocalDate.now().minusMonths(months) }
        val mutableTransactions = lastMonths.toMutableList()


//        val inkomstenKarenCategorie = categories.find { it.category=="Inkomsten Karen" }?:throw RuntimeException("Inkomsten Karen categorie not gevonden")
//        val zakelijkeKostenKarenCategorie = categories.find { it.category=="Zakelijke kosten Karen" }?:throw RuntimeException("Zakelijke kosten Karen categorie not gevonden")
//        val inkomstenRobbertCategorie = categories.find { it.category=="Inkomsten Robbert" }?:throw RuntimeException("Inkomsten Robbert categorie not gevonden")
//        val zakelijkeKostenRobbertCategorie = categories.find { it.category=="Zakelijke kosten Robbert" }?:throw RuntimeException("Zakelijke kosten Robbert categorie not gevonden")
//        val interneOverboekingCategorie = categories.find { it.category=="Interne overboeking" }?:throw RuntimeException("Interne overboeking categorie not gevonden")
//        val extraCategorie = categories.find { it.category=="Extra" }?:throw RuntimeException("Extra categorie not gevonden")

//
//        // check handmatige extra
//        mutableTransactions.toMutableList().forEach {
//            if (it.description!=null && it.description!!.contains("KERSTKADO")){
//                extraCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }
//        mutableTransactions.toMutableList().forEach {
//            if (it.description!=null && it.description!!.contains("OF M.P. VAN DER ZON-SPRENKELING")){
//                extraCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }
//
//        // check speciale interne overboekingen (verkoop aandelen)
//        mutableTransactions.toMutableList().forEach {
//            if (it.description!=null  && it.description!!.contains("OMSCHRIJVING: OVERBOEKING") && it.description!!.contains("RC VAN DER ZON" )){
//                interneOverboekingCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//            if (it.description!=null  && it.description!!.contains("OMSCHRIJVING: OVER") && it.description!!.contains("RC VAN DER ZON" )){
//                interneOverboekingCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//            if (it.description!=null  && it.description!!.contains("OMSCHRIJVING: OVER") && it.description!!.contains("SHIRTSKV" )){
//                interneOverboekingCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//            if (it.description!=null  && it.description!!.lowercase().contains("verkooporder") && it.description!!.lowercase().contains("brand new day" )){
//                interneOverboekingCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }
//
//
//        // check inkomsten karen
//        mutableTransactions.toMutableList().forEach {
//            if (it.betrag!!.toFloat()>0 && it.rekeningaanduiding=="475081803"){
//                inkomstenKarenCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }

        // check zakelijke kosten karen
//        mutableTransactions.toMutableList().forEach {
//            if (it.betrag!!.toFloat()<0 && it.rekeningaanduiding=="475081803" && !it.description!!.lowercase().contains("salaris")){
//                zakelijkeKostenKarenCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }

//        // check inkomsten robbert
//        mutableTransactions.toMutableList().forEach {
//            if (it.betrag!!.toFloat()>0 && it.rekeningaanduiding=="532750330"){
//                inkomstenRobbertCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }

        // check zakelijke kosten robbert
//        mutableTransactions.toMutableList().forEach {
//            if (it.betrag!!.toFloat()<0 && it.rekeningaanduiding=="532750330" && !it.description!!.lowercase().contains("salaris")){
//                zakelijkeKostenRobbertCategorie.transactions.add(it)
//                mutableTransactions.remove(it)
//            }
//        }

        println("size before:"+mutableTransactions.size)
        categories.forEach {
                category -> processCategory(mutableTransactions, category)
        }
        Categories.remaining.transactions.addAll(mutableTransactions)
        Categories.remaining.subCategories.first().transactions.addAll(mutableTransactions)
        println("transactions not categorized:"+mutableTransactions.size)

    }


    private fun processCategory(mutableTransactions: MutableList<Mt940Entry>, category: Category) {

        category.subCategories.forEach {subCategory->
            val split = subCategory.subCategory.split("@")
            if (split.size==2) {
                val searchWordsListRequired = subCategory.getSearchWordsRequired()
                val searchWordsListForbidden = subCategory.getSearchWordsForbidden()
                if (category.category=="Pensioen Karen"){
                    println("")
                }

                val transactions = mutableTransactions
                    .filter {
                        val requiredWordsSearchResult = searchWordsListRequired.map { matchWord ->
                            it.getSummary().lowercase().contains(matchWord.lowercase())
                        }
                        val forbiddenWordsSearchResult = searchWordsListForbidden.map { matchWord ->
                            it.getSummary().lowercase().contains(matchWord.lowercase())
                        }
                        val allRequiredWordsFound = requiredWordsSearchResult.all { it==true }
                        val allForbiddendWordsNotFound = forbiddenWordsSearchResult.all { it==false }
                        val found = allRequiredWordsFound && allForbiddendWordsNotFound
                        if (found && it.getSummary().contains("HARVEY NASH")) {
                            println("")
                        }
                        found
                    }
                if (category.category=="Pensioen Karen"){
                    println("transactions:"+transactions.size)
                }
                subCategory.transactions.addAll(transactions)
                category.transactions.addAll(transactions)
                mutableTransactions.removeAll(transactions)
            }
        }
    }


}