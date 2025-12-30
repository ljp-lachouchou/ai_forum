package com.ljp.common.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun <T : Screen> AppNavigation(
                               startDestination: T,
                               navController: NavHostController = rememberNavController(),
                               builder: NavGraphBuilder.() -> Unit) =
    NavHost(navController = navController, startDestination = startDestination, builder = builder)

