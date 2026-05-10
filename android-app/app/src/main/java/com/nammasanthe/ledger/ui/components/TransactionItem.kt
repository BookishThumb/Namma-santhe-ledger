package com.nammasanthe.ledger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nammasanthe.ledger.data.db.entities.TransactionEntity
import com.nammasanthe.ledger.data.db.entities.TransactionType
import com.nammasanthe.ledger.ui.theme.CreditRed
import com.nammasanthe.ledger.ui.theme.CreditRedLight
import com.nammasanthe.ledger.ui.theme.PaymentGreen
import com.nammasanthe.ledger.ui.theme.PaymentGreenLight
import com.nammasanthe.ledger.util.FormatUtil

@Composable
fun TransactionItem(
    transaction: TransactionEntity,
    customerName: String? = null,
    modifier: Modifier = Modifier
) {
    val isCredit = transaction.type == TransactionType.CREDIT
    val badgeColor  = if (isCredit) CreditRedLight  else PaymentGreenLight
    val amountColor = if (isCredit) CreditRed       else PaymentGreen
    val prefix      = if (isCredit) "+" else "−"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Badge
            Box(
                modifier = Modifier
                    .background(badgeColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isCredit) "Credit" else "Payment",
                    color = amountColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                customerName?.let {
                    Text(it, style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    text = FormatUtil.formatDate(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                )
                transaction.note?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }

            // Amount
            Text(
                text = "$prefix${FormatUtil.formatAmount(transaction.amount)}",
                color = amountColor,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
