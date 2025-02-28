package dev.matsem.bpm.tooling

/**
 * Utility class for comparing semantic versioning strings.
 * Handles version strings in the format "major.minor.patch[-suffix]"
 */
object VersionComparator {

    /**
     * Compares two version strings to determine if a newer version is available.
     *
     * @param currentVersion The current version string (e.g., "1.2.3")
     * @param latestVersion The latest version string to compare against (e.g., "1.3.0")
     * @return true if latest version is newer than current version, false otherwise
     */
    fun isUpdateAvailable(currentVersion: String, latestVersion: String): Boolean {
        val current = parseVersion(currentVersion)
        val latest = parseVersion(latestVersion)

        // Compare major version
        if (latest[0] > current[0]) return true
        if (latest[0] < current[0]) return false

        // Compare minor version
        if (latest[1] > current[1]) return true
        if (latest[1] < current[1]) return false

        // Compare patch version
        return latest[2] > current[2]
    }

    /**
     * Parses a version string into a list of integers representing major, minor, and patch versions.
     * Handles suffixes by ignoring them (e.g., "1.2.3-beta" is treated as "1.2.3").
     * Returns [0, 0, 0] for invalid version strings.
     *
     * @param version The version string to parse
     * @return List of 3 integers representing major, minor, and patch versions
     */
    fun parseVersion(version: String): List<Int> {
        // Remove any suffix (text after hyphen or plus symbol) if present
        val versionWithoutSuffix = version.split(Regex("[-+]"))[0]

        return versionWithoutSuffix.split('.')
            .mapNotNull { it.toIntOrNull() }
            .takeIf { it.size >= 3 } // Ensure we have at least major.minor.patch
            ?: listOf(0, 0, 0) // Default to 0.0.0 if parsing fails
    }
}