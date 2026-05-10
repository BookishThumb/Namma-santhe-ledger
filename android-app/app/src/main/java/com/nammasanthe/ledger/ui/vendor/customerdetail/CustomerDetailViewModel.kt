package com.nammasanthe.ledger.ui.vendor.customerdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.util.FormatUtil
import com.nammasanthe.ledger.util.InterestCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CustomerDetailState(
    val customer: CustomerEntity? = null,
    val transactions: List<TransactionEntity> = emptyList(),
    val balance: Double = 0.0,
    val interest: Double = 0.0,
    val isLoading: Boolean = true,
    val navigateBack: Boolean = false,
    val whatsappMessage: String? = null
)

class CustomerDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository
    private val _state = MutableStateFlow(CustomerDetailState())
    val state: StateFlow<CustomerDetailState> = _state.asStateFlow()

    fun load(customerId: String) {
        viewModelScope.launch {
            repo.observeTransactionsForCustomer(customerId).collectLatest { txs ->
                val customer  = repo.getCustomerById(customerId)
                val balance   = InterestCalculator.calculateBalance(txs)
                val interest  = if (balance > 0) InterestCalculator.calculateInterest(txs) else 0.0
                _state.update { it.copy(customer = customer, transactions = txs, balance = balance, interest = interest, isLoading = false) }
            }
        }
    }

    fun markAsPaid(customerId: String) {
        viewModelScope.launch {
            val st = _state.value
            val totalDue = st.balance + st.interest
            if (totalDue <= 0) return@launch
            val vendor = repo.getVendor() ?: return@launch
            repo.saveTransaction(TransactionEntity(
                id = repo.generateId(), customerId = customerId, vendorId = vendor.id,
                amount = totalDue, type = TransactionType.PAYMENT, date = FormatUtil.nowIso(),
                note = "Full payment (incl. ₹${st.interest.toInt()} interest)"
            ))
        }
    }

    fun deleteCustomer(customerId: String) {
        viewModelScope.launch {
            repo.deleteCustomer(customerId)
            _state.update { it.copy(navigateBack = true) }
        }
    }

    fun sendWhatsAppAlert() {
        val st = _state.value
        val phone = st.customer?.phone ?: ""
        
        // SIMULATED API KEY CHECK
        // In a real app, this would be fetched from BuildConfig or an encrypted preference
        val whatsappApiKey: String? = null 

        if (whatsappApiKey == null) {
            _state.update { it.copy(whatsappMessage = "ERROR: WhatsApp API Key not found. Please add your API key in the configuration to send alerts.") }
        } else {
            // This is where the actual API call would go
            _state.update { it.copy(whatsappMessage = "SUCCESS: WhatsApp alert sent to $phone") }
        }
    }

    fun clearWhatsappMessage() {
        _state.update { it.copy(whatsappMessage = null) }
    }
}
