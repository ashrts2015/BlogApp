package com.example.androidapp.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidapp.components.NavigationDrawer
import com.example.androidapp.components.PostCardsView
import com.example.androidapp.models.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: RequestState<List<Post>>,
    searchedPosts: RequestState<List<Post>>,
    searchBarOpen: Boolean,
    query: String,
    active: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onSearchBarChange: (Boolean) -> Unit,
    onCategorySelect: (Category) -> Unit,

    onActiveChange: (Boolean) -> Unit,
    onPostClick: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState,
        onCategorySelect = { category ->  onCategorySelect(category)
            scope.launch { drawerState.close() }}) {


        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = { Text("BlogApp") }, navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "hello")
                }
            }, actions = {
                IconButton(onClick = {
                    onSearchBarChange(true)
                    onActiveChange(true)

                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
            })
            if (searchBarOpen) {
                SearchBar(
                    active = active,
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = onSearch,
                    onActiveChange = onActiveChange,
                    modifier = Modifier.padding(top = 12.dp),
                    placeholder = { Text(text = "Search here ...") },

                    leadingIcon = {
                        IconButton(onClick = {
                            onSearchBarChange(false)
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "hello"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            onQueryChange("")
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "hello")
                        }
                    }
                )
                {
                    PostCardsView(
                        posts = searchedPosts,
                        topMargin = 12.dp,
                        onPostClick = onPostClick
                    )
                }
            }
        }
        )
        {
            PostCardsView(
                posts = posts,
                topMargin = it.calculateTopPadding(),
                onPostClick = onPostClick
            )

        }
    }
}


