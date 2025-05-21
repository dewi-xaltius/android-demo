package com.example.pizza

sealed class Routes(val route: String) {
    object PizzaOrder : Routes("pizza_order")
    object Menu : Routes("menu")
}
