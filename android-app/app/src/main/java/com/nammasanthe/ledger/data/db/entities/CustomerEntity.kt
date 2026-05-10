package com.nammasanthe.ledger.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Room entity — maps to the `customers` table. */
@Entity(
    tableName = "customers",
    indices = [Index(value = ["customer_code"], unique = true)]
)
data class CustomerEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "vendor_id") val vendorId: String,
    val name: String,
    @ColumnInfo(name = "customer_code") val customerCode: String,   // 4-digit unique code
    val pin: String,        // SHA-256 hashed
    val phone: String = ""  // WhatsApp number
)
