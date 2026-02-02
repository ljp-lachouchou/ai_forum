package com.ljp.common.network

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(
    val aiforumDispatcher : AIForumDispatchers
)

enum class AIForumDispatchers {
    Default,
    IO
}