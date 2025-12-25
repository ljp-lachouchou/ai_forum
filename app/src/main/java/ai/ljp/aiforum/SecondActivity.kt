package ai.ljp.aiforum

import ai.ljp.aiforum.ui.theme.AIForumTheme
import android.graphics.Canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.ljp.common.event.observeEvent
import com.ljp.common.log.core.printer.i
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import io.ljp.simapi.ExampleViewModel
import kotlin.getValue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm by viewModels<ExampleViewModel>()
        observeEvent("USER_CLICK_ACTION","SecondScreen", isSticky = true)
        setContent {
            i(LogcatPriorityInstance,"hello")
            AIForumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val floatValue = remember { Animatable(0f) }

                    // 启动一个无限循环的动画
                    LaunchedEffect(Unit) {
                        floatValue.animateTo(
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                    }
                    AnimatedCircularProgress(floatValue.value, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun AnimatedCircularProgress(
    targetProgress: Float, // 0.0 到 1.0
    modifier: Modifier = Modifier
) {
    // 1. 定义动画进度
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Canvas(modifier = modifier.size(200.dp)) {
        val strokeWidth = 15.dp.toPx()
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.minDimension - strokeWidth) / 2

        // 2. 绘制背景灰圆环
        drawCircle(
            color = Color.LightGray.copy(alpha = 0.3f),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        // 3. 绘制彩色进度弧形
        // 我们使用 SweepGradient 制造炫酷的旋转渐变感
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFF6200EE)),
                center = center
            ),
            startAngle = -90f, // 从 12 点钟方向开始
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // 4. 绘制进度末端的小发光点 (装饰)
        val angleInRadians = (360f * animatedProgress - 90f) * (PI / 180f).toFloat()
        val pointerCenter = Offset(
            x = center.x + radius * cos(angleInRadians),
            y = center.y + radius * sin(angleInRadians)
        )
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            setShadowLayer(10f, 0f, 0f, android.graphics.Color.WHITE) // 设置阴影/发光
            isAntiAlias = true
        }

        drawCircle(
            color = Color.White,
            radius = strokeWidth / 3f,
            center = pointerCenter
        )
    }
}



