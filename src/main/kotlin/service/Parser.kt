package service

import model.Category
import model.Mt940Entry
import java.time.LocalDate

object Parser {

    fun parse(transactions: List<Mt940Entry>, categories: List<Category>, months: Long) {
        val lastMonths: List<Mt940Entry> = transactions.filter { it.valutaDatum!! > LocalDate.now().minusMonths(months) }
        val mutableTransactions = lastMonths.toMutableList()


        val inkomstenKarenCategorie = categories.find { it.category=="Inkomsten Karen" }?:throw RuntimeException("Inkomsten Karen categorie not gevonden")
        val zakelijkeKostenKarenCategorie = categories.find { it.category=="Zakelijke kosten Karen" }?:throw RuntimeException("Zakelijke kosten Karen categorie not gevonden")
        val inkomstenRobbertCategorie = categories.find { it.category=="Inkomsten Robbert" }?:throw RuntimeException("Inkomsten Robbert categorie not gevonden")
        val zakelijkeKostenRobbertCategorie = categories.find { it.category=="Zakelijke kosten Robbert" }?:throw RuntimeException("Zakelijke kosten Robbert categorie not gevonden")

        // check inkomsten karen
        mutableTransactions.toMutableList().forEach {
            if (it.betrag!!.toFloat()>0 && it.rekeningaanduiding=="475081803"){
                inkomstenKarenCategorie.transactions.add(it)
                mutableTransactions.remove(it)
            }
        }

        // check zakelijke kosten karen
        mutableTransactions.toMutableList().forEach {
            if (it.betrag!!.toFloat()<0 && it.rekeningaanduiding=="475081803" && !it.description!!.lowercase().contains("salaris")){
                zakelijkeKostenKarenCategorie.transactions.add(it)
                mutableTransactions.remove(it)
            }
        }

        // check inkomsten karen
        mutableTransactions.toMutableList().forEach {
            if (it.betrag!!.toFloat()>0 && it.rekeningaanduiding=="532750330"){
                inkomstenRobbertCategorie.transactions.add(it)
                mutableTransactions.remove(it)
            }
        }

        // check zakelijke kosten karen
        mutableTransactions.toMutableList().forEach {
            if (it.betrag!!.toFloat()<0 && it.rekeningaanduiding=="532750330" && !it.description!!.lowercase().contains("salaris")){
                zakelijkeKostenRobbertCategorie.transactions.add(it)
                mutableTransactions.remove(it)
            }
        }

        // check interne overboeking




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
            val transactions = mutableTransactions.filter { it.description?.lowercase()?.contains(subCategory.subCategory.lowercase()) ?: false }
            subCategory.transactions.addAll(transactions)
            category.transactions.addAll(transactions)
            mutableTransactions.removeAll(transactions)
        }
    }


}