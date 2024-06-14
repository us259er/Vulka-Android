package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.medzik.android.components.rememberMutable
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.crypto.decryptCredentials
import io.github.vulka.ui.utils.navtype.PlatformType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
class Home(
    val platform: Platform,
    val userId: String,
    val credentials: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    args: Home,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {

    val bottomNavController = rememberNavController()
    val credentials = decryptCredentials(args.credentials)

    // TODO: make something better (Not string based)
    var bottomSelected by rememberMutable("start")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (bottomSelected) {
                            "start" -> stringResource(R.string.Home)
                            "grades" -> stringResource(R.string.Grades)
                            "timetable" -> stringResource(R.string.Timetable)
                            else -> ""
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = { Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = null
                    ) },
                    label = { Text(stringResource(R.string.Home)) },
                    selected = bottomSelected == "start" ,
                    onClick = {
                        bottomSelected = "start"
                        bottomNavController.navigate(Start(
                            platform = args.platform,
                            userId = args.userId,
                            credentials
                        ))
                    }
                )
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = { Icon(
                        imageVector = Icons.Default.Looks6,
                        contentDescription = null
                    ) },
                    label = { Text(stringResource(R.string.Grades)) },
                    selected = bottomSelected == "grades",
                    onClick = {
                        bottomSelected = "grades"
                        bottomNavController.navigate(Grades)
                    }
                )
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = { Icon(
                        imageVector = Icons.Default.Backpack,
                        contentDescription = null
                    ) },
                    label = { Text(stringResource(R.string.Timetable)) },
                    selected = bottomSelected == "timetable",
                    onClick = {
                        bottomSelected = "timetable"
                        bottomNavController.navigate(Timetable)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            bottomNavController,
            startDestination = Start(
                platform = args.platform,
                userId = args.userId,
                credentials
            ),
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Start>(
                typeMap = mapOf(typeOf<Platform>() to PlatformType)
            ) {
                val arg = it.toRoute<Start>()
                StartScreen(arg)
            }
            composable<Grades> {
                GradesScreen()
            }
            composable<Timetable> {
                TimetableScreen()
            }
        }
    }
}
