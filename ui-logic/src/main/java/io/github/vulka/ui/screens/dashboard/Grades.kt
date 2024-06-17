package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.medzik.android.components.rememberMutableBoolean
import dev.medzik.android.utils.runOnUiThread
import io.github.vulka.core.api.Platform
import io.github.vulka.core.api.types.Grade
import io.github.vulka.ui.R
import io.github.vulka.ui.VulkaViewModel
import io.github.vulka.ui.common.Avatar
import io.github.vulka.ui.common.AvatarShape
import io.github.vulka.ui.utils.getInitials
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
class Grades(
    val platform: Platform,
    val userId: String,
    val credentials: String
)

@Composable
fun GradesScreen(args: Grades) {
    val pagerState = rememberPagerState { 2 }
    val tabs = listOf(
        stringResource(R.string.Grades),
        stringResource(R.string.Summary),
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = @Composable { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.PrimaryIndicator(
                        width = 80.dp,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
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
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    when (page) {
                        0 -> GradesTab(args)
                        1 -> SummaryTab()
                    }
                }
            }
        }
    }
}

@Composable
fun GradesTab(
    args: Grades,
    viewModel: VulkaViewModel = hiltViewModel()
) {
    val gradesDb = viewModel.gradesRepository.getByCredentialsId(UUID.fromString(args.userId))
    if (gradesDb != null) {
        val gradeList: List<Grade> = gradesDb.map { it.grade }
        val uniqueSubjectNames: Set<String> = gradeList.map { it.subjectName }.sortedBy { it }.toSet()

        uniqueSubjectNames.forEach { subjectName ->
            SubjectCard(
                more = {
                    val filterGrades = gradeList.filter { it.subjectName == subjectName }

                    filterGrades.forEach { grade ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 32.dp)
                                .padding(vertical = 2.dp),
                        ) {
                            Avatar(
                                text = grade.value?.toString()?.replace("\\.0$".toRegex(), "") ?: "...",
                                shape = AvatarShape.Rounded
                            )
                            Column(
                                Modifier.fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(text = grade.name)
                                Text(
                                    fontSize = 12.sp,
                                    text = "${grade.date}  Waga: ${grade.weight.toString().replace("\\.0$".toRegex(), "")}"
                                )
                            }
                        }
                    }
                }
            ) {
                Text(subjectName)
                Text(
                    fontSize = 12.sp,
                    text = "${viewModel.gradesRepository.countBySubjectAndCredentials(UUID.fromString(args.userId),subjectName)} Ocen"
                )
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
        modifier = Modifier.fillMaxWidth().padding(3.dp),
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
                    .padding(8.dp),
            ) {
                Column(
                    Modifier.fillMaxWidth()
                        .weight(1f)
                ) {
                    content()

                }

                Icon(if (showMore) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown, contentDescription = null)

            }

            if (showMore) {
                Column(
                    modifier = Modifier.padding(8.dp),
                ) {
                    more()
                }
            }
        }




    }
}

@Composable
fun SummaryTab() {
    Text(stringResource(R.string.Summary))
}