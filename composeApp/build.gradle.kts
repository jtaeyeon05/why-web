import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.3.0"
}

val version = "1.0.0B"
val buildNumber = LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))!!
val buildInfoPackage = "io.github.jtaeyeon05.why_web.buildinfo"
val buildInfoDir = layout.buildDirectory.dir("generated/sources/buildInfo/kotlin")

@CacheableTask
abstract class GenerateBuildInfoTask : DefaultTask() {
    @get:Input
    abstract val versionProp: Property<String>
    @get:Input
    abstract val buildNumberProp: Property<String>
    @get:Input
    abstract val packageProp: Property<String>
    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val out = outDir.get().asFile
        val packagePath = packageProp.get().replace('.', '/')
        val file = File(out, "$packagePath/BuildInfo.kt")
        file.parentFile.mkdirs()
        file.writeText(
            """
            package ${packageProp.get()}
            
            object BuildInfo {
                const val VERSION = "${versionProp.get()}"
                const val BUILD_NUMBER  = "${buildNumberProp.get()}"
            }
            
            """.trimIndent()
        )
        print(">> Generated BuildInfo.kt at: ${file.absolutePath}")
    }
}

val generateBuildInfo by tasks.register<GenerateBuildInfoTask>("generateBuildInfo") {
    versionProp.set(version)
    buildNumberProp.set(buildNumber)
    packageProp.set(buildInfoPackage)
    outDir.set(buildInfoDir)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(buildInfoDir)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.2")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
            implementation("io.github.vinceglb:confettikit:0.7.0")
            implementation("io.github.parkwoocheol:compose-webview:1.8.1")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
        dependsOn(generateBuildInfo)
    }
}
