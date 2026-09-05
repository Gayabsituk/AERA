package com.example.activity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.activity.data.ActivityCategory
import com.example.activity.data.ActivityEntry
import java.util.Locale

@Composable
fun ActivityListScreen(viewModel: ActivityViewModel, onAddActivity: () -> Unit) {
    val entries by viewModel.todayEntries.collectAsState()
    val totalFootprint = entries.sumOf { it.footprint }

    Scaffold(
        containerColor = Color(0xFFFDFBF7),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddActivity,
                containerColor = Color(0xFF2D5D45),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Activity")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Today's Activity",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Daily Summary Card
            Surface(
                color = Color(0xFFFBE9E7),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Today's Total", color = Color(0xFFD84315), style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = String.format(Locale.US, "%.2f kg CO₂e", totalFootprint),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D5D45)
                        )
                    }
                    Icon(
                        Icons.Outlined.Eco,
                        contentDescription = null,
                        tint = Color(0xFF2D5D45),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (entries.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No activities logged today.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(entries) { entry ->
                        ActivityItem(entry)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(entry: ActivityEntry) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (entry.category) {
                ActivityCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
                ActivityCategory.ENERGY -> Icons.Outlined.ElectricBolt
                ActivityCategory.FOOD -> Icons.Outlined.Fastfood
                ActivityCategory.SHOPPING -> Icons.Outlined.ShoppingBag
                ActivityCategory.WASTE -> Icons.Outlined.Delete
            }
            
            val iconBg = when (entry.category) {
                ActivityCategory.TRANSPORT -> Color(0xFFE8F5E9)
                ActivityCategory.ENERGY -> Color(0xFFFFF3E0)
                ActivityCategory.FOOD -> Color(0xFFF1F8E9)
                ActivityCategory.SHOPPING -> Color(0xFFE0F2F1)
                ActivityCategory.WASTE -> Color(0xFFF3E5F5)
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF2D5D45))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(entry.type, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    entry.category.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Text(
                String.format(Locale.US, "%.2f kg", entry.footprint),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
