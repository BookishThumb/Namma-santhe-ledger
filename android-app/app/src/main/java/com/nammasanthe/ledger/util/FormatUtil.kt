package com.nammasanthe.ledger.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatUtil {

    private val inrFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    private val displayDateFormat = SimpleDateFormat("d MMM yyyy", Locale("en", "IN"))

    /** Formats a double as Indian Rupee: ₹1,000 */
    fun formatAmount(amount: Double): String =
        inrFormat.format(amount).replace("₹", "₹")

    /** Formats an ISO date string to a readable date. */
    fun formatDate(isoDate: String): String = try {
        val date = Date(isoDate)
        displayDateFormat.format(date)
    } catch (e: Exception) {
        isoDate
    }

    /** Returns current time as ISO-8601 string. */
    fun nowIso(): String = Date().toInstant().toString()
}
