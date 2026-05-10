package com.nammasanthe.ledger.ui.vendor.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammasanthe.ledger.ui.components.CustomerCard
import com.nammasanthe.ledger.ui.theme.VendorPrimary
import com.nammasanthe.ledger.util.FormatUtil

@Composable
fun VendorHomeScreen(
    onAddCustomer: () -> Unit,
    onAddTransaction: () -> Unit,
    onCustomerClick: (String) -> Unit,
    onDailySummary: () -> Unit,
    onLogout: () -> Unit,
    vm: VendorHomeViewModel = viewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf("") }

    val filtered = state.customers.filter { it.customer.name.contains(search, ignoreCase = true) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction, containerColor = VendorPrimary, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction", tint = Color.White, modifier = Modifier.size(30.dp))
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Header
            item {
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
                ) {
                    Surface(color = VendorPrimary) {
                        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Text(state.vendorName, style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
                                    Text("Total Dues Pending", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.65f))
                                }
                                Row {
                                    IconButton(onClick = onDailySummary) { Icon(Icons.Default.BarChart, null, tint = Color.White) }
                                    IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, null, tint = Color.White) }
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(FormatUtil.formatAmount(state.totalDues), style = MaterialTheme.typography.displayLarge, color = Color.White)
                            if (state.totalInterest > 0) {
                                Surface(color = Color.Black.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp)) {
                                    Text(
                                        "Includes ${FormatUtil.formatAmount(state.totalInterest)} interest @ 12% p.a.",
                                        style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.9f),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(14.dp))
                            // Search bar
                            Surface(color = Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
                                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Search, null, tint = Color.White.copy(0.75f), modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(10.dp))
                                    BasicTextField(
                                        value = search,
                                        onValueChange = { search = it },
                                        modifier = Modifier.weight(1f),
                                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                                        decorationBox = { inner ->
                                            if (search.isEmpty()) Text("Search customer...", color = Color.White.copy(0.55f), style = MaterialTheme.typography.bodyLarge)
                                            inner()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // List header
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Customers (${filtered.size})", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
                    OutlinedButton(onClick = onAddCustomer, shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                        Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(16.dp), tint = VendorPrimary)
                        Spacer(Modifier.width(6.dp))
                        Text("Add Customer", color = VendorPrimary, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            if (filtered.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp), contentAlignment = Alignment.Center) {
                        Text(if (search.isEmpty()) "No customers yet" else "No customers found",
                            color = MaterialTheme.colorScheme.onSurface.copy(0.45f),
                            style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            items(filtered, key = { it.customer.id }) { summary ->
                CustomerCard(
                    customer = summary.customer,
                    balance  = summary.balance,
                    interest = summary.interest,
                    onClick  = { onCustomerClick(summary.customer.id) },
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 10.dp)
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
