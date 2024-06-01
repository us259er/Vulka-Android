package io.github.vulka.ui.screens.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import dev.medzik.android.utils.runOnIOThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.response.AccountInfo
import io.github.vulka.database.Credentials
import io.github.vulka.impl.librus.LibrusLoginClient
import io.github.vulka.impl.librus.LibrusLoginData
import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.vulcan.VulcanLoginResponse
import io.github.vulka.impl.vulcan.VulcanUserClient
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
class Start(val platform: Platform, val credentials: String)

@Composable
fun StartScreen(args: Start) {

    val credentials = args.credentials

    val client = when (args.platform) {
        Platform.Vulcan -> {
            val loginData = Gson().fromJson(credentials, VulcanLoginResponse::class.java)
            VulcanUserClient(loginData)
        }
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
            Text(text = credentials)
        }
    }
}