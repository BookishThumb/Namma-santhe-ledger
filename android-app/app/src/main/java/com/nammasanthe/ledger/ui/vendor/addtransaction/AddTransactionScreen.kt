package com.nammasanthe.ledger.ui.vendor.addtransaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.ui.components.NumberPad
import com.nammasanthe.ledger.ui.theme.CreditRed
import com.nammasanthe.ledger.ui.theme.PaymentGreen
import com.nammasanthe.ledger.ui.theme.VendorLight
import com.nammasanthe.ledger.ui.theme.VendorPrimary
import com.nammasanthe.ledger.util.FormatUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onDone: () -> Unit,
    onBack: () -> Unit,
    vm: AddTransactionViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(state.saved) { if (state.saved) onDone() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = { IconButton(onClick = { if (state.step == TxStep.ENTER_AMOUNT) vm.backToSelect() else onBack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VendorPrimary, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Step indicators
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp).padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                StepDot(1, state.step.ordinal + 1 >= 1, "Select Customer")
                HorizontalDivider(modifier = Modifier.weight(1f))
                StepDot(2, state.step.ordinal + 1 >= 2, "Enter Amount")
            }

            if (state.step == TxStep.SELECT_CUSTOMER) {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (state.customers.isEmpty()) {
                        item { Box(Modifier.fillMaxWidth().padding(vertical = 60.dp), contentAlignment = Alignment.Center) {
                            Text("No customers yet. Add a customer first.", color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                        }}
                    }
                    items(state.customers) { c ->
                        Card(modifier = Modifier.fillMaxWidth(), onClick = { vm.selectCustomer(c) },
                            shape = RoundedCornerShape(14.dp)) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                Surface(color = VendorLight, shape = CircleShape, modifier = Modifier.size(46.dp)) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(c.name.first().uppercase(), color = VendorPrimary, style = MaterialTheme.typography.titleLarge)
                                    }
                                }
                                Column {
                                    Text(c.name, style = MaterialTheme.typography.titleMedium)
                                    Text("ID: ${c.customerCode}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                                }
                            }
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(8.dp))
                    // Selected customer
                    Surface(color = VendorLight, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Customer", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                            Text(state.selectedCustomer?.name ?: "", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f), color = VendorPrimary)
                            TextButton(onClick = vm::backToSelect) { Text("Change") }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    // Amount display
                    val amount = state.amountBuffer.toDoubleOrNull() ?: 0.0
                    Text(if (amount > 0) FormatUtil.formatAmount(amount) else "₹0", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(12.dp))
                    // Type toggle
                    Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TypeToggleBtn("Given Credit", state.txType == TransactionType.CREDIT, CreditRed, Modifier.weight(1f)) { vm.toggleType(TransactionType.CREDIT) }
                            TypeToggleBtn("Payment Received", state.txType == TransactionType.PAYMENT, PaymentGreen, Modifier.weight(1f)) { vm.toggleType(TransactionType.PAYMENT) }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    NumberPad(onDigit = vm::onDigit, onDelete = vm::onDelete, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = vm::save, modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = amount > 0,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VendorPrimary)
                    ) { Text("Save Transaction", style = MaterialTheme.typography.titleMedium, color = Color.White) }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun StepDot(num: Int, active: Boolean, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(shape = CircleShape, color = if (active) VendorPrimary else MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.size(28.dp)) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("$num", style = MaterialTheme.typography.labelLarge, color = if (active) Color.White else MaterialTheme.colorScheme.onSurface.copy(0.5f))
            }
        }
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
    }
}

@Composable
private fun TypeToggleBtn(label: String, selected: Boolean, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier.height(48.dp), shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (selected) color else Color.Transparent,
            contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface.copy(0.55f)),
        elevation = ButtonDefaults.buttonElevation(0.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}
