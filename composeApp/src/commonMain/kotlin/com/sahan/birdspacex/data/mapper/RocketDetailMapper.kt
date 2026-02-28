package com.sahan.birdspacex.data.mapper

import com.sahan.birdspacex.data.remote.response.model.RocketResponseModel
import com.sahan.birdspacex.domain.model.RocketUiModel

class RocketDetailMapper : IMapper<RocketResponseModel?, RocketUiModel?> {
    override fun map(data: RocketResponseModel?): RocketUiModel? {
        data ?: return null
        return RocketUiModel(
            name = data.name.orEmpty().ifBlank { "-" },
            description = data.description.orEmpty().ifBlank { "-" },
        )
    }
}
