package com.example.androidapp.navigation.destination

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androidapp.models.Category
import com.example.androidapp.navigation.Screen
import com.example.androidapp.screen.category.CategoryScreen
import com.example.androidapp.screen.category.CategoryViewModel
import com.example.androidapp.util.Constants

fun NavGraphBuilder.categoryRoute(onBackPress: () -> Unit, onPostClick: (String) -> Unit) {
    composable(
        route = Screen.CategoryScreen.route,
        arguments = listOf(navArgument(name = Constants.CATEGORY_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val viewModel: CategoryViewModel = viewModel()
        val selectedCategory =
            it.arguments?.getString(Constants.CATEGORY_ARGUMENT) ?: Category.Programming.name
        CategoryScreen(
            posts = viewModel.categoryPosts.value,
            category = Category.valueOf(selectedCategory),
            onBackPress =onBackPress,
            onPostClick =onPostClick
        )
    }
}