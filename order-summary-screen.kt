package com.example.pizza

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pizza.ui.theme.PizzaTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment

// OrderSummaryScreen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(summary: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderSummaryScreenPreview() {
    PizzaTheme {
        OrderSummaryScreen(summary = "You have ordered Medium Margherita with Cheese, Mushrooms")
    }
}
