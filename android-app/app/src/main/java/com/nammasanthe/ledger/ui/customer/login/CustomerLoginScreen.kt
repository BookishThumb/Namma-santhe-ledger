package com.nammasanthe.ledger.ui.customer.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.ui.components.NumberPad
import com.nammasanthe.ledger.ui.components.PinDots
import com.nammasanthe.ledger.ui.theme.CustomerPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerLoginScreen(
    onLoginSuccess: (String) -> Unit,
    onBack: () -> Unit,
    vm: CustomerLoginViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    LaunchedEffect(state.loginSuccess) { if (state.loginSuccess) onLoginSuccess(vm.successfulCode()) }

    Scaffold(
        containerColor = CustomerPrimary,
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(12.dp))
            Text(if (state.stage == CustomerLoginStage.ENTER_CODE) "Customer Login" else "Welcome,\n${state.foundName}",
                style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(Modifier.height(6.dp))
            Text(if (state.stage == CustomerLoginStage.ENTER_CODE) "Enter the 4-digit ID your vendor gave you"
                 else "Enter your 4-digit PIN to view your balance",
                style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(0.8f))
            Spacer(Modifier.height(24.dp))
            Surface(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp), color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (state.stage == CustomerLoginStage.ENTER_CODE) {
                        Spacer(Modifier.height(8.dp))
                        Text("Your Customer ID", style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.align(Alignment.Start), color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = state.codeInput, onValueChange = vm::onCodeChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("XXXX", style = MaterialTheme.typography.headlineMedium) },
                            singleLine = true, shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center, letterSpacing = 12.0.sp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        if (state.codeError.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(state.codeError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = vm::onCodeNext, modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = CustomerPrimary)) {
                            Text("Continue", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }
                    } else {
                        Spacer(Modifier.weight(1f))
                        PinDots(enteredLength = state.pinBuffer.length, color = CustomerPrimary)
                        Spacer(Modifier.height(16.dp))
                        if (state.pinError.isNotEmpty()) Text(state.pinError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.weight(1f))
                        NumberPad(onDigit = vm::onDigit, onDelete = vm::onDelete, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

