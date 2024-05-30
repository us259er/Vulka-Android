package io.github.vulka.ui.screens.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.medzik.android.utils.runOnIOThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.ui.VulkaViewModel
import io.ktor.http.*
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
    val client = when (args.platform) {
        Platform.Vulcan -> TODO()
        Platform.Librus -> LibrusUserClient(
            Gson().fromJson(args.credentials, object : TypeToken<List<Cookie>>() {}.type)
        )
    }

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
