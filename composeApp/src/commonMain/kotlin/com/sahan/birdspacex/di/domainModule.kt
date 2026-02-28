package com.sahan.birdspacex.di

import com.sahan.birdspacex.domain.usecase.GetLaunchDetailUseCase
import com.sahan.birdspacex.domain.usecase.GetLaunchesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetLaunchesUseCase(repository = get()) }
    factory { GetLaunchDetailUseCase(repository = get()) }
}
