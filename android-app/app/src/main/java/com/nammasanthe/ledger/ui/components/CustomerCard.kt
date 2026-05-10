package com.nammasanthe.ledger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nammasanthe.ledger.data.db.entities.CustomerEntity
import com.nammasanthe.ledger.ui.theme.*
import com.nammasanthe.ledger.util.FormatUtil

@Composable
fun CustomerCard(
    customer: CustomerEntity,
    balance: Double,
    interest: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPaid   = balance <= 0.0
    val totalDue = if (isPaid) 0.0 else balance + interest
    val avatarBg = if (isPaid) VendorLight else CreditRedLight
    val avatarFg = if (isPaid) VendorPrimary else CreditRed

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(avatarBg)
            ) {
                Text(
                    text = customer.name.first().uppercase(),
                    color = avatarFg,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(customer.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "ID: ${customer.customerCode}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                if (!isPaid && interest > 0) {
                    Text(
                        "incl. ${FormatUtil.formatAmount(interest)} interest",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                }
            }

            // Balance
            Column(horizontalAlignment = Alignment.End) {
                if (isPaid) {
                    Text("Paid ✓", color = PaymentGreen, style = MaterialTheme.typography.titleMedium)
                } else {
                    Text(FormatUtil.formatAmount(totalDue), color = CreditRed, style = MaterialTheme.typography.titleMedium)
                    Text("total due", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
                }
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }
    }
}
