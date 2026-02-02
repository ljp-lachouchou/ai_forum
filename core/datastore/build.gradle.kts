plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
}

android {
    defaultConfig {
        /*
        consumerProguardFiles
        当 这个 module 被作为 library 依赖 引入时，
        把 .pro 自动合并到 最终 App 的混淆规则中。
        -keepclassmembers class * extends GeneratedMessageLite* {
           <fields>;
        }
        凡是继承 GeneratedMessageLite 的类（也就是 proto 生成类），
        不要混淆 / 删除它们的字段。
         */
        consumerProguardFiles("proguard-rules.pro")
        consumerProguardFiles("consumer-rules.pro")
        // 防止混淆而导致proto生成的类的字段出错
    }
    namespace = "ai.ljp.datastore"

}

dependencies {
    api(libs.androidx.dataStore)
    api(projects.core.model)
    api(projects.core.datastoreProto)

    api(projects.core.common)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
