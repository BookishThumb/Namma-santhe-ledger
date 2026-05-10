package com.nammasanthe.ledger.ui.customer.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.util.InterestCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomerHomeState(
    val customerName: String = "",
    val customerId: String = "",
    val transactions: List<TransactionEntity> = emptyList(),
    val balance: Double = 0.0,
    val interest: Double = 0.0,
    val isLoading: Boolean = true
)

class CustomerHomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository
    private val _state = MutableStateFlow(CustomerHomeState())
    val state: StateFlow<CustomerHomeState> = _state.asStateFlow()

    /** Call once after obtaining the customer code from the login screen. */
    fun loadForCode(code: String) {
        viewModelScope.launch {
            val customer = repo.getCustomerByCode(code) ?: return@launch
            _state.update { it.copy(customerName = customer.name, customerId = customer.id) }
            repo.observeTransactionsForCustomer(customer.id).collectLatest { txs ->
                val balance  = InterestCalculator.calculateBalance(txs)
                val interest = if (balance > 0) InterestCalculator.calculateInterest(txs) else 0.0
                _state.update { it.copy(transactions = txs, balance = balance, interest = interest, isLoading = false) }
            }
        }
    }
}
