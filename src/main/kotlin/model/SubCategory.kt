package model

import extensions.filterMonth

data class SubCategory(
    val subCategory: String,
    val transactions : MutableList<Mt940Entry> = mutableListOf(),
) {
    fun getDescription() : String{
        val split = subCategory.split("@")
        return if (split.size==2) split[0] else "Invalid subcategory $subCategory"
    }

    private fun getSearchWords() : List<String>{
        val split = subCategory.split("@")
        return if (split.size==2) split[1].split("#") else emptyList()
    }

    fun getSearchWordsRequired() : List<String>{
        return getSearchWords().filter { !it.startsWith("!") }
    }

    fun getSearchWordsForbidden() : List<String>{
        return getSearchWords().filter { it.startsWith("!") }.map { it.replace("!","") }
    }

    fun totalBedrag(): Int {
        val toInt = transactions.map { it.betrag!!.toDouble() }.sum().toInt()
        println(subCategory+":"+toInt)
        return toInt
    }

    fun totalBedragOfMonth(month: Long): Int {
        val toInt = transactions.filterMonth(month).map { it.betrag!!.toDouble() }.sum().toInt()
//        println(subCategory+":::"+toInt)
        return toInt
    }

}