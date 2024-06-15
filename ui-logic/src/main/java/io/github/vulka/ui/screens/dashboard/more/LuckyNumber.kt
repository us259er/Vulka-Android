package io.github.vulka.ui.screens.dashboard.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vulka.ui.R
import kotlinx.serialization.Serializable

@Serializable
class LuckyNumber(
    val luckyNumber: Int
)

@Composable
fun LuckyNumberScreen(args: LuckyNumber) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = if (args.luckyNumber != 0)
                stringResource(R.string.LuckyNumberText) + " " + args.luckyNumber.toString()
            else
                stringResource(R.string.LuckyNumberNone),
            fontSize = 20.sp,
        )
    }
}