package io.github.vulka.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.medzik.android.components.PreferenceEntry
import dev.medzik.android.components.rememberMutable
import dev.medzik.android.components.rememberMutableString
import dev.medzik.android.utils.runOnIOThread
import io.github.vulka.core.api.Platform
import io.github.vulka.impl.vulcan.VulcanApi
import io.github.vulka.impl.vulcan.VulcanLoginClient
import io.github.vulka.impl.vulcan.VulcanLoginData
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.TextInputField
import kotlinx.serialization.Serializable

@Serializable
class Login(val platform: Platform)

@Composable
fun LoginScreen(
    args: Login,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (args.platform) {
            Platform.Vulcan -> {

                var symbol by rememberMutableString()
                var token by rememberMutableString()
                var pin by rememberMutableString()

                TextInputField(
                    label = stringResource(R.string.Symbol),
                    value = symbol,
                    onValueChange = { symbol = it },
                )

                TextInputField(
                    label = stringResource(R.string.Token),
                    value = token,
                    onValueChange = { token = it },
                )

                TextInputField(
                    label = stringResource(R.string.Pin),
                    value = pin,
                    onValueChange = { pin = it },
                )
//           try {
//                val keystore = HebeKeystore.create(context,"key2","","Vulca ALPHA")
//                val loginData = VulcanLoginData(
//                    symbol = "powiatstrzelecki",
//                    token = "3S1FANN",
//                    pin = "149167",
//                    keystore = keystore
//                )
//                val loginClient = VulcanLoginClient()
//                val session = loginClient.login(loginData)
//
//                x = session.account!!.userLogin
//            } catch (e: Exception) {
//                x = "Error " + e.message
//                e.printStackTrace()
//            }
            }

            Platform.Librus -> TODO()
        }
    }
}
