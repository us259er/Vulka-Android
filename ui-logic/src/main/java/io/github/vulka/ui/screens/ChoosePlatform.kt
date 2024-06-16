package io.github.vulka.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.medzik.android.components.ui.preference.BasicPreference
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.R
import io.github.vulka.ui.common.MediumTopAppBarWithBack
import io.github.vulka.ui.screens.auth.Login
import kotlinx.serialization.Serializable

@Serializable
object ChoosePlatform

@Composable
fun ChoosePlatform(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BasicPreference(
            leading = { Icon(painterResource(R.drawable.vulcan_logo), contentDescription = null) },
            title = stringResource(R.string.Vulcan),
            onClick = { navController.navigate(Login(Platform.Vulcan)) }
        )

        BasicPreference(
            leading = { Icon(painterResource(R.drawable.librus_logo), contentDescription = null) },
            title = stringResource(R.string.Librus),
            onClick = { navController.navigate(Login(Platform.Librus)) }
        )
    }
}
