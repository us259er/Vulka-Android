package io.github.vulka.ui.screens.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.github.vulka.core.api.Platform
import kotlinx.serialization.Serializable

@Serializable
class Login(val platform: Platform)

@Composable
fun LoginScreen(args: Login, navController: NavController) {
    Text("Platform ${args.platform}")
}
