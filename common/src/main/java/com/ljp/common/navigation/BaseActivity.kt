package com.ljp.common.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ljp.common.event.NavEvent
import com.ljp.common.event.observeEvent
import com.ljp.common.log.core.printer.simName

internal interface Navigable {
    val navHostController : NavHostController
    val startDestination : Screen

    fun appHost() :NavGraphBuilder.() -> Unit
}
abstract class BaseActivity : ComponentActivity(), Navigable {
    override lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navHostController = rememberNavController()
            LaunchedEffect(navHostController) {
                observeEvent(NavEvent.EVENT_NAME, this@BaseActivity.simName) { event ->
                    navHostController.navigate(event.route)
                }
            }
            AppNavigation(startDestination,navHostController,appHost())
        }
    }
}