package com.example.pizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pizza.ui.theme.PizzaTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember

// Additional import for Toast
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

// Additional import for Navigation to Menu
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Additional import for Navigation to Order Summary
import androidx.navigation.NavType
import androidx.navigation.navArgument

// import for AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

// import for Notification
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import android.os.Build

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PizzaTheme {
                // Set up navigation with Routes
                val navController = rememberNavController()

                // Request Notification Permission
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        // Permission granted! You can send notifications.
                        Toast.makeText(this@MainActivity, "Notification permission granted", Toast.LENGTH_SHORT).show()
                    } else {
                        // Permission denied. Explain why the feature won't work.
                        Toast.makeText(this@MainActivity, "Notification permission denied. Cannot send order status updates.", Toast.LENGTH_LONG).show()
                    }
                }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU is API 33
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                // Navigation
                NavHost(navController = navController, startDestination = Routes.PizzaOrder.route) {
                    composable(Routes.PizzaOrder.route) {
                        PizzaOrderScreen(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateToMenu = { navController.navigate(Routes.Menu.route) },
                            onNavigateToOrderSummary = { summary: String -> navController.navigate("${Routes.OrderSummary.route}?summary=$summary") }

                        )
                    }
                    composable(Routes.Menu.route) {
                        MenuScreen()
                    }
                    composable(
                        route = "${Routes.OrderSummary.route}?summary={summary}",
                        arguments = listOf(navArgument("summary") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val summary = backStackEntry.arguments?.getString("summary") ?: ""
                        OrderSummaryScreen(summary = summary)
                    }
            }
        }
    }
}

@Composable
fun PizzaOrderScreen(
    modifier: Modifier = Modifier,
    onNavigateToMenu: () -> Unit,
    onNavigateToOrderSummary: (String) -> Unit
) {
    var selectedPizzaType by remember { mutableStateOf("Margherita") }
    var selectedPizzaSize by remember { mutableStateOf("Medium") }
    var selectedToppings by remember { mutableStateOf(listOf<String>()) }
    var orderSummary by remember { mutableStateOf("") }

    // Get the current Context
    val context = LocalContext.current

    // State for dialog visibility
    var showCancelDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // App Title
        Text(
            text = "Pizza Order System",
            style = MaterialTheme.typography.headlineMedium.copy(color = androidx.compose.ui.graphics.Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        )

        // Pizza Image
        Image(
            painter = painterResource(id = R.drawable.pizza_pollo_arrost),
            contentDescription = "Pizza Image",
            modifier = Modifier.size(150.dp)
        )

        // Pizza Type Selection
        Text(
            text = "Select Pizza Type:",
            style = MaterialTheme.typography.bodyLarge
        )
        val pizzaTypes = listOf("Margherita", "Pepperoni", "Veggie")
        pizzaTypes.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (type == selectedPizzaType),
                    onClick = { selectedPizzaType = type }
                )
                Text(text = type)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Pizza Size Selection
        Text(
            text = "Select Pizza Size:",
            style = MaterialTheme.typography.bodyLarge
        )
        val pizzaSizes = listOf("Small", "Medium", "Large")
        pizzaSizes.forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (size == selectedPizzaSize),
                    onClick = { selectedPizzaSize = size }
                )
                Text(text = size)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toppings Selection
        Text(
            text = "Add Toppings:",
            style = MaterialTheme.typography.bodyLarge
        )
        val toppings = listOf("Cheese", "Mushrooms", "Olives")
        toppings.forEach { topping ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedToppings.contains(topping),
                    onCheckedChange = { isChecked ->
                        selectedToppings = if (isChecked) {
                            selectedToppings + topping
                        } else {
                            selectedToppings - topping
                        }
                    }
                )
                Text(text = topping)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row for Place Order, Menu, Order Summary Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Place Order Button with Toast
            Button(
                onClick = {
                    val toppingsString = if (selectedToppings.isEmpty()) "no toppings" else selectedToppings.joinToString(", ")
                    orderSummary = "You have ordered $selectedPizzaSize $selectedPizzaType with $toppingsString"
                    Toast.makeText(context, "Order has been placed successfully", Toast.LENGTH_SHORT).show()
//                    onNavigateToOrderSummary(orderSummary)
                }
            ) {
                Text(text = "Place Order")
            }

            // Menu Button
            Button(
                onClick = { onNavigateToMenu() }
            ) {
                Text(text = "Menu")
            }

            // Order Summary Button
            Button(
                onClick = { onNavigateToOrderSummary(orderSummary) }
            ) {
                Text(text = "Order Summary")
            }
        }

        // Cancel Order Button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            onClick = { showCancelDialog = true } // Show dialog when clicked
        ) {
            Text(text = "Cancel Order")
        }

        // AlertDialog for Cancel Confirmation
        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = { Text("Confirm Cancellation") },
                text = { Text("Are you sure you want to cancel your order?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showCancelDialog = false
                            orderSummary = "" // Clear the order summary
                            Toast.makeText(context, "Order has been cancelled", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showCancelDialog = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Order Summary
        Text(
            text = orderSummary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PizzaOrderScreenPreview() {
    PizzaTheme {
        PizzaOrderScreen(
            onNavigateToMenu = {},
            onNavigateToOrderSummary = {}
        )
    }
}}
