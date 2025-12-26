package ai.ljp.aiforum

import ai.ljp.aiforum.ui.theme.AIForumTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.ljp.common.baseui.keyword.KeyboardManager

class KeyboardManagerTestActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIForumTheme {
                KeyboardTestScreen()
            }
        }
    }
}

@Composable
fun KeyboardTestScreen() {
    val view = LocalView.current
    var logText by remember { mutableStateOf("等待操作...") }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "KeyboardManager 测试中心")
            Text(text = logText, color = Color.Gray)

            Button(onClick = {
                KeyboardManager.hideKeyboard(view)
                logText = "手动触发了收起"
            }) {
                Text("强制收起键盘")
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .imePadding()
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(20.dp)
        ) {
            Text("👇 这是一个会被顶起的测试条")

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(value = "", onValueChange = {})
        }
    }
}