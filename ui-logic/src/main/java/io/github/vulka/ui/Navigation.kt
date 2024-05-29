package io.github.vulka.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.vulka.ui.common.DefaultScaffold
import io.github.vulka.ui.screens.Welcome
import io.github.vulka.ui.screens.WelcomeScreen

@Composable
fun VulkaNavigation() {
    val navController = rememberNavController()

    val transmissionDurationMills = 400

    NavHost(
        navController,
        startDestination = Welcome,
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
            DefaultScaffold {
                WelcomeScreen(navController)
            }
        }
    }
}
