package com.sahan.birdspacex.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sahan.birdspacex.presentation.screen.LaunchDetailRoute
import com.sahan.birdspacex.presentation.screen.LaunchListRoute

@Composable
fun SpaceNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SpaceDestination.LaunchList,
        enterTransition = { slideInHorizontally() },
        exitTransition = { slideOutVertically() }
    ) {
        composable<SpaceDestination.LaunchList> {
            LaunchListRoute(
                onNavigateToDetail = { launchId ->
                    navController.navigate(SpaceDestination.LaunchDetail(launchId))
                },
            )
        }

        composable<SpaceDestination.LaunchDetail> { backStackEntry ->
            val destination = backStackEntry.toRoute<SpaceDestination.LaunchDetail>()
            LaunchDetailRoute(
                launchId = destination.launchId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
