package model

import java.io.IOException
import java.io.LineNumberReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * MT940 Parser.
 *
 * @author Arnout Engelen
 * @author Miroslav Holubec
 * @author Craig Cavanaugh
 */
object Mt940Parser {
    private const val PREFIX_MERHZWECKFELD = ":86:"
    private const val PREFIX_KONTOBEZEICHNUNG = ":25:"
    private const val PREFIX_ENTRY_START = ":61:"

    /**
     * Parse the Mt940-file. Mt940 records are delimited by '-'.
     *
     * @param reader reader
     * @return model.Mt940File instance
     * @throws IOException    An IO exception occurred
     * @throws DateTimeParseException parse error occurred reading text
     */
    @Throws(IOException::class, DateTimeParseException::class)
    fun parse(reader: LineNumberReader): Mt940File {
        val mt940File = Mt940File()
        var recordLines: MutableList<String> = ArrayList()
        var currentLine = reader.readLine()
        while (currentLine != null) {
            if (currentLine.startsWith("-")) {
                // Parse this record and add it to the file
                mt940File.records.add(parseRecord(recordLines))

                // Clear recordLines to start on the next record
                recordLines = ArrayList()
            } else {
                recordLines.add(currentLine)
            }
            currentLine = reader.readLine()
        }

        // A file might not end with a trailing '-' (e.g. from Rabobank):
        mt940File.records.add(parseRecord(recordLines))
        return mt940File
    }

    /**
     * An mt940-record first has a couple of 'header' lines that do not
     * start with a ':'.
     *
     *
     * After that, a line that doesn't start with a ':' is assumed to
     * belong to the previous 'real' line.
     *
     * @param recordLines list of records
     * @return List of strings that have been correctly merged
     */
    private fun mergeLines(recordLines: List<String>): List<String> {
        val retVal: MutableList<String> = ArrayList()
        var currentString = StringBuilder()
        var inMessage = false
        for (string in recordLines) {
            // Are we in the message proper, after the
            // header?
            if (inMessage) {
                if (string.startsWith(":")) {
                    retVal.add(currentString.toString())
                    currentString = StringBuilder()
                }
                currentString.append(string)
            } else {
                if (string.startsWith(":")) {
                    // then we're past the header
                    inMessage = true
                    currentString = StringBuilder(string)
                } else {
                    // add a line of the header
                    retVal.add(string)
                }
            }
        }
        return retVal
    }

    /**
     * An mt940-record consists of some general lines and a couple
     * of entries consisting of a :61: and a :86:-section.
     *
     * @param recordLines the List of MT940 records to parse
     * @return and generate Mt940 Record
     * @throws DateTimeParseException parse error occurred reading text
     */
    @Throws(DateTimeParseException::class)
    private fun parseRecord(recordLines: List<String>): Mt940Record {
        val mt940Record = Mt940Record()

        // Merge 'lines' that span multiple actual lines.
        val mergedLines = mergeLines(recordLines)
        var currentEntry: Mt940Entry? = null
        var currentAccount: String? = null
        for (line in mergedLines) {
            var line2 = line
            if (line2.startsWith(PREFIX_KONTOBEZEICHNUNG)) {
                currentAccount = line2.substring(PREFIX_KONTOBEZEICHNUNG.length)
            }
            if (line2.startsWith(PREFIX_ENTRY_START)) {
                currentEntry = nextEntry(mt940Record.entries, currentEntry, currentAccount)
                line2 = line2.substring(PREFIX_ENTRY_START.length)
                line2 = parseDatumJJMMTT(currentEntry, line2)
                // for now don't handle the buchungsdatum. It is optional.
                if (startsWithBuchungsDatum(line2)) {
                    line2 = line2.substring(4)
                }
                // for now only support C and D, not RC and RD
                line2 = parseSollHabenKennung(currentEntry, line2)
                line2 = parseBetrag(currentEntry, line2)
            }
            if (line2.startsWith(PREFIX_MERHZWECKFELD) && currentEntry != null) {
                currentEntry.addToMehrzweckfeld(line2.substring(PREFIX_MERHZWECKFELD.length))
            }
        }

        // add the last one:
        nextEntry(mt940Record.entries, currentEntry, currentAccount)
        return mt940Record
    }

    /**
     * Adds the current entry to the result as a side-effect, if available.
     *
     * @param entries       entry list
     * @param previousEntry entry to add if not null;
     * @return new working `model.Mt940Entry`
     */
    private fun nextEntry(
        entries: MutableList<Mt940Entry>,
        previousEntry: Mt940Entry?,
        currentAccount: String?
    ): Mt940Entry {
        if (previousEntry != null) {
            entries.add(previousEntry)
        }
        val entry = Mt940Entry()
        entry.kontobezeichnung = currentAccount
        return entry
    }

    /**
     * BuchungsDatum is a 4-character optional field - but how can we check whether it was included.
     *
     *
     * The field is directly followed by the mandatory 'soll/haben-kennung' character, so
     * we assume that when the string starts with a digit that's probably the buchungsdatum
     *
     * @param line line to check for BuchungsDatum
     * @return true if found
     */
    private fun startsWithBuchungsDatum(line: String?): Boolean {
        return line != null && line.matches("^\\d.*".toRegex())
    }

    /**
     * Parse the value, put it into the entry.
     *
     * @param currentEntry working `model.Mt940Entry`
     * @param line line to parse decimal value from
     * @return the rest of the string to be parsed
     */
    private fun parseBetrag(currentEntry: Mt940Entry?, line: String): String {
        var endIndex = line.indexOf('N')
        if (endIndex < 0) {
            endIndex = line.indexOf('F')
        }
        var decimal = line.substring(0, endIndex)
        decimal = decimal.replace(",".toRegex(), ".")

        // According to the MT940 Standard the amount (field :61:) could start with the last character of the currency
        // e.g. R for EUR
        // See: https://www.kontopruef.de/mt940s.shtml
        // This code removes any character which is not a decimal or point.
        decimal = decimal.replace("[^\\d.]".toRegex(), "")
        currentEntry!!.betrag = BigDecimal(decimal)
        return line.substring(endIndex)
    }

    /**
     * Parse the debit/credit value, put it into the entry.
     *
     * @param currentEntry working `model.Mt940Entry`
     * @param string credit / debit line to parse
     * @return the rest of the string to be parsed
     */
    private fun parseSollHabenKennung(currentEntry: Mt940Entry?, string: String): String {
        var s = string
        if (string.startsWith("D")) {
            currentEntry!!.sollHabenKennung = Mt940Entry.SollHabenKennung.DEBIT
            s = string.substring(1)
        } else if (string.startsWith("C")) {
            currentEntry!!.sollHabenKennung = Mt940Entry.SollHabenKennung.CREDIT
            s = string.substring(1)
        } else {
            throw UnsupportedOperationException("soll-haben-kennung $s not yet supported")
        }
        return s
    }

    /**
     * Parse the formatted date, put it into the entry.
     *
     * @param currentEntry working `model.Mt940Entry`
     * @param string string to parse date from
     * @return the rest of the string to be parsed
     * @throws DateTimeParseException thrown if date format is bad
     */
    @Throws(DateTimeParseException::class)
    private fun parseDatumJJMMTT(currentEntry: Mt940Entry?, string: String): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")
        val date = string.substring(0, 6)
        currentEntry!!.valutaDatum = LocalDate.from(dateTimeFormatter.parse(date))
        return string.substring(6)
    }
}