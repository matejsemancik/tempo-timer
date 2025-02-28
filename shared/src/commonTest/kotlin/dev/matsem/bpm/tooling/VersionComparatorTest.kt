package dev.matsem.bpm.tooling

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VersionComparatorTest {

    @Test
    fun `parseVersion parses standard semantic versions correctly`() {
        assertEquals(listOf(1, 2, 3), VersionComparator.parseVersion("1.2.3"))
        assertEquals(listOf(0, 1, 0), VersionComparator.parseVersion("0.1.0"))
        assertEquals(listOf(10, 20, 30), VersionComparator.parseVersion("10.20.30"))
    }

    @Test
    fun `parseVersion handles versions with suffix correctly`() {
        assertEquals(listOf(1, 2, 3), VersionComparator.parseVersion("1.2.3-beta"))
        assertEquals(listOf(1, 2, 3), VersionComparator.parseVersion("1.2.3-rc.1"))
        assertEquals(listOf(2, 0, 0), VersionComparator.parseVersion("2.0.0-alpha.1"))
        assertEquals(listOf(1, 0, 0), VersionComparator.parseVersion("1.0.0+build.123"))
    }

    @Test
    fun `parseVersion returns fallback for invalid version strings`() {
        val fallback = listOf(0, 0, 0)
        assertEquals(fallback, VersionComparator.parseVersion("invalid"))
        assertEquals(fallback, VersionComparator.parseVersion("1.2"))
        assertEquals(fallback, VersionComparator.parseVersion(""))
        assertEquals(fallback, VersionComparator.parseVersion(".1.2"))
        assertEquals(fallback, VersionComparator.parseVersion("a.b.c"))
    }

    @Test
    fun `parseVersion handles mixed valid and invalid parts`() {
        assertEquals(listOf(0, 0, 0), VersionComparator.parseVersion("1.a.3"))
        assertEquals(listOf(0, 0, 0), VersionComparator.parseVersion("1..3"))
    }

    @Test
    fun `isUpdateAvailable compares major versions correctly`() {
        assertTrue(VersionComparator.isUpdateAvailable("1.0.0", "2.0.0"))
        assertTrue(VersionComparator.isUpdateAvailable("1.5.10", "2.0.0"))
        assertFalse(VersionComparator.isUpdateAvailable("2.0.0", "1.0.0"))
        assertFalse(VersionComparator.isUpdateAvailable("2.5.1", "1.9.9"))
    }

    @Test
    fun `isUpdateAvailable compares minor versions correctly when major versions are equal`() {
        assertTrue(VersionComparator.isUpdateAvailable("1.0.0", "1.1.0"))
        assertTrue(VersionComparator.isUpdateAvailable("2.1.5", "2.2.0"))
        assertFalse(VersionComparator.isUpdateAvailable("1.2.0", "1.1.0"))
        assertFalse(VersionComparator.isUpdateAvailable("3.5.0", "3.4.9"))
    }

    @Test
    fun `isUpdateAvailable compares patch versions correctly when major and minor versions are equal`() {
        assertTrue(VersionComparator.isUpdateAvailable("1.0.0", "1.0.1"))
        assertTrue(VersionComparator.isUpdateAvailable("2.3.5", "2.3.6"))
        assertFalse(VersionComparator.isUpdateAvailable("1.1.2", "1.1.1"))
        assertFalse(VersionComparator.isUpdateAvailable("3.2.10", "3.2.9"))
    }

    @Test
    fun `isUpdateAvailable ignores version suffixes`() {
        assertTrue(VersionComparator.isUpdateAvailable("1.0.0-beta", "1.0.1"))
        assertTrue(VersionComparator.isUpdateAvailable("1.0.0", "1.0.1-alpha"))
        assertFalse(VersionComparator.isUpdateAvailable("1.0.1-rc.1", "1.0.0"))
        assertTrue(VersionComparator.isUpdateAvailable("1.0.0+build.123", "1.0.1"))
    }

    @Test
    fun `isUpdateAvailable handles invalid version strings`() {
        // If either version is invalid, it will be parsed as 0.0.0
        assertTrue(VersionComparator.isUpdateAvailable("invalid", "0.0.1"))
        assertFalse(VersionComparator.isUpdateAvailable("0.0.1", "invalid"))
        assertFalse(VersionComparator.isUpdateAvailable("invalid", "invalid"))

        // Partial version strings are considered invalid
        assertTrue(VersionComparator.isUpdateAvailable("1.2", "1.2.3"))
    }

    @Test
    fun `isUpdateAvailable handles equal versions correctly`() {
        assertFalse(VersionComparator.isUpdateAvailable("1.0.0", "1.0.0"))
        assertFalse(VersionComparator.isUpdateAvailable("1.2.3", "1.2.3"))

        // Even with different suffixes, they are considered equal
        assertFalse(VersionComparator.isUpdateAvailable("1.0.0-beta", "1.0.0-alpha"))
        assertFalse(VersionComparator.isUpdateAvailable("1.0.0-rc.1", "1.0.0+build.123"))
    }
}