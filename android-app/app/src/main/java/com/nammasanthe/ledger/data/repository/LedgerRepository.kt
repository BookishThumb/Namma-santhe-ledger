package com.nammasanthe.ledger.data.repository

import com.nammasanthe.ledger.data.db.NammaDatabase
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.data.db.entities.VendorEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Single source of truth for all data operations.
 * Equivalent to a combined Room DAO facade.
 */
class LedgerRepository(private val db: NammaDatabase) {

    // ─── Vendor ──────────────────────────────────────────────────────────────

    suspend fun getVendor(): VendorEntity? = db.vendorDao().getVendor()

    suspend fun saveVendor(vendor: VendorEntity) = db.vendorDao().upsertVendor(vendor)

    // ─── Customers ───────────────────────────────────────────────────────────

    fun observeCustomers(): Flow<List<CustomerEntity>> =
        db.customerDao().observeCustomers()

    suspend fun getCustomers(): List<CustomerEntity> =
        db.customerDao().getCustomers()

    suspend fun getCustomerById(id: String): CustomerEntity? =
        db.customerDao().getCustomerById(id)

    suspend fun getCustomerByCode(code: String): CustomerEntity? =
        db.customerDao().getCustomerByCode(code)

    suspend fun saveCustomer(customer: CustomerEntity) =
        db.customerDao().upsertCustomer(customer)

    suspend fun deleteCustomer(customerId: String) {
        db.transactionDao().deleteTransactionsForCustomer(customerId)
        db.customerDao().deleteCustomer(customerId)
    }

    /** Generates a 4-digit code that is not already taken. */
    suspend fun generateUniqueCode(): String {
        val used = db.customerDao().getAllCustomerCodes().toSet()
        var code: String
        do {
            code = (1000..9999).random().toString()
        } while (code in used)
        return code
    }

    // ─── Transactions ─────────────────────────────────────────────────────────

    fun observeTransactionsForCustomer(customerId: String): Flow<List<TransactionEntity>> =
        db.transactionDao().observeTransactionsForCustomer(customerId)

    suspend fun getTransactionsForCustomer(customerId: String): List<TransactionEntity> =
        db.transactionDao().getTransactionsForCustomer(customerId)

    suspend fun getTransactionsForVendor(vendorId: String): List<TransactionEntity> =
        db.transactionDao().getTransactionsForVendor(vendorId)

    suspend fun getAllTransactions(): List<TransactionEntity> =
        db.transactionDao().getAllTransactions()

    suspend fun saveTransaction(tx: TransactionEntity) =
        db.transactionDao().insertTransaction(tx)

    // ─── Helpers ─────────────────────────────────────────────────────────────

    fun generateId(): String = UUID.randomUUID().toString()
}
