package com.nammasanthe.ledger.ui.vendor.addcustomer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.ui.components.NumberPad
import com.nammasanthe.ledger.ui.components.PinDots
import com.nammasanthe.ledger.ui.theme.VendorLight
import com.nammasanthe.ledger.ui.theme.VendorPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerScreen(
    onDone: () -> Unit,
    onBack: () -> Unit,
    vm: AddCustomerViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Customer") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VendorPrimary, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (state.stage) {
                AddCustomerStage.NAME -> NameStage(state, vm)
                AddCustomerStage.PIN, AddCustomerStage.CONFIRM -> PinStage(state, vm)
                AddCustomerStage.DONE -> DoneStage(state, onDone)
            }
        }
    }
}

@Composable
private fun NameStage(state: AddCustomerState, vm: AddCustomerViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Customer Registration", style = MaterialTheme.typography.headlineMedium)
        Text("Enter the customer details below", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
        
        OutlinedTextField(
            value = state.name, onValueChange = vm::onNameChange,
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth(), placeholder = { Text("e.g. Lakshmi Devi") },
            singleLine = true, shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = state.phone, onValueChange = vm::onPhoneChange,
            label = { Text("WhatsApp Number") },
            modifier = Modifier.fillMaxWidth(), placeholder = { Text("e.g. 9845012345") },
            singleLine = true, shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        if (state.error.isNotEmpty()) Text(state.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)

        Surface(color = VendorLight, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Auto-assigned Customer ID", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                Spacer(Modifier.height(4.dp))
                Text(state.assignedCode, style = MaterialTheme.typography.displayLarge.copy(letterSpacing = 12.0.sp),
                    color = VendorPrimary)
            }
        }
        Button(onClick = vm::onNameNext, modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = VendorPrimary)) {
            Text("Set PIN for Customer", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}

@Composable
private fun PinStage(state: AddCustomerState, vm: AddCustomerViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        Text(if (state.stage == AddCustomerStage.PIN) "Set PIN for ${state.name}" else "Confirm PIN",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(if (state.stage == AddCustomerStage.PIN) "Choose a 4-digit PIN this customer will use"
             else "Enter the PIN again to confirm",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.55f),
            textAlign = TextAlign.Center)
        Spacer(Modifier.weight(1f))
        PinDots(enteredLength = if (state.stage == AddCustomerStage.CONFIRM) state.confirmBuffer.length else state.pinBuffer.length, color = VendorPrimary)
        Spacer(Modifier.height(16.dp))
        if (state.error.isNotEmpty()) Text(state.error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.weight(1f))
        NumberPad(onDigit = vm::onDigit, onDelete = vm::onDelete, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun DoneStage(state: AddCustomerState, onDone: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text("✅", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(16.dp))
        Text("Customer Added!", style = MaterialTheme.typography.headlineMedium)
        Text(state.name, style = MaterialTheme.typography.titleLarge, color = VendorPrimary)
        Spacer(Modifier.height(24.dp))
        Surface(color = VendorLight, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Customer ID (share with them)", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                Spacer(Modifier.height(8.dp))
                Text(state.assignedCode, style = MaterialTheme.typography.displayLarge, color = VendorPrimary)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("The customer uses this 4-digit ID + their PIN to log in and view their balance.",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.55f), textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = VendorPrimary)) {
            Text("Done", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}
