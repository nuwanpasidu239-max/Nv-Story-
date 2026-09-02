package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RatingBreakdown
import com.example.ui.theme.PlayGold
import com.example.ui.theme.PlayGreen

@Composable
fun RatingBreakdownView(
    rating: Float,
    reviewsCount: String,
    breakdown: RatingBreakdown,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Big Rating Number and Stars
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(110.dp)
        ) {
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    val isFilled = index < rating.toInt()
                    Icon(
                        imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = if (isFilled) PlayGold else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$reviewsCount reviews",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 5 Star Bars
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RatingBarRow(starLabel = "5", fraction = breakdown.fiveStar)
            RatingBarRow(starLabel = "4", fraction = breakdown.fourStar)
            RatingBarRow(starLabel = "3", fraction = breakdown.threeStar)
            RatingBarRow(starLabel = "2", fraction = breakdown.twoStar)
            RatingBarRow(starLabel = "1", fraction = breakdown.oneStar)
        }
    }
}

@Composable
private fun RatingBarRow(starLabel: String, fraction: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = starLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(12.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = PlayGreen,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
