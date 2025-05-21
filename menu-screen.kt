package com.example.pizza

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pizza.ui.theme.PizzaTheme
import androidx.compose.material3.ExperimentalMaterial3Api

// Data class for menu items
data class MenuItem(val name: String, val price: Double)

// MenuScreen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    // Sample menu items with prices
    val menuItems = listOf(
        MenuItem("Margherita", 10.99),
        MenuItem("Pepperoni", 12.99),
        MenuItem("Veggie", 11.99),
        MenuItem("Cheese Burst", 13.99)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pizza Menu") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menuItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "$${item.price}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    PizzaTheme {
        MenuScreen()
    }
}
