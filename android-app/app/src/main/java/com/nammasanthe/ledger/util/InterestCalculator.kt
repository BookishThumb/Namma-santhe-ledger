package com.nammasanthe.ledger.util

import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import kotlin.math.max
import kotlin.math.roundToLong

const val ANNUAL_INTEREST_RATE = 0.12   // 12% per annum

object InterestCalculator {

    /** Principal balance = sum of credits − sum of payments. */
    fun calculateBalance(transactions: List<TransactionEntity>): Double =
        transactions.fold(0.0) { acc, tx ->
            if (tx.type == TransactionType.CREDIT) acc + tx.amount else acc - tx.amount
        }

    /**
     * Accrues 12% p.a. simple interest on the running balance between each
     * transaction (chronological order), then from the last transaction to now.
     *
     * Returns 0 when the balance is fully settled.
     */
    fun calculateInterest(transactions: List<TransactionEntity>): Double {
        if (transactions.isEmpty()) return 0.0

        val sorted = transactions.sortedBy { it.date }
        val nowMs = System.currentTimeMillis()

        var runningBalance = 0.0
        var totalInterest = 0.0
        var lastMs = parseMs(sorted.first().date)

        for (tx in sorted) {
            val txMs = parseMs(tx.date)
            val days = (txMs - lastMs) / (1000.0 * 60 * 60 * 24)
            if (runningBalance > 0 && days > 0) {
                totalInterest += runningBalance * ANNUAL_INTEREST_RATE * (days / 365.0)
            }
            runningBalance = if (tx.type == TransactionType.CREDIT) {
                runningBalance + tx.amount
            } else {
                max(0.0, runningBalance - tx.amount)
            }
            lastMs = txMs
        }

        val daysToNow = (nowMs - lastMs) / (1000.0 * 60 * 60 * 24)
        if (runningBalance > 0 && daysToNow > 0) {
            totalInterest += runningBalance * ANNUAL_INTEREST_RATE * (daysToNow / 365.0)
        }

        return totalInterest.roundToLong().toDouble()
    }

    private fun parseMs(isoDate: String): Long = try {
        java.time.Instant.parse(isoDate).toEpochMilli()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}
