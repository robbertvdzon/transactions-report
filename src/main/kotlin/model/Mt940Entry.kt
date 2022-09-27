package model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class Mt940Entry {
    enum class DebetID {
        CREDIT, DEBIT
    }

    var valutaDatum : LocalDate? = null
    var debetID: DebetID? = null
    var betrag : BigDecimal? = null
    var description : String? = null
        private set
    var rekeningaanduiding: String? = null

    fun getSummary() = debetID?.name+rekeningaanduiding+description

    override fun toString(): String {
        val sb = StringBuilder("At ").append(valutaDatum)
        if (rekeningaanduiding != null) {
            sb.append(" (")
            sb.append(rekeningaanduiding)
            sb.append(")")
        }
        sb.append(", ")
        sb.append(debetID)
        sb.append(": ")
        if (betrag != null) {
            // Set scale to 2 digits and round if necessary, then to plain string for nicer output.
            sb.append(betrag!!.setScale(2, RoundingMode.HALF_EVEN).toPlainString())
        } else {
            sb.append("-")
        }
        sb.append(" for ")
        sb.append(description)
        return sb.toString()
    }

    fun addToDescription(string: String) {
        if (description == null || description!!.trim { it <= ' ' }.isEmpty()) {
            description = string
        } else {
            description += " "
            description += string
        }
    }
}