package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import dev.medzik.android.components.rememberMutable
import dev.medzik.android.components.rememberMutableBoolean
import dev.medzik.android.components.ui.IconBox
import dev.medzik.android.utils.runOnIOThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Student
import io.github.vulka.impl.librus.LibrusLoginCredentials
import io.github.vulka.impl.librus.LibrusUserClient
import io.github.vulka.impl.vulcan.Utils
import io.github.vulka.impl.vulcan.VulcanLoginCredentials
import io.github.vulka.impl.vulcan.VulcanUserClient
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.Avatar
import io.github.vulka.ui.utils.getInitials
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.UUID

@Serializable
class Start(
    val platform: Platform,
    val userId: String,
    val credentials: String
)

@Composable
fun StartScreen(
    args: Start,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val credentials = args.credentials

    var luckyNumber by rememberMutable(0)

    val client by rememberMutable(when (args.platform) {
        Platform.Vulcan -> {
            val loginData = Gson().fromJson(credentials, VulcanLoginCredentials::class.java)
            VulcanUserClient(loginData)
        }
        Platform.Librus -> {
            val loginData = Gson().fromJson(credentials, LibrusLoginCredentials::class.java)
            LibrusUserClient(loginData)
        }
    })

    var loaded by rememberMutableBoolean(false)
    var student: Student? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        runOnIOThread {
            // Renew librus credentials
            // TODO: not refresh every time
            if (args.platform == Platform.Librus)
                (client as LibrusUserClient).renewCredentials()

            student = viewModel.credentialRepository.getById(UUID.fromString(args.userId))!!.student

            luckyNumber = client.getLuckyNumber(student!!,Date())

            loaded = true
        }
    }

    if (loaded) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            HeaderCard(student!!)
            Spacer(
                modifier = Modifier.size(5.dp)
            )
            Row {
                LuckyCard(luckyNumber)
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun HeaderCard(student: Student) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(3.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(10.dp),
        ) {
            Avatar(text = student.getInitials())
            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    fontSize = 18.sp,
                    text = student.fullName
                )
                Text(student.classId)
            }
        }
    }
}

@Composable
fun LuckyCard(luckyNumber: Int) {
    Surface(
        modifier = Modifier.padding(3.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 48.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBox(Icons.Default.Star)

            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 18.sp,
                text = "${if (luckyNumber != 0) luckyNumber else stringResource(R.string.None)}"
            )
        }
    }
}