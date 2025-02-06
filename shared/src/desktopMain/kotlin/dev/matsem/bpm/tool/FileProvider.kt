package dev.matsem.bpm.tool

import net.harawata.appdirs.AppDirsFactory
import java.io.File
import java.nio.file.Path

/**
 * Platform-agnostic file paths.
 */
object FileProvider {

    /**
     * Returns a path to directory where application data such as Room DB or DataStore are stored.
     */
    fun getApplicationDataPath(): Path {
        return File(AppDirsFactory.getInstance().getUserDataDir("dev.matsem.bpm", "common", "matsemdev")).toPath()
    }
}