package io.github.vulka.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class AvatarShape {
    Rounded,
    Circle
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    text: String,
    shape: AvatarShape = AvatarShape.Circle,
    onClick: () -> Unit = {},
) {
    Card(
        shape = if (shape == AvatarShape.Circle) RoundedCornerShape(50.dp) else CardDefaults.shape,
        modifier = Modifier.size(45.dp).then(modifier),
        onClick = onClick
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = text
            )
        }
    }
}