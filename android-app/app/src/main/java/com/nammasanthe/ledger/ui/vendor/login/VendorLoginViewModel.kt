package com.nammasanthe.ledger.ui.vendor.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammasanthe.ledger.NammaApplication
import com.nammasanthe.ledger.data.db.entities.VendorEntity
import com.nammasanthe.ledger.util.HashUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

enum class VendorLoginStage { CHECKING, SETUP_NAME, SETUP_PIN, SETUP_CONFIRM, LOGIN_PIN }

data class VendorLoginState(
    val stage: VendorLoginStage = VendorLoginStage.CHECKING,
    val vendorName: String = "",
    val nameInput: String = "",
    val pinBuffer: String = "",
    val confirmBuffer: String = "",
    val error: String = ""
)

class VendorLoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as NammaApplication).repository

    private val _state = MutableStateFlow(VendorLoginState())
    val state: StateFlow<VendorLoginState> = _state.asStateFlow()

    private var pendingPin = ""

    init {
        viewModelScope.launch {
            val vendor = repo.getVendor()
            _state.value = if (vendor != null) {
                _state.value.copy(stage = VendorLoginStage.LOGIN_PIN, vendorName = vendor.name)
            } else {
                _state.value.copy(stage = VendorLoginStage.SETUP_NAME)
            }
        }
    }

    fun onNameChange(s: String) = run { _state.value = _state.value.copy(nameInput = s, error = "") }

    fun onNameNext() {
        val name = _state.value.nameInput.trim()
        if (name.length < 2) { _state.value = _state.value.copy(error = "Please enter your name"); return }
        _state.value = _state.value.copy(stage = VendorLoginStage.SETUP_PIN, vendorName = name, pinBuffer = "")
    }

    fun onDigit(d: String) {
        val st = _state.value
        when (st.stage) {
            VendorLoginStage.SETUP_PIN -> {
                if (st.pinBuffer.length < 4) {
                    val nb = st.pinBuffer + d
                    _state.value = st.copy(pinBuffer = nb)
                    if (nb.length == 4) {
                        pendingPin = nb
                        viewModelScope.launch {
                            kotlinx.coroutines.delay(300)
                            _state.value = _state.value.copy(stage = VendorLoginStage.SETUP_CONFIRM, confirmBuffer = "")
                        }
                    }
                }
            }
            VendorLoginStage.SETUP_CONFIRM -> {
                if (st.confirmBuffer.length < 4) {
                    val nb = st.confirmBuffer + d
                    _state.value = st.copy(confirmBuffer = nb, error = "")
                    if (nb.length == 4) { viewModelScope.launch { kotlinx.coroutines.delay(300); confirmPin(nb) } }
                }
            }
            VendorLoginStage.LOGIN_PIN -> {
                if (st.pinBuffer.length < 4) {
                    val nb = st.pinBuffer + d
                    _state.value = st.copy(pinBuffer = nb, error = "")
                    if (nb.length == 4) { viewModelScope.launch { kotlinx.coroutines.delay(300); verifyLogin(nb) } }
                }
            }
            else -> {}
        }
    }

    fun onDelete() {
        val st = _state.value
        _state.value = when (st.stage) {
            VendorLoginStage.SETUP_PIN     -> st.copy(pinBuffer = st.pinBuffer.dropLast(1))
            VendorLoginStage.SETUP_CONFIRM -> st.copy(confirmBuffer = st.confirmBuffer.dropLast(1))
            VendorLoginStage.LOGIN_PIN     -> st.copy(pinBuffer = st.pinBuffer.dropLast(1))
            else -> st
        }
    }

    private suspend fun confirmPin(confirmed: String) {
        if (confirmed != pendingPin) {
            _state.value = _state.value.copy(
                stage = VendorLoginStage.SETUP_PIN, pinBuffer = "", confirmBuffer = "",
                error = "PINs do not match. Try again."
            )
            return
        }
        repo.saveVendor(VendorEntity(UUID.randomUUID().toString(), _state.value.vendorName, HashUtil.hashPin(confirmed)))
        _state.value = _state.value.copy(stage = VendorLoginStage.CHECKING, error = "SUCCESS")
    }

    private suspend fun verifyLogin(pin: String) {
        val vendor = repo.getVendor() ?: return
        if (HashUtil.verifyPin(pin, vendor.pin)) {
            _state.value = _state.value.copy(error = "SUCCESS")
        } else {
            _state.value = _state.value.copy(pinBuffer = "", error = "Wrong PIN. Please try again.")
        }
    }
}
