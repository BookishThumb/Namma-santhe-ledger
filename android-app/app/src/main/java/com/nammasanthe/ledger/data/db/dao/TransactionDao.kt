package com.nammasanthe.ledger.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions WHERE customer_id = :customerId ORDER BY date DESC")
    fun observeTransactionsForCustomer(customerId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE customer_id = :customerId ORDER BY date DESC")
    suspend fun getTransactionsForCustomer(customerId: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE vendor_id = :vendorId ORDER BY date DESC")
    suspend fun getTransactionsForVendor(vendorId: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE customer_id = :customerId")
    suspend fun deleteTransactionsForCustomer(customerId: String)
}
