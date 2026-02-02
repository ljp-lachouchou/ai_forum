package ai.ljp.designsystem.component.expandbutton

import android.util.Size
import androidx.compose.animation.core.Animatable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberExpandButtonState(initExpanded: Boolean = false) : ExpandButtonState {
    val actionColor = MaterialTheme.colorScheme.primary
    val expandedColor = MaterialTheme.colorScheme.primaryContainer
    val state = remember {
        ExpandButtonState(
            initExpanded = initExpanded,
            actionColor = actionColor,
            expandedColor = expandedColor
        )
    }

    return state
}
class ExpandButtonState(
    initExpanded : Boolean = false,
    initAngle : Float = 0f,
    val size : Dp = 75.dp,
    val targetAngle : Float = 45f,
    val actionColor : Color,
    val expandedColor : Color
) {
    var expanded by mutableStateOf(initExpanded)
        private set
    fun rotate() {
        expanded = !expanded

    }
}