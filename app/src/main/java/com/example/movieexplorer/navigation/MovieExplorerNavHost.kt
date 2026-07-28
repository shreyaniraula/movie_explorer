package com.example.movieexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieexplorer.presentation.details.MovieDetailsScreen
import com.example.movieexplorer.presentation.home.HomeScreen
import com.example.movieexplorer.presentation.search.SearchScreen
import com.example.movieexplorer.presentation.settings.SettingsScreen

@Composable
fun MovieExplorerNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Search.route) {
            SearchScreen(
                onMovieClick = { imdbId ->
                    navController.navigate(Screen.Details.createRoute(imdbId))
                }
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument("imdbId") { type = NavType.StringType })
        ) {
            MovieDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Home.route,
        ) {
            HomeScreen(
                onMovieClick = { imdbId -> navController.navigate(Screen.Details.createRoute(imdbId)) },
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(
            route = Screen.Settings.route,
        ) {
            SettingsScreen()
        }
    }
}