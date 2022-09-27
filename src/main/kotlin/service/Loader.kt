package service

import model.Mt940Entry
import model.Mt940Parser
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.time.LocalDate

object Loader {
    fun loadTransactions(): List<Mt940Entry> {
//        val lineNumberReader = LineNumberReader(FileReader("/tmp/MT940220802191727.STA"))
//        val lineNumberReader = LineNumberReader(FileReader("/tmp/karen.sta"))
//        val lineNumberReader = LineNumberReader(FileReader("/tmp/robbert.sta"))
        val lineNumberReader = LineNumberReader(FileReader("/tmp/MT940220829172602.STA"))
        val parse = Mt940Parser.parse(lineNumberReader)
        val transactions = parse.records.flatMap { it?.entries ?: emptyList() }
        return transactions
    }

}