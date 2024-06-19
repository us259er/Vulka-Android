package io.github.vulka.ui.common

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.medzik.android.compose.icons.TopAppBarBackIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumTopAppBarWithBack(@StringRes title: Int, navController: NavController) {
    MediumTopAppBar(
        title = {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = { TopAppBarBackIcon(navController) }
    )
}
