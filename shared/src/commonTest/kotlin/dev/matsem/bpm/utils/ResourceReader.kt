package dev.matsem.bpm.utils

import java.nio.charset.StandardCharsets

/**
 * Reads a resource file from the `commonTest/resources` directory.
 *
 * @param resourceName The name of the file in the resources directory (e.g., "your_mock_data.json").
 * @return The content of the file as a String.
 * @throws IllegalArgumentException if the resource is not found.
 */
expect fun readResourceFile(resourceName: String): String
