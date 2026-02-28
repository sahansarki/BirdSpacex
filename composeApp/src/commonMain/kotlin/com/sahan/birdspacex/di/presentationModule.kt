package com.sahan.birdspacex.di

import com.sahan.birdspacex.domain.DefaultDispatcherProvider
import com.sahan.birdspacex.domain.util.DispatcherProvider
import com.sahan.birdspacex.presentation.viewmodel.LaunchDetailViewModel
import com.sahan.birdspacex.presentation.viewmodel.LaunchListViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory {
        LaunchListViewModel(
            getLaunchesUseCase = get(),
            dispatcherProvider = get()
        )
    }
    factory {
        LaunchDetailViewModel(
            getLaunchDetailUseCase = get(),
            dispatcherProvider = get()
        )
    }
}

val coroutineModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider }
}
