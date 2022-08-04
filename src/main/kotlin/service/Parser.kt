package service

import model.Category
import model.Mt940Entry
import java.time.LocalDate

object Parser {

    fun parse(transactions: List<Mt940Entry>, categories: List<Category>, months: Long) {
        val lastMonths: List<Mt940Entry> = transactions.filter { it.valutaDatum!! > LocalDate.now().minusMonths(months) }
        val mutableTransactions = lastMonths.toMutableList()
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