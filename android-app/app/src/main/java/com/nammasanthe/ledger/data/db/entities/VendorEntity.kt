package com.nammasanthe.ledger.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Room entity — maps to the `vendors` table. One vendor per device. */
@Entity(tableName = "vendors")
data class VendorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val pin: String        // SHA-256 hashed
)
