package com.nammasanthe.ledger.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nammasanthe.ledger.util.ANNUAL_INTEREST_RATE
import com.nammasanthe.ledger.util.FormatUtil

/** Expandable card showing principal / interest / total breakdown. */
@Composable
fun InterestBreakdown(
    principal: Double,
    interest: Double,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    if (principal <= 0) return

    val total = principal + interest
    var expanded by remember { mutableStateOf(false) }
    val rateStr = "${(ANNUAL_INTEREST_RATE * 100).toInt()}%"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(accentColor.copy(alpha = 0.06f))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
    ) {
        Column {
            // Header row — always visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.Percent, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                    Text("Interest @ $rateStr p.a.", color = accentColor, style = MaterialTheme.typography.labelLarge)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("+${FormatUtil.formatAmount(interest)}", color = accentColor, style = MaterialTheme.typography.titleMedium)
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Expandable breakdown
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HorizontalDivider(color = accentColor.copy(alpha = 0.2f))
                    Spacer(Modifier.height(4.dp))
                    BreakdownRow("Principal (credit owed)", FormatUtil.formatAmount(principal),
                        valueColor = MaterialTheme.colorScheme.onSurface)
                    BreakdownRow("Interest ($rateStr p.a.)", "+${FormatUtil.formatAmount(interest)}", valueColor = accentColor)
                    HorizontalDivider(color = accentColor.copy(alpha = 0.15f))
                    BreakdownRow("Total Due", FormatUtil.formatAmount(total), valueColor = accentColor, bold = true)
                    Text(
                        "Interest accrues daily on outstanding balance from each credit date.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BreakdownRow(label: String, value: String, valueColor: Color, bold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = if (bold) MaterialTheme.typography.labelLarge else MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (bold) 0.9f else 0.6f))
        Text(value, style = if (bold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
            color = valueColor)
    }
}
