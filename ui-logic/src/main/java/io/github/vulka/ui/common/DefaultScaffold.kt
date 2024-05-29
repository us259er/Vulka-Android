package io.github.vulka.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
