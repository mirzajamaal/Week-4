package com.example.quickbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickbite.ui.theme.QuickBiteTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    // Define other text styles as needed
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase

        setContent {
            QuickBiteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    QuickBiteApp()
                }
            }
        }
    }
}

@Composable
fun QuickBiteApp(cartViewModel: CartViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController, cartViewModel)
        }
        composable("menu/{restaurantId}") { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")?.toInt() ?: 0
            MenuScreen(restaurantId, navController, cartViewModel)
        }
        composable("cart") {
            CartScreen(navController, cartViewModel)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("order_history") {
            OrderHistoryScreen(navController)
        }
        composable("checkout") {
            CheckoutScreen(navController)
        }
        composable("cash_payment") {
            CashPaymentScreen(navController)
        }
        composable("card_payment") {
            CardPaymentScreen(navController)
        }
        composable("success") {
            SuccessScreen(navController)
        }
    }
}

class SplashViewModel : ViewModel() {
    fun startSplashScreen(onTimeout: () -> Unit) {
        viewModelScope.launch {
            delay(2000L)
            onTimeout()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.startSplashScreen {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Login Button
        Button(
            onClick = {
                // Call onLoginSuccess() to navigate to the home screen
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Login", style = MaterialTheme.typography.button)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        TextButton(onClick = {
            // Navigate to sign up screen
            navController.navigate("signup") {
                popUpTo("login") { inclusive = true }
            }
        }) {
            Text("Don't have an account? Sign Up")
        }
    }
}

@Composable
fun SignupScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text(
            text = "QuickBite",
            style = MaterialTheme.typography.h3.copy(
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm Password Icon")
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Signup Button
        Button(
            onClick = {
                // Signup logic here
                navController.navigate("home") {
                    popUpTo("signup") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Sign Up", style = MaterialTheme.typography.button)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to Login Text
        TextButton(onClick = {
            // Navigate back to the login screen
            navController.navigate("login") {
                popUpTo("signup") { inclusive = true }
            }
        }) {
            Text("Already have an account? Log In")
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val allRestaurants = listOf(
        Restaurant(1, "Pizza Place", "123 Main St", "Pizza"),
        Restaurant(2, "Burger Joint", "456 Elm St", "Burgers"),
        Restaurant(3, "Sushi Spot", "789 Oak St", "Sushi"),
        Restaurant(4, "Taco Haven", "101 Pine St", "Mexican"),
        Restaurant(5, "Steakhouse Elite", "202 Maple St", "Steakhouse"),

    )


    // Filter restaurants based on search query
    val filteredRestaurants = allRestaurants.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.cuisine.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for restaurants or dishes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
        )

        // Restaurant Listings
        LazyColumn {
            items(filteredRestaurants) { restaurant ->
                RestaurantItem(restaurant) {
                    navController.navigate("menu/${restaurant.id}")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {
                navController.navigate("cart")
            }) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart Icon")
            }
            IconButton(onClick = {
                navController.navigate("profile")
            }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Icon")
            }
            IconButton(onClick = {
                navController.navigate("order_history")
            }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Order History Icon")
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = restaurant.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.address, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = restaurant.cuisine, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun MenuScreen(restaurantId: Int, navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    // Sample menu items for demonstration
    val menuItems = listOf(
        // Classic Burgers
        MenuItem(1, "Classic Cheeseburger", "Juicy beef patty, melted cheddar cheese, lettuce, tomato, pickles, and special sauce.", 7.99),
        MenuItem(2, "Bacon BBQ Burger", "Beef patty with crispy bacon, BBQ sauce, cheddar cheese, lettuce, and onion rings.", 9.49),
        MenuItem(3, "Mushroom Swiss Burger", "Beef patty topped with sautéed mushrooms, Swiss cheese, and garlic aioli.", 8.99),

        // Gourmet Burgers
        MenuItem(4, "Truffle Burger", "Beef patty with truffle aioli, sautéed mushrooms, Swiss cheese, and arugula.", 12.99),
        MenuItem(5, "Lobster Roll", "Fresh lobster meat mixed with lemon herb mayo, served on a toasted bun with lettuce.", 15.99),

        // Pizzas
        MenuItem(6, "Margherita Pizza", "Classic pizza with tomato sauce, fresh mozzarella, basil leaves, and a drizzle of olive oil.", 9.99),
        MenuItem(7, "Pepperoni Pizza", "Pepperoni slices, mozzarella cheese, and tomato sauce on a hand-tossed crust.", 10.49),
        MenuItem(8, "Vegetarian Supreme Pizza", "Loaded with bell peppers, mushrooms, olives, onions, and spinach on a tomato base.", 11.49),
        MenuItem(9, "Prosciutto and Arugula Pizza", "Thinly sliced prosciutto, fresh arugula, shaved Parmesan, and a balsamic glaze.", 12.99),

        // Pastas
        MenuItem(10, "Spaghetti Carbonara", "Classic Italian pasta with pancetta, eggs, Parmesan cheese, and black pepper.", 11.99),
        MenuItem(11, "Fettuccine Alfredo", "Fettuccine pasta in a rich and creamy Alfredo sauce made with butter, cream, and Parmesan.", 12.49),
        MenuItem(12, "Penne Arrabbiata", "Penne pasta tossed in a spicy tomato sauce with garlic, red chili flakes, and parsley.", 10.99),
        MenuItem(13, "Lasagna Bolognese", "Layers of pasta with rich Bolognese meat sauce, béchamel sauce, and melted mozzarella.", 13.49),

        // Appetizers
        MenuItem(14, "Truffle Fries", "Crispy fries tossed with truffle oil, Parmesan cheese, and fresh herbs.", 6.99),
        MenuItem(15, "Chicken Wings", "Spicy buffalo wings served with celery sticks and ranch dipping sauce.", 8.49),
        MenuItem(16, "Caprese Salad", "Fresh mozzarella, tomatoes, and basil drizzled with balsamic reduction and olive oil.", 7.99),

        // Desserts
        MenuItem(17, "Chocolate Lava Cake", "Warm chocolate cake with a gooey molten center, served with vanilla ice cream.", 6.99),
        MenuItem(18, "Tiramisu", "Classic Italian dessert with coffee-soaked ladyfingers, mascarpone cheese, and cocoa powder.", 7.49),
        MenuItem(19, "New York Cheesecake", "Rich and creamy cheesecake with a graham cracker crust and a hint of vanilla.", 6.49),

        // Beverages
        MenuItem(20, "Craft Beer", "Locally brewed craft beer with a rich and diverse flavor profile.", 5.99),
        MenuItem(21, "Fresh Lemonade", "Refreshing lemonade made with freshly squeezed lemons and a touch of mint.", 4.99),
        MenuItem(22, "Cold Brew Coffee", "Smooth and strong cold brew coffee served over ice.", 3.99)
    )

    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Menu for Restaurant $restaurantId", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        // Menu Items
        LazyColumn {
            items(menuItems) { menuItem ->
                MenuItemCard(menuItem) {
                    selectedMenuItem = menuItem
                    showDialog = true
                }
            }
        }

        // Cart Button
        Button(
            onClick = {
                navController.navigate("cart")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("View Cart", style = MaterialTheme.typography.button)
        }
    }

    if (showDialog && selectedMenuItem != null) {
        AddToCartDialog(
            menuItem = selectedMenuItem!!,
            onDismiss = { showDialog = false },
            cartViewModel = cartViewModel
        )
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = menuItem.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = menuItem.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$${menuItem.price}", style = MaterialTheme.typography.body2)
        }
    }
}


@Composable
fun CartItemCard(cartItem: CartItem, function: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = cartItem.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = cartItem.description, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Quantity: ${cartItem.quantity}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Customization: ${cartItem.customization}", style = MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$${cartItem.price * cartItem.quantity}", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
fun AddToCartDialog(
    menuItem: MenuItem,
    onDismiss: () -> Unit,
    cartViewModel: CartViewModel
) {
    var quantity by remember { mutableStateOf(1) }
    var customization by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add ${menuItem.name} to Cart") },
        text = {
            Column {
                Text("Price: $${menuItem.price}")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = customization,
                    onValueChange = { customization = it },
                    label = { Text("Customization") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                cartViewModel.addToCart(
                    menuItem = menuItem,
                    quantity = quantity,
                    customization = customization
                )
                onDismiss()
            }) {
                Text("Add to Cart")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    // Collect cartItems from the ViewModel as State
    val cartItems by cartViewModel.cartItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Cart", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(cartItems) { cartItem ->
                CartItemCard(cartItem) {
                    cartViewModel.removeFromCart(cartItem)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate("checkout")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Proceed to Checkout", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun CheckoutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Choose Payment Method", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("cash_payment")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Pay with Cash", style = MaterialTheme.typography.button)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("card_payment")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Pay with Card", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun CashPaymentScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pay with Cash", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("success")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Confirm Payment", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun CardPaymentScreen(navController: NavController) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pay with Card", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Expiry Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("success")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Confirm Payment", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun SuccessScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Order Successful!", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Go to Home", style = MaterialTheme.typography.button)
        }
    }
}

data class MenuItem(val id: Int, val name: String, val description: String, val price: Double)
data class CartItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    var quantity: Int,
    var customization: String
)

class CartViewModel : ViewModel() {
    // Use StateFlow for observing changes to the cart items
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    fun addToCart(menuItem: MenuItem, quantity: Int, customization: String) {
        _cartItems.value = _cartItems.value.toMutableList().apply {
            val existingItem = find { it.id == menuItem.id }
            if (existingItem != null) {
                existingItem.quantity += quantity
                existingItem.customization = customization
            } else {
                add(
                    CartItem(
                        id = menuItem.id,
                        name = menuItem.name,
                        description = menuItem.description,
                        price = menuItem.price,
                        quantity = quantity,
                        customization = customization
                    )
                )
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.toMutableList().apply {
            remove(cartItem)
        }
    }
}


@Composable
fun ProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("John Doe") }
    var address by remember { mutableStateOf("123 Main St") }
    var profilePicture by remember { mutableStateOf("https://example.com/profile.jpg") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your Profile", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Profile details and edit functionality go here
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Name Icon")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Address Icon")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Profile picture change logic goes here

        Button(
            onClick = {
                // Save profile changes
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Save Changes", style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun OrderHistoryScreen(navController: NavController) {
    // Placeholder for OrderHistoryScreen implementation
    // Fetch and display user's past orders
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Order History", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        // Order history items go here
    }
}

data class Restaurant(val id: Int, val name: String, val address: String, val cuisine: String)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QuickBiteTheme {
        QuickBiteApp()
    }
}
