package com.nammasanthe.ledger.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class TransactionType { CREDIT, PAYMENT }

/** Room entity — maps to the `transactions` table. */
@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["customer_id"]),
        Index(value = ["vendor_id"])
    ]
)
data class TransactionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "customer_id") val customerId: String,
    @ColumnInfo(name = "vendor_id") val vendorId: String,
    val amount: Double,
    val type: TransactionType,
    val date: String,           // ISO-8601 string
    val note: String? = null
)
