import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

// https://github.com/JetBrains/compose-hot-reload?tab=readme-ov-file#enable-optimizenonskippinggroups
composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            // project
            implementation(projects.shared)

            // compose
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.matsem.bpm.MainKt"
        jvmArgs("-Dapple.awt.application.appearance=system", "-Dapple.awt.enableTemplateImages=true")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "Tempo Timer"
            packageVersion = System.getenv("DESKTOP_PACKAGE_VERSION")?.takeIf { it.isNotBlank() } ?: "1.2.1"
            description = "Desktop client for Tempo Timesheets"
            copyright = "Copyright (c) 2025 Matej Semančík"
            vendor = "matsem.dev"
            licenseFile.set(rootProject.file("LICENSE"))

            // https://github.com/JetBrains/compose-multiplatform/issues/2686
            modules("jdk.unsupported")

            macOS {
                iconFile.set(project.file("nativeDistributions/macOS/icon.icns"))
                infoPlist {

                }
                infoPlist {
                    extraKeysRawXml = project.file("nativeDistributions/macOS/Info.plist").readText()
                }
            }

            windows {
                iconFile.set(project.file("nativeDistributions/windows/icon.ico"))
            }

            linux {
                iconFile.set(project.file("nativeDistributions/linux/icon.png"))
            }
        }
    }
}
