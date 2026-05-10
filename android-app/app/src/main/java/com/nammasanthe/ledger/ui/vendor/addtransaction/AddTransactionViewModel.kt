package com.nammasanthe.ledger.ui.vendor.addtransaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.util.FormatUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TxStep { SELECT_CUSTOMER, ENTER_AMOUNT }

data class AddTransactionState(
    val step: TxStep = TxStep.SELECT_CUSTOMER,
    val customers: List<CustomerEntity> = emptyList(),
    val selectedCustomer: CustomerEntity? = null,
    val amountBuffer: String = "",
    val txType: TransactionType = TransactionType.CREDIT,
    val saved: Boolean = false
)

class AddTransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository
    private val _state = MutableStateFlow(AddTransactionState())
    val state: StateFlow<AddTransactionState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(customers = repo.getCustomers()) }
        }
    }

    fun selectCustomer(c: CustomerEntity) {
        _state.update { it.copy(selectedCustomer = c, step = TxStep.ENTER_AMOUNT) }
    }

    fun backToSelect() = _state.update { it.copy(step = TxStep.SELECT_CUSTOMER) }

    fun onDigit(d: String) {
        if (_state.value.amountBuffer.length >= 8) return
        _state.update { it.copy(amountBuffer = it.amountBuffer + d) }
    }

    fun onDelete() = _state.update { it.copy(amountBuffer = it.amountBuffer.dropLast(1)) }

    fun toggleType(type: TransactionType) = _state.update { it.copy(txType = type) }

    fun save() {
        val st = _state.value
        val amount = st.amountBuffer.toDoubleOrNull() ?: return
        if (amount <= 0) return
        viewModelScope.launch {
            val vendor = repo.getVendor() ?: return@launch
            repo.saveTransaction(TransactionEntity(
                id = repo.generateId(),
                customerId = st.selectedCustomer!!.id,
                vendorId = vendor.id,
                amount = amount,
                type = st.txType,
                date = FormatUtil.nowIso()
            ))
            _state.update { it.copy(saved = true) }
        }
    }
}
