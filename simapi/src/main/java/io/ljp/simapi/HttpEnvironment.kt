package io.ljp.simapi
enum class Environment{ DEV, TEST, PROD}
object HttpEnvironment {
    val configs: Map<Environment, String> = mapOf(
        Environment.DEV to "https://dev.api.example.com",
        Environment.TEST to "https://test.api.example.com",
        Environment.PROD to "https://api.example.com" //需要修改
    )
}