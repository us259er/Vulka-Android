package io.github.vulka.ui.screens.auth

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.medzik.android.components.TextFieldValue
import dev.medzik.android.components.rememberMutableBoolean
import dev.medzik.android.components.rememberMutableString
import dev.medzik.android.components.ui.LoadingButton
import dev.medzik.android.components.ui.textfield.AnimatedTextField
import dev.medzik.android.components.ui.textfield.PasswordAnimatedTextField
import dev.medzik.android.utils.runOnIOThread
import dev.medzik.android.utils.runOnUiThread
import io.github.vulka.core.api.LoginData
import io.github.vulka.core.api.Platform
import io.github.vulka.impl.librus.LibrusLoginClient
import io.github.vulka.impl.librus.LibrusLoginData
import io.github.vulka.impl.vulcan.VulcanLoginClient
import io.github.vulka.impl.vulcan.VulcanLoginData
import io.github.vulka.impl.vulcan.hebe.login.HebeKeystore
import io.github.vulka.ui.R
import io.github.vulka.ui.crypto.serializeCredentials
import kotlinx.serialization.Serializable

@Serializable
class Login(val platform: Platform)

@Composable
fun LoginScreen(
    args: Login,
    navController: NavController
) {
    val client = when (args.platform) {
        Platform.Vulcan -> VulcanLoginClient()
        Platform.Librus -> LibrusLoginClient()
    }

    val context = LocalContext.current

    var requestData: LoginData? by remember { mutableStateOf(null) }

    var loading by rememberMutableBoolean()

    val vulcanSymbol = rememberMutableString()
    val vulcanToken = rememberMutableString()
    val vulcanPin = rememberMutableString()

    val librusLogin = rememberMutableString()
    val librusPassword = rememberMutableString()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            when (args.platform) {
                Platform.Vulcan -> {
                    AnimatedTextField(
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = stringResource(R.string.Field_Symbol),
                        value = TextFieldValue.fromMutableState(vulcanSymbol),
                        clearButton = true,
                        singleLine = true,
                        leading = {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null
                            )
                        }
                    )

                    AnimatedTextField(
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = stringResource(R.string.Field_Token),
                        value = TextFieldValue.fromMutableState(vulcanToken),
                        clearButton = true,
                        singleLine = true,
                        leading = {
                            Icon(
                                imageVector = Icons.Default.DataObject,
                                contentDescription = null
                            )
                        }
                    )

                    AnimatedTextField(
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = stringResource(R.string.Field_Pin),
                        value = TextFieldValue.fromMutableState(vulcanPin),
                        clearButton = true,
                        singleLine = true,
                        leading = {
                            Icon(
                                imageVector = Icons.Default.Password,
                                contentDescription = null
                            )
                        }
                    )
                }

                Platform.Librus -> {
                    AnimatedTextField(
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = stringResource(R.string.Field_Login),
                        value = TextFieldValue.fromMutableState(librusLogin),
                        clearButton = true,
                        singleLine = true,
                        leading = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        }
                    )

                    PasswordAnimatedTextField(
                        modifier = Modifier.padding(vertical = 5.dp),
                        label = stringResource(R.string.Field_Password),
                        value = TextFieldValue.fromMutableState(librusPassword)
                    )
                }
            }
        }

        item {
            LoadingButton(
                onClick = {
                    runOnIOThread {
                        loading = true

                        when (args.platform) {
                            Platform.Vulcan -> {
                                // For Vulcan we must create keystore first
                                val keystore = HebeKeystore.create(
                                    context = context,
                                    alias = HebeKeystore.generateKeystoreName(vulcanSymbol.value),
                                    firebaseToken = "",
                                    deviceModel = "${Build.MANUFACTURER} ${Build.MODEL} (Vulka)")

                                requestData = VulcanLoginData(
                                    symbol = vulcanSymbol.value,
                                    token = vulcanToken.value,
                                    pin = vulcanPin.value,
                                    keystore = keystore
                                )
                            }

                            Platform.Librus -> {
                                requestData = LibrusLoginData(
                                    login = librusLogin.value,
                                    password = librusPassword.value
                                )
                            }
                        }

                        try {
                            // Credentials will be encrypted in ChooseStudents screen,
                            // because Vulcan implementation must encrypt credentials for every student,
                            // then can save it to Room database
                            // Currently encrypts only one credential
                            val response = client.login(requestData!!)

                            val data = serializeCredentials(response)

                            runOnUiThread {
                                navController.navigate(ChooseStudents(
                                    platform = args.platform,
                                    credentialsData = data
                                ))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        loading = false
                    }
                },
                loading = loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.Login))
            }
        }
    }
}
