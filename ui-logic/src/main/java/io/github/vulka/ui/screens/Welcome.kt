package io.github.vulka.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Composable
fun WelcomeScreen(navController: NavController) {
    Text("Hello!")
}
