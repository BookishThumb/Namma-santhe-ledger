package com.nammasanthe.ledger.ui.vendor.addcustomer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.util.HashUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AddCustomerStage { NAME, PIN, CONFIRM, DONE }

data class AddCustomerState(
    val stage: AddCustomerStage = AddCustomerStage.NAME,
    val name: String = "",
    val phone: String = "",
    val assignedCode: String = "",
    val pinBuffer: String = "",
    val confirmBuffer: String = "",
    val error: String = ""
)

class AddCustomerViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository
    private val vendorId get() = runCatching {
        kotlinx.coroutines.runBlocking { repo.getVendor()?.id ?: "" }
    }.getOrDefault("")

    private val _state = MutableStateFlow(AddCustomerState())
    val state: StateFlow<AddCustomerState> = _state.asStateFlow()

    private var pendingPin = ""

    init {
        viewModelScope.launch {
            val code = repo.generateUniqueCode()
            _state.update { it.copy(assignedCode = code) }
        }
    }

    fun onNameChange(s: String) = _state.update { it.copy(name = s, error = "") }

    fun onPhoneChange(s: String) = _state.update { it.copy(phone = s, error = "") }

    fun onNameNext() {
        val name = _state.value.name.trim()
        val phone = _state.value.phone.trim()
        if (name.length < 2) { _state.update { it.copy(error = "Please enter a valid name") }; return }
        if (phone.length < 10) { _state.update { it.copy(error = "Please enter a valid 10-digit phone number") }; return }
        _state.update { it.copy(stage = AddCustomerStage.PIN, error = "") }
    }

    fun onDigit(d: String) {
        val st = _state.value
        when (st.stage) {
            AddCustomerStage.PIN -> {
                if (st.pinBuffer.length < 4) {
                    val nb = st.pinBuffer + d
                    _state.update { it.copy(pinBuffer = nb) }
                    if (nb.length == 4) {
                        pendingPin = nb
                        viewModelScope.launch { kotlinx.coroutines.delay(300); _state.update { it.copy(stage = AddCustomerStage.CONFIRM, confirmBuffer = "") } }
                    }
                }
            }
            AddCustomerStage.CONFIRM -> {
                if (st.confirmBuffer.length < 4) {
                    val nb = st.confirmBuffer + d
                    _state.update { it.copy(confirmBuffer = nb, error = "") }
                    if (nb.length == 4) { viewModelScope.launch { kotlinx.coroutines.delay(300); save(nb) } }
                }
            }
            else -> {}
        }
    }

    fun onDelete() {
        val st = _state.value
        _state.update {
            when (st.stage) {
                AddCustomerStage.PIN     -> it.copy(pinBuffer = st.pinBuffer.dropLast(1))
                AddCustomerStage.CONFIRM -> it.copy(confirmBuffer = st.confirmBuffer.dropLast(1))
                else -> it
            }
        }
    }

    private suspend fun save(confirmed: String) {
        if (confirmed != pendingPin) {
            _state.update { it.copy(stage = AddCustomerStage.PIN, pinBuffer = "", confirmBuffer = "", error = "PINs do not match. Try again.") }
            return
        }
        val st = _state.value
        repo.saveCustomer(CustomerEntity(
            id = repo.generateId(),
            vendorId = vendorId,
            name = st.name.trim(),
            customerCode = st.assignedCode,
            pin = HashUtil.hashPin(confirmed),
            phone = st.phone.trim()
        ))
        _state.update { it.copy(stage = AddCustomerStage.DONE) }
    }
}
