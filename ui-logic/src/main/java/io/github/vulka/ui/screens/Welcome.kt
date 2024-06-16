package io.github.vulka.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.drawablepainter.DrawablePainter
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val context = LocalContext.current

    // get app icon
    val icon = context.packageManager.getApplicationIcon(context.packageName)

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Vulka",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = DrawablePainter(icon),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )

            Text(
                text = "Witaj w Vulka",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 20.dp)
            )

            Text(
                text = "Nowoczesna aplikacja dla twojego e-dziennika",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            OutlinedButton(
                onClick = { navController.navigate(ChoosePlatform) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 90.dp)
                    .padding(top = 8.dp)
            ) {
                Text("Rozpocznij")
            }
        }
    }
}