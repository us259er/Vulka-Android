package io.github.vulka.ui.screens.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.VulkaViewModel
import kotlinx.serialization.Serializable

@Serializable
class Login(val platform: Platform)

@Composable
fun LoginScreen(
    args: Login,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    Text("Platform ${args.platform}")
}
