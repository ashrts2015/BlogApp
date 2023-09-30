package com.example.androidapp.navigation.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androidapp.navigation.Screen
import com.example.androidapp.screen.details.DetailsScreen
import com.example.androidapp.util.Constants

fun NavGraphBuilder.detailRoute(onBackPressed:()->Unit)
{
    composable(
        route = Screen.DetailsScreen.route,
        arguments = listOf(navArgument(name = Constants.POST_ID_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val postId = it.arguments?.getString(Constants.POST_ID_ARGUMENT)
        DetailsScreen(
            url = "http://192.168.0.3:8080/posts/post?postId=$postId&showSections=false",
            onBackPress = onBackPressed
        )
    }
}