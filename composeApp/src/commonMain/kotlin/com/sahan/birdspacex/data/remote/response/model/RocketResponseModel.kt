package com.sahan.birdspacex.data.remote.response.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketResponseModel(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("active")
    val active: Boolean? = null,
    @SerialName("first_flight")
    val firstFlight: String? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("company")
    val company: String? = null,
    @SerialName("wikipedia")
    val wikipedia: String? = null,
    @SerialName("flickr_images")
    val flickrImages: List<String>? = null,
    @SerialName("cost_per_launch")
    val costPerLaunch: Long? = null,
    @SerialName("success_rate_pct")
    val successRatePct: Int? = null,
)
