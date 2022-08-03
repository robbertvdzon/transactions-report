package model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class Mt940Entry {
    // 'Credit', in mt940, means money was transferred 
    // to the current account.
    enum class SollHabenKennung {
        CREDIT, DEBIT
        //STORNO_DEBIT,
        //STORNO_CREDIT
    }

    var valutaDatum // date
            : LocalDate? = null
    var sollHabenKennung: SollHabenKennung? = null

    //    public void setMehrzweckfeld(String mehrzweckfeld) {
    //        this.mehrzweckfeld = mehrzweckfeld;
    //    }
    var betrag // amount
            : BigDecimal? = null
    var description // multi purpose field
            : String? = null
        private set
    var kontobezeichnung: String? = null
    override fun toString(): String {
        val sb = StringBuilder("At ").append(valutaDatum)
        if (kontobezeichnung != null) {
            sb.append(" (")
            sb.append(kontobezeichnung)
            sb.append(")")
        }
        sb.append(", ")
        sb.append(sollHabenKennung)
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

    fun addToMehrzweckfeld(string: String) {
        if (description == null || description!!.trim { it <= ' ' }.isEmpty()) {
            description = string
        } else {
            description += " "
            description += string
        }
    }
}