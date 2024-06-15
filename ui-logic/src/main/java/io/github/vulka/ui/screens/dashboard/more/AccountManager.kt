package io.github.vulka.ui.screens.dashboard.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.vulka.ui.R
import io.github.vulka.ui.screens.Welcome
import io.github.vulka.ui.screens.auth.Login
import kotlinx.serialization.Serializable

@Serializable
class AccountManager

@Composable
fun AccountManagerScreen(
    args: AccountManager,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
        }

        item {
            Button(
                onClick = {
                    navController.navigate(Welcome)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.AddAccount))
            }
        }
    }
}