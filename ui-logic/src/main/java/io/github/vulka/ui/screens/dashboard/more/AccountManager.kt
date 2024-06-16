package io.github.vulka.ui.screens.dashboard.more

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import dev.medzik.android.components.rememberMutableBoolean
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Student
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.Avatar
import io.github.vulka.ui.screens.Welcome
import io.github.vulka.ui.screens.dashboard.Home
import io.github.vulka.ui.utils.getInitials
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class AccountManager(
    val platform: Platform,
    val userId: String,
)

@Composable
fun AccountManagerScreen(
    args: AccountManager,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val credentials = remember { mutableStateListOf(*viewModel.credentialRepository.getAll().toTypedArray()) }

    var selfDelete by rememberMutableBoolean()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            credentials.forEach { credential ->
                StudentCard(
                    student = credential.student,
                    options = {
                        IconButton(
                            onClick = {
                                // TODO: Clear all account data from database
                                viewModel.credentialRepository.delete(credential)
                                selfDelete = UUID.fromString(args.userId) == credential.id
                                credentials.remove(credential)

                                check(viewModel,navController,false)
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
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

    BackHandler {
        check(viewModel,navController,selfDelete)
    }
}

fun check(
    viewModel: VulkaViewModel,
    navController: NavController,
    selfDelete: Boolean,
) {
    if (viewModel.credentialRepository.count() == 0) {
        navController.navigate(Welcome) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
                inclusive = true
            }
        }
        return
    }

    // Navigate to first credential if current selected was deleted
    if (selfDelete) {
        val firstCredential = viewModel.credentialRepository.get()!!

        navController.navigate(Home(
            userId = firstCredential.id.toString(),
            credentials = firstCredential.data,
            platform = firstCredential.platform
        )) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
                inclusive = true
            }
        }
    }
}

@Composable
fun StudentCard(
    student: Student,
    options: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .padding(10.dp),
    ) {
        Avatar(
            text = student.getInitials()
        )

        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (student.isParent) {
                Text(student.parent!!.name)
                Text(
                    text = "${student.fullName} - ${stringResource(R.string.Parent)}",
                    fontSize = 12.sp
                )
            } else {
                Text(student.fullName)
                Text(
                    text = stringResource(R.string.Student),
                    fontSize = 12.sp
                )
            }
        }
        options()

    }
}