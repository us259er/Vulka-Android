package io.github.vulka.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.medzik.android.compose.ui.IconBox
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
object Timetable

@Composable
fun TimetableScreen() {

    Column {
        Column(
            modifier = Modifier.fillMaxSize().weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Timetable")
        }

        Surface(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = {

                    }
                ) {
                    IconBox(
                        imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                    )
                }

                Text(
                    fontWeight = FontWeight.Bold,
                    text = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                )

                IconButton(
                    onClick = {

                    }
                ) {
                    IconBox(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    )
                }
            }
        }
    }
}