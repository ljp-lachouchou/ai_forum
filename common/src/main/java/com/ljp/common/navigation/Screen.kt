package com.ljp.common.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    sealed class MainPage : Screen() {
        @Serializable
        data object Home : MainPage()
        @Serializable
        data class Find(val id: String) : MainPage()
        @Serializable
        data object TreeHollow : MainPage()
        @Serializable
        data object Profile : MainPage()
    }


}