package io.github.vulka.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.medzik.android.compose.ui.preference.BasicPreference
import io.github.vulka.core.api.Platform
import io.github.vulka.ui.R
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
