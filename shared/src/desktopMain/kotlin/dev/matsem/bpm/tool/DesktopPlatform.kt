package dev.matsem.bpm.tool

import dev.matsem.bpm.tooling.Platform

internal class DesktopPlatform : Platform {
    override fun getVersionString(): String =
        System.getProperty("jpackage.app-version")?.takeIf { it.isNotBlank() } ?: "dev"

    override fun isMacos(): Boolean =
        System.getProperty("os.name").lowercase().contains("mac")
}
