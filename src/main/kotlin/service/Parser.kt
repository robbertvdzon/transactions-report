package service

import model.Category
import model.Mt940Entry
import java.time.LocalDate

object Parser {

    fun parse(transactions: List<Mt940Entry>, categories: List<Category>, months: Long) {
        val lastMonths: List<Mt940Entry> = transactions.filter { it.valutaDatum!! > LocalDate.now().minusMonths(months) }
        val mutableTransactions = lastMonths.toMutableList()
        println("size before:"+mutableTransactions.size)
        Categories.categories.forEach {
                category -> processCategory(mutableTransactions, category)
        }
        Categories.remaining.transactions.addAll(mutableTransactions)
        println("transactions ot categorized:"+mutableTransactions.size)

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