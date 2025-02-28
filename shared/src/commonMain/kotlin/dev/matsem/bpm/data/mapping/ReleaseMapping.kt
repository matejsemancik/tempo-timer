package dev.matsem.bpm.data.mapping

import dev.matsem.bpm.data.repo.model.AppVersion
import dev.matsem.bpm.data.service.github.model.Release as ApiRelease

fun ApiRelease.toAppVersion(): AppVersion = AppVersion(
    version = tagName.removePrefix("v"),
    name = name,
    releaseUrl = htmlUrl
)