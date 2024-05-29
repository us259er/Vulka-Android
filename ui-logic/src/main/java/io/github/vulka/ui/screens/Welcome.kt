package io.github.vulka.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.medzik.android.components.PreferenceEntry
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wybierz swój dziennik",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PreferenceEntry(
                icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null) },
                title = "UONET+ Vulcan",
                onClick = {  }
            )

            PreferenceEntry(
                icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null) },
                title = "Librus Synergia",
                onClick = {  }
            )
        }
    }
}
