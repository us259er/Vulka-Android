package io.github.vulka.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.medzik.android.components.ui.PreferenceEntry
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.R
import io.github.vulka.ui.screens.auth.Login
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
                        text = stringResource(R.string.SelectJournal),
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
                title = stringResource(R.string.Vulcan),
                onClick = { navController.navigate(Login(Platform.Vulcan)) }
            )

            PreferenceEntry(
                icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null) },
                title = stringResource(R.string.Librus),
                onClick = { navController.navigate(Login(Platform.Librus)) }
            )
        }
    }
}
