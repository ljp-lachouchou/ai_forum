package ai.ljp.aiforum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.ui.tooling.preview.Preview
import ai.ljp.aiforum.ui.theme.AIForumTheme
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ljp.common.event.EventHandler
import com.ljp.common.event.asInteractionEvent
import com.ljp.common.event.observeEvent
import com.ljp.common.event.publish
import com.ljp.common.log.core.printer.i
import com.ljp.common.log.core.priority.LogcatPriorityInstance

import io.ljp.simapi.ExampleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm by viewModels<ExampleViewModel>()
        observeEvent("USER_CLICK_ACTION","MainScreen")
        setContent {
            i(LogcatPriorityInstance,"hello")
            val intent = Intent(this, SecondActivity::class.java)
            AIForumTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding).clickable {
                                val clickTask: EventHandler = {
                                    i(LogcatPriorityInstance, "执行了点击后的具体业务逻辑！")
                                    vm.fetchData()
                                }
                                clickTask
                                    .asInteractionEvent("USER_CLICK_ACTION",
                                        )
                                    .publish()
                            }
                        )
                        Button(onClick = {
                            startActivity(intent)
                        }) {
                            Text("跳转")
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AIForumTheme {
        Greeting("Android")
    }
}