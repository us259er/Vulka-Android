package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.medzik.android.components.rememberMutable
import dev.medzik.android.components.rememberMutableString
import dev.medzik.android.components.ui.IconBox
import dev.medzik.android.utils.runOnIOThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Student
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.Avatar
import io.github.vulka.ui.screens.dashboard.more.LuckyNumber
import io.github.vulka.ui.sync.sync
import io.github.vulka.ui.utils.getInitials
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Start(
    val platform: Platform,
    val userId: String,
    val credentials: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    args: Start,
    navController: NavController,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var luckyNumber by rememberMutable(0)

    val student by rememberMutable(viewModel.credentialRepository.getById(UUID.fromString(args.userId))!!.student)

    // TODO: replace with SnackBar or something similar
    var errorText by rememberMutableString()

    fun updateUI() {
        luckyNumber = viewModel.luckyNumberRepository.getByCredentialsId(UUID.fromString(args.userId))?.number ?: 0
    }

    LaunchedEffect(Unit) {
        runOnIOThread {
            pullToRefreshState.startRefresh()

            updateUI()

            // Sync database
            try {
                sync(
                    platform = args.platform,
                    userId = args.userId,
                    credentials = args.credentials,
                    viewModel = viewModel
                )
                errorText = ""
            } catch (e: Exception) {
                errorText = e.message ?: ""
            }

            updateUI()

            pullToRefreshState.endRefresh()
        }
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            runOnIOThread {
                // Sync database
                try {
                    sync(
                        platform = args.platform,
                        userId = args.userId,
                        credentials = args.credentials,
                        viewModel = viewModel
                    )
                    errorText = ""
                } catch (e: Exception) {
                    errorText = e.message ?: ""
                }
                updateUI()
                pullToRefreshState.endRefresh()
            }
        }
    }

    Box(
        modifier = Modifier
            .nestedScroll(connection = pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            item {
                HeaderCard(student)
                Spacer(
                    modifier = Modifier.size(5.dp)
                )
            }

            item {
                Row {
                    LuckyCard(luckyNumber,navController)
                }
            }

            item {
                Text(text = errorText)
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(alignment = Alignment.TopCenter),
            state = pullToRefreshState,
        )
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
                Text(student.classId ?: "")
            }
        }
    }
}

@Composable
fun LuckyCard(luckyNumber: Int,navController: NavController) {
    Surface(
        modifier = Modifier.padding(3.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        onClick = {
            navController.navigate(LuckyNumber(luckyNumber))
        }
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