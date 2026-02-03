package io.ljp.simapi.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpModule(val moduleType : ModuleType)

enum class ModuleType {
    Logging
}