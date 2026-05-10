package com.nammasanthe.ledger.ui.vendor.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.ui.components.NumberPad
import com.nammasanthe.ledger.ui.components.PinDots
import com.nammasanthe.ledger.ui.theme.VendorPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorLoginScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit,
    vm: VendorLoginViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state.error) {
        if (state.error == "SUCCESS") onLoginSuccess()
    }

    Scaffold(
        containerColor = VendorPrimary,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)
        ) {
            // Header
            Spacer(Modifier.height(12.dp))
            Text(
                text = when (state.stage) {
                    VendorLoginStage.SETUP_NAME    -> "Welcome, Vendor"
                    VendorLoginStage.SETUP_PIN     -> "Create your PIN"
                    VendorLoginStage.SETUP_CONFIRM -> "Confirm your PIN"
                    else -> "Welcome back,\n${state.vendorName}"
                },
                style = MaterialTheme.typography.headlineLarge, color = Color.White
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = when (state.stage) {
                    VendorLoginStage.SETUP_NAME    -> "Enter your name to get started"
                    VendorLoginStage.SETUP_PIN     -> "Choose a 4-digit PIN"
                    VendorLoginStage.SETUP_CONFIRM -> "Re-enter the PIN to confirm"
                    else -> "Enter your PIN to continue"
                },
                style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(24.dp))

            // Card
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.stage == VendorLoginStage.SETUP_NAME) {
                        // Name input
                        Spacer(Modifier.height(8.dp))
                        Text("Your Name", style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.align(Alignment.Start),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.nameInput,
                            onValueChange = vm::onNameChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Ravi Kumar") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        if (state.error.isNotEmpty() && state.error != "SUCCESS") {
                            Spacer(Modifier.height(8.dp))
                            Text(state.error, color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = vm::onNameNext, modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VendorPrimary)) {
                            Text("Next", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }
                    } else {
                        // PIN dots + number pad
                        Spacer(Modifier.weight(1f))
                        val pinLen = when (state.stage) {
                            VendorLoginStage.SETUP_CONFIRM -> state.confirmBuffer.length
                            else -> state.pinBuffer.length
                        }
                        PinDots(enteredLength = pinLen, color = VendorPrimary)
                        Spacer(Modifier.height(16.dp))
                        if (state.error.isNotEmpty() && state.error != "SUCCESS") {
                            Text(state.error, color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.weight(1f))
                        NumberPad(onDigit = vm::onDigit, onDelete = vm::onDelete,
                            modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
