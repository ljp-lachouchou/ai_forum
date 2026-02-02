plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "ai.ljp.datastore_proto"
}

protobuf {
    protoc { // 确定使用哪个 protoc 版本
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks { // 根据proto生成java/kotlin版本的代码
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite") //
                }
            }
        }
    }
}

androidComponents.beforeVariants { // 确定文件生成路径,
    android.sourceSets.named(it.name) {
        val buildDir = layout.buildDirectory.get().asFile
        java.srcDir(buildDir.resolve("generated/source/proto/${it.name}/java"))
        kotlin.srcDir(buildDir.resolve("generated/source/proto/${it.name}/kotlin"))
    }
}

dependencies { // 把生成类型作为 API 暴露,不然其他module调用不了生成类型
    api(libs.protobuf.kotlin.lite)
}