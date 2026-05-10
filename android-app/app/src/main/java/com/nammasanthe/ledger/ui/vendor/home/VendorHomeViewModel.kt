package com.nammasanthe.ledger.ui.vendor.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.util.InterestCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CustomerSummary(
    val customer: CustomerEntity,
    val balance: Double,
    val interest: Double,
    val totalDue: Double
)

data class VendorHomeState(
    val vendorName: String = "",
    val customers: List<CustomerSummary> = emptyList(),
    val totalDues: Double = 0.0,
    val totalInterest: Double = 0.0,
    val isLoading: Boolean = true
)

class VendorHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository

    private val _state = MutableStateFlow(VendorHomeState())
    val state: StateFlow<VendorHomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val vendor = repo.getVendor()
            _state.update { it.copy(vendorName = vendor?.name ?: "") }
        }
        viewModelScope.launch {
            repo.observeCustomers().collectLatest { customers ->
                val allTxs = repo.getAllTransactions()
                val summaries = customers.map { c ->
                    val txs = allTxs.filter { it.customerId == c.id }
                    val balance  = InterestCalculator.calculateBalance(txs)
                    val interest = if (balance > 0) InterestCalculator.calculateInterest(txs) else 0.0
                    CustomerSummary(c, balance, interest, if (balance > 0) balance + interest else 0.0)
                }.sortedByDescending { it.totalDue }
                val totalDues     = summaries.sumOf { it.totalDue }
                val totalInterest = summaries.sumOf { it.interest }
                _state.update { it.copy(customers = summaries, totalDues = totalDues, totalInterest = totalInterest, isLoading = false) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val customers = repo.getCustomers()
            val allTxs = repo.getAllTransactions()
            val summaries = customers.map { c ->
                val txs = allTxs.filter { it.customerId == c.id }
                val balance  = InterestCalculator.calculateBalance(txs)
                val interest = if (balance > 0) InterestCalculator.calculateInterest(txs) else 0.0
                CustomerSummary(c, balance, interest, if (balance > 0) balance + interest else 0.0)
            }.sortedByDescending { it.totalDue }
            _state.update { it.copy(customers = summaries, totalDues = summaries.sumOf { it.totalDue }, totalInterest = summaries.sumOf { it.interest }) }
        }
    }
}
