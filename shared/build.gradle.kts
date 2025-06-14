plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.androidx.room)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        val commonTest by getting

        commonMain.dependencies {
            // compose
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.markdownRenderer)

            // kx
            implementation(libs.kotlinx.collections)
            implementation(libs.kotlinx.datetime)

            // koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            // network
            implementation(libs.ktorfit.lib)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.plugin.contentNegotiation)
            implementation(libs.ktor.plugin.logging)
            implementation(libs.ktor.plugin.auth)

            // coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.svg)

            // persistence
            implementation(libs.androidx.datastore)

            // database
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // other
            implementation(libs.platformTools.darkModeDetector)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.engine.okhttp)
            implementation(libs.appDirs)
        }
    }
}

dependencies {
    ksp(libs.androidx.room.compiler)
}

room {
    schemaDirectory(project.layout.projectDirectory.dir("room-schemas"))
}
