package io.github.vulka.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.google.gson.Gson
import dev.medzik.android.components.rememberMutableBoolean
import dev.medzik.android.components.ui.LoadingButton
import dev.medzik.android.utils.runOnIOThread
import dev.medzik.android.utils.runOnUiThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Student
import io.github.vulka.database.Credentials
import io.github.vulka.impl.librus.LibrusLoginCredentials
import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.vulcan.VulcanLoginCredentials
import io.github.vulka.impl.vulcan.VulcanUserClient
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.crypto.serializeCredentialsAndEncrypt
import io.github.vulka.ui.screens.dashboard.Home
import kotlinx.serialization.Serializable

@Serializable
class ChooseStudents(
    val platform: Platform,
    val credentialsData: String
)

@Composable
fun ChooseStudentsScreen(
    args: ChooseStudents,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val credentials = when (args.platform) {
        Platform.Vulcan -> Gson().fromJson(args.credentialsData, VulcanLoginCredentials::class.java)
        Platform.Librus -> Gson().fromJson(args.credentialsData, LibrusLoginCredentials::class.java)
    }

    val client = when (args.platform) {
        Platform.Vulcan -> VulcanUserClient(credentials as VulcanLoginCredentials)
        Platform.Librus -> LibrusUserClient((credentials as LibrusLoginCredentials).cookies)
    }

    val students = remember { mutableStateListOf<Student>()}

    LaunchedEffect(Unit) {
        runOnIOThread {
            client.getStudents().forEach {
                students.add(it)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            students.forEach { student ->
                if (student.isParent)
                    Text(text = "${student.fullName} - ${student.parent!!.name} - Rodzic")
                else
                    Text(text = "${student.fullName} - Uczeń")
            }
        }

        var loading by rememberMutableBoolean()

        LoadingButton(
            onClick = {
                runOnIOThread {
                    loading = true

                    val encryptedCredentials = Credentials(
                        platform = args.platform,
                        data = serializeCredentialsAndEncrypt(credentials)
                    )

                    // TODO: add credentials for every students in Vulcan (Librus always had one student)
                    viewModel.credentialRepository.insert(encryptedCredentials)

                    runOnUiThread {
                        navController.navigate(
                            Home(
                                userId = encryptedCredentials.id.toString(),
                                credentials = encryptedCredentials.data,
                                platform = args.platform
                            )
                        ) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                                inclusive = true
                            }
                        }

                        loading = false
                    }
                }
            },
            loading = loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.Done))
        }
    }
}