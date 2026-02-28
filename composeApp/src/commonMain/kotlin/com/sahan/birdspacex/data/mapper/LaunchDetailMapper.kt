package com.sahan.birdspacex.data.mapper

import com.sahan.birdspacex.data.remote.response.model.LaunchResponseModel
import com.sahan.birdspacex.domain.model.ExternalLinksUiModel
import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.RocketUiModel
import com.sahan.birdspacex.platform.PlatformDateFormatter

data class LaunchDetailSource(
    val launch: LaunchResponseModel,
    val rocket: RocketUiModel?,
)

class LaunchDetailMapper(
    private val dateFormatter: PlatformDateFormatter,
) : IMapper<LaunchDetailSource, LaunchDetailUiModel> {

    override fun map(data: LaunchDetailSource): LaunchDetailUiModel {
        val status = data.launch.success.toStatus()
        return LaunchDetailUiModel(
            missionName = data.launch.name.orEmpty().ifBlank { "-" },
            launchDateTimeText = dateFormatter.formatLaunchDetailDateTime(data.launch.dateUtc.orEmpty()),
            rocket = data.rocket,
            successText = status.toText(),
            details = data.launch.details,
            webcastUrl = data.launch.links?.webcast,
            links = ExternalLinksUiModel(
                article = data.launch.links?.article,
                wikipedia = data.launch.links?.wikipedia,
            ),
        )
    }
}
