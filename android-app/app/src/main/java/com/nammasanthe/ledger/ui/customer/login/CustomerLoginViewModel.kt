package com.nammasanthe.ledger.ui.customer.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.util.HashUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CustomerLoginStage { ENTER_CODE, ENTER_PIN }

data class CustomerLoginState(
    val stage: CustomerLoginStage = CustomerLoginStage.ENTER_CODE,
    val codeInput: String = "",
    val foundName: String = "",
    val pinBuffer: String = "",
    val codeError: String = "",
    val pinError: String = "",
    val loginSuccess: Boolean = false
)

class CustomerLoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository
    private val _state = MutableStateFlow(CustomerLoginState())
    val state: StateFlow<CustomerLoginState> = _state.asStateFlow()

    fun onCodeChange(s: String) {
        val filtered = s.filter { it.isDigit() }.take(4)
        _state.update { it.copy(codeInput = filtered, codeError = "") }
    }

    fun onCodeNext() {
        val code = _state.value.codeInput
        if (code.length != 4) { _state.update { it.copy(codeError = "Please enter your 4-digit Customer ID") }; return }
        viewModelScope.launch {
            val customer = repo.getCustomerByCode(code)
            if (customer == null) {
                _state.update { it.copy(codeError = "No customer found with this ID. Check with your vendor.") }
            } else {
                _state.update { it.copy(stage = CustomerLoginStage.ENTER_PIN, foundName = customer.name, pinBuffer = "") }
            }
        }
    }

    fun onDigit(d: String) {
        val st = _state.value
        if (st.stage == CustomerLoginStage.ENTER_PIN && st.pinBuffer.length < 4) {
            val nb = st.pinBuffer + d
            _state.update { it.copy(pinBuffer = nb, pinError = "") }
            if (nb.length == 4) { viewModelScope.launch { kotlinx.coroutines.delay(300); verifyPin(nb) } }
        }
    }

    fun onDelete() = _state.update { it.copy(pinBuffer = it.pinBuffer.dropLast(1)) }

    private suspend fun verifyPin(pin: String) {
        val customer = repo.getCustomerByCode(_state.value.codeInput) ?: return
        if (HashUtil.verifyPin(pin, customer.pin)) {
            _state.update { it.copy(loginSuccess = true) }
        } else {
            _state.update { it.copy(pinBuffer = "", pinError = "Wrong PIN. Please try again.") }
        }
    }

    fun successfulCode(): String = _state.value.codeInput
}
