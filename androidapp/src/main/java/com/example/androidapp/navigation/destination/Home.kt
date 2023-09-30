package com.example.androidapp.navigation.destination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.androidapp.models.Category
import com.example.androidapp.navigation.Screen
import com.example.androidapp.screen.home.HomeScreen
import com.example.androidapp.screen.home.HomeViewModel

fun NavGraphBuilder.homeRoute(onCategorySelect: (Category) -> Unit, onPostClick: (String) -> Unit) {
    composable(route = Screen.HomeScreen.route)
    {
        val vieModel: HomeViewModel = viewModel()
        val posts = vieModel.allPosts.value
        val searchedPosts = vieModel.searchedPosts.value
        var searchBarOpen by remember {
            mutableStateOf(false)
        }
        var active by remember {
            mutableStateOf(false)
        }
        var query by remember {
            mutableStateOf("")
        }
        HomeScreen(posts,
            searchedPosts = searchedPosts,
            searchBarOpen = searchBarOpen,
            active = active,
            onActiveChange = {
                active = it
            },
            query = query,
            onSearch = vieModel::getPostsByTitle,
            onQueryChange = {
                query = it

            },
            onPostClick = onPostClick,
            onSearchBarChange = { opened ->
                searchBarOpen = opened
                if (!opened) {
                    query = ""
                    active = false
                    vieModel.resetSearch()
                }
            }, onCategorySelect =onCategorySelect)
    }

}