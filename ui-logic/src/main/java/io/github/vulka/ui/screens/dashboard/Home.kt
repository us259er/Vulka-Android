package io.github.vulka.ui.screens.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import dev.medzik.android.utils.runOnIOThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.impl.librus.LibrusLoginClient
import io.github.vulka.impl.librus.LibrusLoginData
import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.vulcan.VulcanUserClient
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.crypto.decryptCredentials
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
class Home(
    val platform: Platform,
    val userId: String,
    val credentials: String
)

@Composable
fun HomeScreen(
    args: Home,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val credentials = decryptCredentials(args.credentials)

    val client = when (args.platform) {
        Platform.Vulcan -> VulcanUserClient()
        Platform.Librus -> {
            val loginData = Gson().fromJson(credentials, LibrusLoginData::class.java)

            // TODO: do not block main-thread
            runBlocking {
                val loginResponse = LibrusLoginClient().login(loginData)
                LibrusUserClient(loginResponse.cookies)
            }
        }
    }

    when (args.platform) {
        Platform.Librus -> {
            var accountInfo: AccountInfo? by remember { mutableStateOf(null) }

            LaunchedEffect(Unit) {
                runOnIOThread {
                    accountInfo = client.getAccountInfo()
                }
            }

            if (accountInfo != null) {
                Text(accountInfo!!.fullName)
                Text(accountInfo!!.className)
            }
        }

        Platform.Vulcan -> {
            Text(text = args.credentials)
        }
    }
}
