package dev.matsem.bpm.data.service.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    @SerialName("url") val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("assets_url") val assetsUrl: String,
    @SerialName("upload_url") val uploadUrl: String,
    @SerialName("tarball_url") val tarballUrl: String?,
    @SerialName("zipball_url") val zipballUrl: String?,
    @SerialName("id") val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("tag_name") val tagName: String,
    @SerialName("target_commitish") val targetCommitish: String,
    @SerialName("name") val name: String?,
    @SerialName("body") val body: String?,
    @SerialName("draft") val draft: Boolean,
    @SerialName("prerelease") val prerelease: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("assets") val assets: List<ReleaseAsset> = emptyList(),
)

@Serializable
data class ReleaseAsset(
    @SerialName("url") val url: String,
    @SerialName("browser_download_url") val browserDownloadUrl: String,
    @SerialName("id") val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("name") val name: String,
    @SerialName("label") val label: String?,
    @SerialName("content_type") val contentType: String,
    @SerialName("state") val state: String,
    @SerialName("size") val size: Long,
    @SerialName("download_count") val downloadCount: Long,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)