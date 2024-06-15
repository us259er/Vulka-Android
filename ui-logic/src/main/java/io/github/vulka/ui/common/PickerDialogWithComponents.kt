package io.github.vulka.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.medzik.android.components.ui.BaseDialog
import dev.medzik.android.components.ui.DialogState

@Composable
fun <T> PickerDialogWithComponents(
    state: DialogState,
    title: String?,
    items: List<T>,
    onSelected: (T) -> Unit,
    content: @Composable (T) -> Unit,
    components: @Composable () -> Unit = {}
) {
    BaseDialog(state) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (title != null) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 8.dp)
                )
            }

            items.forEach { item ->
                Box(
                    modifier = Modifier.fillMaxWidth().clickable {
                        onSelected(item)
                        state.hide()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        content(item)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                components()
            }

        }
    }
}