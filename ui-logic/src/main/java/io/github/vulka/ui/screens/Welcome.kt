package io.github.vulka.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.github.vulka.impl.vulcan.VulcanLoginClient
import io.github.vulka.impl.vulcan.VulcanLoginData
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Composable
fun WelcomeScreen(navController: NavController) {
    val response = VulcanLoginClient().login(
        VulcanLoginData(
            symbol = "test",
            token = "test",
            pin = "test"
        )
    )

    Text(response.symbol)
}
