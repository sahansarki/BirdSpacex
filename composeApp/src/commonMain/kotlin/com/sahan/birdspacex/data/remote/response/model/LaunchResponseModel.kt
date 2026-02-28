package com.sahan.birdspacex.data.remote.response.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchResponseModel(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("date_utc")
    val dateUtc: String? = null,
    @SerialName("success")
    val success: Boolean? = null,
    @SerialName("rocket")
    val rocket: String? = null,
    @SerialName("details")
    val details: String? = null,
    @SerialName("links")
    val links: LinksResponseModel? = null,
    @SerialName("failures")
    val failures: List<FailureResponseModel>? = null,
)

@Serializable
data class LinksResponseModel(
    @SerialName("patch")
    val patch: PatchResponseModel? = null,
    @SerialName("webcast")
    val webcast: String? = null,
    @SerialName("article")
    val article: String? = null,
    @SerialName("wikipedia")
    val wikipedia: String? = null,
    @SerialName("youtube_id")
    val youtubeId: String? = null,
)

@Serializable
data class PatchResponseModel(
    @SerialName("small")
    val small: String? = null,
    @SerialName("large")
    val large: String? = null,
)

@Serializable
data class FailureResponseModel(
    @SerialName("time")
    val time: Int? = null,
    @SerialName("altitude")
    val altitude: Int? = null,
    @SerialName("reason")
    val reason: String? = null,
)
