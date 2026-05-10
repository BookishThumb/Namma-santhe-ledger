package com.nammasanthe.ledger.ui.customer.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.ui.components.InterestBreakdown
import com.nammasanthe.ledger.ui.components.TransactionItem
import com.nammasanthe.ledger.ui.theme.CustomerLight
import com.nammasanthe.ledger.ui.theme.CustomerPrimary
import com.nammasanthe.ledger.util.FormatUtil

@Composable
fun CustomerHomeScreen(
    customerCode: String = "",
    onLogout: () -> Unit,
    vm: CustomerHomeViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(customerCode) {
        if (customerCode.isNotEmpty()) vm.loadForCode(customerCode)
    }

    val totalDue = state.balance + state.interest

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Header
            item {
                Surface(color = CustomerPrimary) {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(state.customerName, style = MaterialTheme.typography.titleLarge, color = Color.White)
                                Text("Your Credit Ledger", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(0.75f))
                            }
                            IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, null, tint = Color.White) }
                        }
                        Spacer(Modifier.height(16.dp))
                        Surface(color = Color.Black.copy(alpha = 0.15f), shape = RoundedCornerShape(18.dp)) {
                            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (state.balance > 0) "You Owe (with interest)" else "You are Clear",
                                    style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(0.85f))
                                Spacer(Modifier.height(4.dp))
                                Text(if (state.balance > 0) FormatUtil.formatAmount(totalDue) else "₹0",
                                    style = MaterialTheme.typography.displayLarge, color = Color.White)
                                if (state.balance > 0) {
                                    Spacer(Modifier.height(10.dp))
                                    Surface(color = Color.Black.copy(0.15f), shape = RoundedCornerShape(10.dp)) {
                                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp)) {
                                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text("Principal", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.75f))
                                                Text(FormatUtil.formatAmount(state.balance), style = MaterialTheme.typography.labelLarge, color = Color.White.copy(0.9f))
                                            }
                                            Spacer(Modifier.height(4.dp))
                                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                Text("Interest (12% p.a.)", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.75f))
                                                Text("+${FormatUtil.formatAmount(state.interest)}", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(0.9f))
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text("Please pay your vendor as soon as possible",
                                        style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.65f))
                                } else {
                                    Spacer(Modifier.height(4.dp))
                                    Text("No dues pending. Thank you!", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.65f))
                                }
                            }
                        }
                    }
                }
            }

            // Read-only badge
            item {
                Surface(color = CustomerLight) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.RemoveRedEye, null, tint = CustomerPrimary, modifier = Modifier.size(16.dp))
                        Text("Read-only view — your transaction history", style = MaterialTheme.typography.labelLarge, color = CustomerPrimary)
                    }
                }
            }

            // Interest breakdown (expandable)
            if (state.balance > 0) {
                item {
                    InterestBreakdown(principal = state.balance, interest = state.interest, accentColor = CustomerPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }

            // Transaction list header
            item {
                Text("Transaction History (${state.transactions.size})",
                    style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            if (state.transactions.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(vertical = 50.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions yet", color = MaterialTheme.colorScheme.onSurface.copy(0.45f))
                    }
                }
            }

            items(state.transactions, key = { it.id }) { tx ->
                TransactionItem(tx, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
