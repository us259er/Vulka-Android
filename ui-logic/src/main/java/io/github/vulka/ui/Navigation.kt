package io.github.vulka.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.common.DefaultScaffold
import io.github.vulka.ui.common.TopAppBarWithBack
import io.github.vulka.ui.screens.Welcome
import io.github.vulka.ui.screens.WelcomeScreen
import io.github.vulka.ui.screens.auth.Login
import io.github.vulka.ui.screens.auth.LoginScreen
import io.github.vulka.ui.screens.dashboard.Home
import io.github.vulka.ui.screens.dashboard.HomeScreen
import io.github.vulka.ui.utils.navtype.PlatformType
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VulkaNavigation(viewModel: VulkaViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val transmissionDurationMills = 400

    fun getStartDestination(): Any {
        val credentials = viewModel.credentialRepository.get()

        return if (credentials != null) {
            Home(
                platform = credentials.platform,
                userId = credentials.id.toString(),
                credentials = credentials.data
            )
        } else {
            Welcome
        }
    }

    NavHost(
        navController,
        startDestination = getStartDestination(),
        modifier = Modifier.imePadding(),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(transmissionDurationMills)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(transmissionDurationMills)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(transmissionDurationMills)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(transmissionDurationMills)
            )
        }
    ) {
        composable<Welcome> {
            WelcomeScreen(navController)
        }

        composable<Login>(
            typeMap = mapOf(typeOf<Platform>() to PlatformType)
        ) {
            val args = it.toRoute<Login>()

            DefaultScaffold(
                topBar = {
                    TopAppBarWithBack(
                        title = R.string.Login,
                        navController
                    )
                }
            ) {
                LoginScreen(args, navController)
            }
        }

        composable<Home>(
            typeMap = mapOf(typeOf<Platform>() to PlatformType)
        ) {
            val args = it.toRoute<Home>()

            DefaultScaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.Home)
                            )
                        }
                    )
                }
            ) {
                HomeScreen(args, navController)
            }
        }
    }
}
