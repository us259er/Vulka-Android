package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.medzik.android.components.rememberMutableBoolean
import dev.medzik.android.components.ui.ExpandedIfNotEmpty
import dev.medzik.android.components.ui.IconBox
import dev.medzik.android.utils.runOnUiThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Grade
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.Avatar
import io.github.vulka.ui.common.AvatarShape
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Grades(
    val platform: Platform,
    val userId: String,
    val credentials: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(
    args: Grades,
    pullRefresh: @Composable BoxScope.() -> Unit = {},
    pullToRefreshState: PullToRefreshState,
    refreshed: Boolean
) {
    val pagerState = rememberPagerState { 2 }
    val tabs = listOf(
        stringResource(R.string.Grades),
        stringResource(R.string.Summary),
    )

    Box {
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = @Composable { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.PrimaryIndicator(
                            width = 80.dp,
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            runOnUiThread {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        GradesTab(
                            pullToRefreshState = pullToRefreshState,
                            args = args,
                            refreshed = refreshed
                        )
                    }

                    1 -> SummaryTab()
                }
            }
        }

        pullRefresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesTab(
    pullToRefreshState: PullToRefreshState,
    args: Grades,
    viewModel: VulkaViewModel = hiltViewModel(),

    refreshed: Boolean
) {
    val gradesDb = viewModel.gradesRepository.getByCredentialsId(UUID.fromString(args.userId))
    if (gradesDb != null) {
        val gradeList: List<Grade> = gradesDb.map { it.grade }
        val uniqueSubjectNames: Set<String> = gradeList.map { it.subjectName }.sortedBy { it }.toSet()

        Box(
            modifier = Modifier.nestedScroll(connection = pullToRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    if (refreshed) uniqueSubjectNames.forEach { subjectName ->
                        SubjectCard(
                            more = {
                                val filterGrades =
                                    gradeList.filter { it.subjectName == subjectName }

                                filterGrades.forEach { grade ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 32.dp)
                                            .padding(vertical = 2.dp),
                                    ) {
                                        Avatar(
                                            text = grade.value?.replace("\\.0$".toRegex(), "") ?: "...",
                                            shape = AvatarShape.Rounded
                                        )
                                        Column(
                                            Modifier.fillMaxWidth()
                                                .weight(1f)
                                                .padding(horizontal = 10.dp)
                                        ) {
                                            Text(
                                                text = grade.name,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                fontSize = 12.sp,
                                                text = "${grade.date}  Waga: ${
                                                    grade.weight.toString()
                                                        .replace("\\.0$".toRegex(), "")
                                                }"
                                            )
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(subjectName)
                            Text(
                                fontSize = 12.sp,
                                text = "${
                                    viewModel.gradesRepository.countBySubjectAndCredentials(
                                        UUID.fromString(args.userId),
                                        subjectName
                                    )
                                } Ocen"
                            )
                        }
                    }
                }
            }
        }
    } else {
        Text("Not loaded")
    }
}

@Composable
fun SubjectCard(
    more: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    var showMore by rememberMutableBoolean()

    Surface(
        onClick = {
            showMore = !showMore
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp)
                    .padding(12.dp),
            ) {
                Column(
                    Modifier.fillMaxWidth()
                        .weight(1f)
                ) {
                    content()
                }

                IconBox(
                    if (showMore) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
                )
            }

            ExpandedIfNotEmpty(Unit.takeIf { showMore }) {
                Column(
                    modifier = Modifier.padding(12.dp),
                ) {
                    more()
                }
            }
        }
    }
}

@Composable
fun SummaryTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(stringResource(R.string.Summary))
        }
    }
}