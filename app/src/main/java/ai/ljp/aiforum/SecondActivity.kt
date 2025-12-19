package ai.ljp.aiforum

import ai.ljp.aiforum.ui.theme.AIForumTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.ljp.common.event.observeEvent
import com.ljp.common.log.core.printer.i
import com.ljp.common.log.core.priority.LogcatPriorityInstance
import io.ljp.simapi.ExampleViewModel
import kotlin.getValue

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm by viewModels<ExampleViewModel>()
        observeEvent("USER_CLICK_ACTION","SecondScreen1", isSticky = true)
        setContent {
            i(LogcatPriorityInstance,"hello")
            AIForumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}



