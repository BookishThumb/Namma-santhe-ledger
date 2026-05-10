package com.nammasanthe.ledger.ui.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nammasanthe.ledger.ui.theme.CustomerPrimary
import com.nammasanthe.ledger.ui.theme.VendorPrimary

@Composable
fun WelcomeScreen(
    onVendorClick: () -> Unit,
    onCustomerClick: () -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            // Branding
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(96.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("📒", style = MaterialTheme.typography.displayLarge)
                    }
                }
                Text("Namma Santhe", style = MaterialTheme.typography.headlineLarge,
                    color = VendorPrimary)
                Text("Udari Ledger", style = MaterialTheme.typography.titleLarge,
                    color = VendorPrimary.copy(alpha = 0.7f))
                Text("Simple credit tracking for your shop",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center)
            }

            Spacer(Modifier.weight(1f))

            // Role selection
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Who are you today?",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                RoleButton(
                    emoji = "🛒",
                    title = "I am a Vendor",
                    subtitle = "Lender — Shop / Stall Owner",
                    color = VendorPrimary,
                    onClick = onVendorClick
                )
                RoleButton(
                    emoji = "👤",
                    title = "I am a Customer",
                    subtitle = "Borrower — View my balance",
                    color = CustomerPrimary,
                    onClick = onCustomerClick
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RoleButton(emoji: String, title: String, subtitle: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(emoji, style = MaterialTheme.typography.headlineMedium)
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}
