package com.example.activity.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.activity.data.ActivityCategory
import com.example.activity.logic.FootprintCalculator
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogActivityScreen(viewModel: ActivityViewModel, onBack: () -> Unit) {
    var selectedCategory by remember { mutableStateOf(ActivityCategory.TRANSPORT) }
    var selectedType by remember { mutableStateOf("") }
    
    // Inputs
    var distance by remember { mutableStateOf("") }
    var trips by remember { mutableStateOf("1") }
    var passengers by remember { mutableStateOf("1") }
    var electricityUsed by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var itemsCount by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val typesMap = mapOf(
        ActivityCategory.TRANSPORT to listOf("Jeepney", "Motorcycle", "Tricycle", "Car", "Taxi", "Bus", "UV/Van"),
        ActivityCategory.ENERGY to listOf("Electricity"),
        ActivityCategory.FOOD to listOf("High-Impact Meat (Beef)", "Medium-Impact Meat (Pork)", "Low-Impact Meat (Chicken)", "Seafood", "Dairy & Eggs", "Grains & Staples", "Fruits & Vegetables", "Legumes & Plant-Based", "Processed & Packaged Foods", "Sweets & Desserts", "Beverages"),
        ActivityCategory.SHOPPING to listOf("Clothing & Footwear", "Electronics", "Furniture", "Household Products", "Personal Care", "Paper Products", "Plastic Products", "Other Retail Goods"),
        ActivityCategory.WASTE to listOf("Food Waste", "Paper & Cardboard", "Plastic", "Glass", "Metal", "Textiles", "Electronic Waste", "Mixed/General Waste")
    )

    val currentTypes = typesMap[selectedCategory] ?: emptyList()
    
    LaunchedEffect(selectedCategory) {
        selectedType = currentTypes.firstOrNull() ?: ""
    }

    val estimatedFootprint = remember(selectedCategory, selectedType, distance, trips, passengers, electricityUsed, amount, itemsCount, weight) {
        val value = when (selectedCategory) {
            ActivityCategory.TRANSPORT -> distance.toDoubleOrNull() ?: 0.0
            ActivityCategory.ENERGY -> electricityUsed.toDoubleOrNull() ?: 0.0
            ActivityCategory.FOOD -> amount.toDoubleOrNull() ?: 0.0
            ActivityCategory.SHOPPING -> itemsCount.toDoubleOrNull() ?: 0.0
            ActivityCategory.WASTE -> weight.toDoubleOrNull() ?: 0.0
        }
        FootprintCalculator.calculate(
            selectedCategory,
            selectedType,
            value,
            trips.toIntOrNull() ?: 1,
            passengers.toIntOrNull() ?: 1
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Log Activity", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFDFBF7)
                )
            )
        },
        containerColor = Color(0xFFFDFBF7)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Category", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActivityCategory.entries.forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFF2D5D45) else Color.White,
                        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                                color = if (isSelected) Color.White else Color.Black,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val typeLabel = when (selectedCategory) {
                ActivityCategory.TRANSPORT -> "Transport Type"
                ActivityCategory.ENERGY -> "Electricity"
                ActivityCategory.FOOD -> "Food Category"
                ActivityCategory.SHOPPING -> "Purchase Category"
                ActivityCategory.WASTE -> "Type of Waste"
            }
            Text(typeLabel, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (selectedCategory) {
                        ActivityCategory.TRANSPORT -> Icons.Outlined.DirectionsCar
                        ActivityCategory.ENERGY -> Icons.Outlined.ElectricBolt
                        ActivityCategory.FOOD -> Icons.Outlined.Fastfood
                        ActivityCategory.SHOPPING -> Icons.Outlined.ShoppingBag
                        ActivityCategory.WASTE -> Icons.Outlined.Delete
                    }
                    Icon(icon, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(selectedType, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    currentTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (selectedCategory == ActivityCategory.ENERGY) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Calculate your total daily kWh by adding the energy used by all your appliances. Multiply each appliance's wattage by its hours of use, divide by 1,000, then add the results.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dynamic Inputs
            when (selectedCategory) {
                ActivityCategory.TRANSPORT -> {
                    InputSection("Distance Driven", distance, { distance = it }, "km")
                    Spacer(modifier = Modifier.height(16.dp))
                    InputSection("Number of Trips", trips, { trips = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    InputSection("Number of Passengers", passengers, { passengers = it })
                }
                ActivityCategory.ENERGY -> {
                    InputSection("Electricity Used", electricityUsed, { electricityUsed = it }, "kWh")
                }
                ActivityCategory.FOOD -> {
                    InputSection("Serving/Amount", amount, { amount = it }, "kg")
                }
                ActivityCategory.SHOPPING -> {
                    InputSection("Number of Items", itemsCount, { itemsCount = it })
                }
                ActivityCategory.WASTE -> {
                    InputSection("Weight", weight, { weight = it }, "kg")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
            
            // Footprint Card
            Surface(
                color = Color(0xFFFBE9E7),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFFFCCBC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Estimated Footprint", color = Color(0xFFD84315), style = MaterialTheme.typography.labelMedium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = String.format(Locale.US, "%.1f kg CO₂e", estimatedFootprint),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Outlined.Eco,
                            contentDescription = null,
                            tint = Color(0xFF8D6E63),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val value = when (selectedCategory) {
                        ActivityCategory.TRANSPORT -> distance.toDoubleOrNull() ?: 0.0
                        ActivityCategory.ENERGY -> electricityUsed.toDoubleOrNull() ?: 0.0
                        ActivityCategory.FOOD -> amount.toDoubleOrNull() ?: 0.0
                        ActivityCategory.SHOPPING -> itemsCount.toDoubleOrNull() ?: 0.0
                        ActivityCategory.WASTE -> weight.toDoubleOrNull() ?: 0.0
                    }
                    viewModel.logActivity(
                        selectedCategory,
                        selectedType,
                        value,
                        trips.toIntOrNull() ?: 1,
                        passengers.toIntOrNull() ?: 1
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5D45)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Save Entry", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InputSection(label: String, value: String, onValueChange: (String) -> Unit, unit: String? = null) {
    Column {
        Text(label, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF2D5D45),
                unfocusedBorderColor = Color.LightGray
            ),
            trailingIcon = unit?.let {
                { Text(it, color = Color(0xFF2D5D45), fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp)) }
            }
        )
    }
}
