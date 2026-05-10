package com.nammasanthe.ledger.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nammasanthe.ledger.data.db.dao.CustomerDao
import com.nammasanthe.ledger.data.db.dao.TransactionDao
import com.nammasanthe.ledger.data.db.dao.VendorDao
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.data.db.entities.VendorEntity

class TransactionTypeConverter {
    @TypeConverter
    fun fromType(type: TransactionType): String = type.name

    @TypeConverter
    fun toType(value: String): TransactionType = TransactionType.valueOf(value)
}

@Database(
    entities = [VendorEntity::class, CustomerEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransactionTypeConverter::class)
abstract class NammaDatabase : RoomDatabase() {
    abstract fun vendorDao(): VendorDao
    abstract fun customerDao(): CustomerDao
    abstract fun transactionDao(): TransactionDao
}
