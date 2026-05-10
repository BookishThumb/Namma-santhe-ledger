package com.nammasanthe.ledger.ui.vendor.customerdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.ui.components.InterestBreakdown
import com.nammasanthe.ledger.ui.components.TransactionItem
import com.nammasanthe.ledger.ui.theme.CreditRed
import com.nammasanthe.ledger.ui.theme.CreditRedLight
import com.nammasanthe.ledger.ui.theme.PaymentGreen
import com.nammasanthe.ledger.ui.theme.VendorLight
import com.nammasanthe.ledger.ui.theme.VendorPrimary
import com.nammasanthe.ledger.util.FormatUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    customerId: String,
    onBack: () -> Unit,
    vm: CustomerDetailViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPayDialog by remember { mutableStateOf(false) }

    LaunchedEffect(customerId) { vm.load(customerId) }
    LaunchedEffect(state.navigateBack) { if (state.navigateBack) onBack() }

    val totalDue = state.balance + state.interest

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.customer?.name ?: "") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VendorPrimary, titleContentColor = Color.White, navigationIconContentColor = Color.White, actionIconContentColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 24.dp)) {
            // Balance header
            item {
                Surface(color = if (state.balance > 0) CreditRedLight else VendorLight, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(color = if (state.balance > 0) CreditRed else PaymentGreen, shape = CircleShape, modifier = Modifier.size(72.dp)) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(state.customer?.name?.first()?.uppercase() ?: "", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Customer ID: ${state.customer?.customerCode}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                        Spacer(Modifier.height(4.dp))
                        if (state.balance > 0) {
                            Text("Total Due (with interest)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                            Text(FormatUtil.formatAmount(totalDue), style = MaterialTheme.typography.displayLarge, color = CreditRed)
                            Text("Principal ${FormatUtil.formatAmount(state.balance)} + Interest ${FormatUtil.formatAmount(state.interest)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                            Spacer(Modifier.height(12.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { showPayDialog = true }, 
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PaymentGreen),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Mark Paid", color = Color.White, style = MaterialTheme.typography.titleMedium)
                                }
                                
                                OutlinedButton(
                                    onClick = { vm.sendWhatsAppAlert() },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VendorPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Outlined.NotificationsActive, null, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("WhatsApp", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        } else {
                            Text("Fully Paid", style = MaterialTheme.typography.headlineMedium, color = PaymentGreen)
                            Text("No dues pending", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                        }
                    }
                }
            }

            // Interest breakdown
            if (state.balance > 0) {
                item {
                    InterestBreakdown(principal = state.balance, interest = state.interest, accentColor = CreditRed,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }

            // Section header
            item {
                Text("Transaction History (${state.transactions.size})",
                    style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
            }

            if (state.transactions.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(vertical = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions yet", color = MaterialTheme.colorScheme.onSurface.copy(0.45f))
                    }
                }
            }

            items(state.transactions, key = { it.id }) { tx ->
                TransactionItem(tx, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }
        }
    }

    if (showPayDialog) {
        AlertDialog(
            onDismissRequest = { showPayDialog = false },
            title = { Text("Mark as Paid") },
            text = { Text("Record full payment of ${FormatUtil.formatAmount(totalDue)} from ${state.customer?.name}?\n\nPrincipal: ${FormatUtil.formatAmount(state.balance)}\nInterest: ${FormatUtil.formatAmount(state.interest)}\nTotal: ${FormatUtil.formatAmount(totalDue)}") },
            confirmButton = { TextButton(onClick = { vm.markAsPaid(customerId); showPayDialog = false }) { Text("Confirm") } },
            dismissButton = { TextButton(onClick = { showPayDialog = false }) { Text("Cancel") } }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Customer") },
            text = { Text("Remove ${state.customer?.name} and all their transaction history? This cannot be undone.") },
            confirmButton = { TextButton(onClick = { vm.deleteCustomer(customerId) }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") } },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }

    state.whatsappMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { vm.clearWhatsappMessage() },
            title = { Text(if (msg.startsWith("ERROR")) "API Key Required" else "WhatsApp Alert") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = { vm.clearWhatsappMessage() }) {
                    Text("OK")
                }
            }
        )
    }
}
