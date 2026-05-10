package com.nammasanthe.ledger.ui.vendor.dailysummary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.nammasanthe.ledger.ui.components.TransactionItem
import com.nammasanthe.ledger.ui.theme.CreditRed
import com.nammasanthe.ledger.ui.theme.CreditRedLight
import com.nammasanthe.ledger.ui.theme.PaymentGreen
import com.nammasanthe.ledger.ui.theme.PaymentGreenLight
import com.nammasanthe.ledger.ui.theme.VendorLight
import com.nammasanthe.ledger.ui.theme.VendorPrimary
import com.nammasanthe.ledger.util.FormatUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySummaryScreen(onBack: () -> Unit, vm: DailySummaryViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val net = state.totalPayment - state.totalCredit

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Summary") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VendorPrimary, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Summary cards
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard("Credit Given", state.totalCredit, CreditRed, CreditRedLight, Modifier.weight(1f))
                    SummaryCard("Payment Received", state.totalPayment, PaymentGreen, PaymentGreenLight, Modifier.weight(1f))
                }
            }

            // Net card
            item {
                Surface(
                    color = if (net >= 0) VendorLight else CreditRedLight,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Net for Today", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${if (net >= 0) "+" else ""}${FormatUtil.formatAmount(net)}",
                            style = MaterialTheme.typography.displayLarge,
                            color = if (net >= 0) PaymentGreen else CreditRed
                        )
                        Text(
                            if (net >= 0) "More collected than given" else "More credit given than collected",
                            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )
                    }
                }
            }

            // Transactions
            item {
                Text("Today's Transactions (${state.todayTransactions.size})",
                    style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface.copy(0.55f))
            }
            if (state.todayTransactions.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions today", color = MaterialTheme.colorScheme.onSurface.copy(0.45f))
                    }
                }
            }
            items(state.todayTransactions, key = { it.id }) { tx ->
                TransactionItem(tx, customerName = vm.customerName(tx.customerId))
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, amount: Double, textColor: Color, bgColor: Color, modifier: Modifier) {
    Surface(color = bgColor, shape = RoundedCornerShape(16.dp), modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = textColor)
            Text(FormatUtil.formatAmount(amount), style = MaterialTheme.typography.headlineMedium, color = textColor)
        }
    }
}
