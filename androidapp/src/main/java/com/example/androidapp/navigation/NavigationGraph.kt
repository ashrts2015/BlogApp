package com.example.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.androidapp.navigation.destination.categoryRoute
import com.example.androidapp.navigation.destination.detailRoute
import com.example.androidapp.navigation.destination.homeRoute


@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route)
    {

        homeRoute(onCategorySelect = { category ->
            navController.navigate(Screen.CategoryScreen.passCategory(category))
        }, onPostClick = { postId ->
            navController.navigate(Screen.DetailsScreen.passPostId(postId))
        })
        categoryRoute(onBackPress = {
            navController.popBackStack()
        }, onPostClick = { postId ->
            navController.navigate(Screen.DetailsScreen.passPostId(postId))
        })
        detailRoute(onBackPressed = { navController.popBackStack() })


    }

}