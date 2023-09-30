package com.example.androidapp.navigation

import com.example.androidapp.models.Category

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object CategoryScreen : Screen("category_screen/{category}") {


        fun passCategory(category: Category): String {
            val categoryName = category.name
            return "category_screen/$categoryName"
        }
    }

    data object DetailsScreen : Screen("details_screen/{postid}") {
        fun passPostId(id: String) = "details_screen/$id"
    }
}


