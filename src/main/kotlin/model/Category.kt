package model

data class Category(
    val category: String,
    val transactions : MutableList<Mt940Entry> = mutableListOf(),
    val searchWords: List<String>
) {
    fun totalBedrag() = transactions.map { it.betrag!!.toDouble() }.sum().toInt()

}