package com.nammasanthe.ledger.ui.vendor.dailysummary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId

data class DailySummaryState(
    val todayTransactions: List<TransactionEntity> = emptyList(),
    val customers: List<CustomerEntity> = emptyList(),
    val totalCredit: Double = 0.0,
    val totalPayment: Double = 0.0,
    val isLoading: Boolean = true
)

class DailySummaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository
    private val _state = MutableStateFlow(DailySummaryState())
    val state: StateFlow<DailySummaryState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            val vendor = repo.getVendor() ?: return@launch
            val allTxs = repo.getTransactionsForVendor(vendor.id)
            val customers = repo.getCustomers()
            val today = LocalDate.now(ZoneId.systemDefault())
            val todayTxs = allTxs.filter { tx ->
                try {
                    val date = Instant.parse(tx.date).atZone(ZoneId.systemDefault()).toLocalDate()
                    date == today
                } catch (e: Exception) { false }
            }
            val credit  = todayTxs.filter { it.type == TransactionType.CREDIT }.sumOf { it.amount }
            val payment = todayTxs.filter { it.type == TransactionType.PAYMENT }.sumOf { it.amount }
            _state.update { it.copy(todayTransactions = todayTxs, customers = customers, totalCredit = credit, totalPayment = payment, isLoading = false) }
        }
    }

    fun customerName(id: String) = _state.value.customers.find { it.id == id }?.name ?: "Unknown"
}
