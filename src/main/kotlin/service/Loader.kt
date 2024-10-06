package service

import model.Mt940Entry
import model.Mt940Parser
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader
import java.time.LocalDate

object Loader {
    fun loadTransactions(): List<Mt940Entry> {
//        val lineNumberReader = LineNumberReader(FileReader("/Users/robbertvanderzon/git/transactions-report/downloads/MT940241006110612.STA"))
//        val lineNumberReader = LineNumberReader(FileReader("/Users/robbertvanderzon/git/transactions-report/downloads/2024.STA"))
        val lineNumberReader = LineNumberReader(FileReader("/Users/robbertvanderzon/git/transactions-report/downloads/heeljaar.STA"))
//        val lineNumberReader = LineNumberReader(FileReader("/Users/robbertvanderzon/git/transactions-report/downloads/eind-vorigjaar.STA"))
        val parse = Mt940Parser.parse(lineNumberReader)
        val transactions = parse.records.flatMap { it?.entries ?: emptyList() }
        return transactions
    }

}