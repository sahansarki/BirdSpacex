package com.sahan.birdspacex.data.mapper

import com.sahan.birdspacex.data.remote.response.model.LaunchResponseModel
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.model.LaunchStatus
import com.sahan.birdspacex.platform.PlatformDateFormatter

class LaunchListItemMapper(
    private val dateFormatter: PlatformDateFormatter,
) : IMapper<LaunchResponseModel, LaunchListItemUiModel> {

    override fun map(data: LaunchResponseModel): LaunchListItemUiModel {
        val status = data.success.toStatus()
        return LaunchListItemUiModel(
            id = data.id,
            missionName = data.name.orEmpty().ifBlank { "-" },
            launchDateText = dateFormatter.formatLaunchListDate(data.dateUtc.orEmpty()),
            rocketName = data.rocket.orEmpty(),
            status = status,
            successText = status.toText(),
            patchImageUrl = data.links?.patch?.small,
        )
    }
}

fun Boolean?.toStatus(): LaunchStatus {
    return when (this) {
        true -> LaunchStatus.SUCCESS
        false -> LaunchStatus.FAILURE
        null -> LaunchStatus.UNKNOWN
    }
}

fun LaunchStatus.toText(): String {
    return when (this) {
        LaunchStatus.SUCCESS -> "Success"
        LaunchStatus.FAILURE -> "Failure"
        LaunchStatus.UNKNOWN -> "-"
    }
}
