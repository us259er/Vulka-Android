package io.github.vulka.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Composable
fun DefaultScaffold(
    topBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    horizontalPadding: Boolean = true,
    composable: @Composable () -> Unit
) {
    Scaffold(
        topBar = { topBar() },
        floatingActionButton = { floatingActionButton() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = if (horizontalPadding) 16.dp else 0.dp)
        ) {
            composable()
        }
    }
}
