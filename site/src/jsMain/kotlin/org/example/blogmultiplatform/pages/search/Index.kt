package org.example.blogmultiplatform.pages.search


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.LoadingIndicator
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItem
import org.example.blogmultiplatform.components.overFlowSidePanel
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.pages.sections.HeaderSection
import org.example.blogmultiplatform.pages.sections.PostSection
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.searchPostsByCategory
import org.example.blogmultiplatform.util.searchPostsByTitle
import org.jetbrains.compose.web.css.px


/*
@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    val context = rememberPageContext()
    val scope = rememberCoroutineScope()
    var overFlowOpened by remember {
        mutableStateOf(false)
    }
    var apiResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }

    var showMoreOpened by remember {
        mutableStateOf(false)
    }
    val searchedPosts = remember {
        mutableStateListOf<PostWithoutDetails>()
    }
    val breakPoint = rememberBreakpoint()
    var postsToSkip by remember {
        mutableStateOf(0)
    }
    val hasCategoryParams = remember(key1 = context.route)
    {
        context.route.params.containsKey("category")
    }
    val hasQueryParams = remember(key1 = context.route)
    {
        context.route.params.containsKey("query")
    }
    val value = remember(key1 = context.route) {
        if (hasCategoryParams) {
            context.route.params.getValue("category")
        } else if (hasQueryParams) {
            context.route.params.getValue("query")
        } else {
            ""
        }
    }
    LaunchedEffect(key1 = context.route) {

        postsToSkip = 0
        showMoreOpened = false

        if (hasCategoryParams) {
            searchPostsByCategory(category = Category.valueOf(value),
                skip = postsToSkip, onSuccess = { response ->
                    if (response is ApiListResponse.Success) {
                        apiResponse = response
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += 8
                        if (searchedPosts.size >= 8) {
                            showMoreOpened = true
                        }
                    }
                }, onError = {

                })
        } else if (hasQueryParams) {
            searchPostsByTitle(query = value,
                skip = postsToSkip, onSuccess = { response ->
                    if (response is ApiListResponse.Success) {
                        apiResponse = response
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += 8
                        if (searchedPosts.size >= 8) {
                            showMoreOpened = true
                        }
                    }
                }, onError = {

                })
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (overFlowOpened) {
            overFlowSidePanel(onMenuClose = { overFlowOpened = false }, content = {
                CategoryNavigationItem(
                    true,
                    selectedCategory = if (hasCategoryParams) Category.valueOf(value) else null
                )
            })
        }
        HeaderSection(breakpoint = breakPoint, onMenuOpen = {
            overFlowOpened = true
        }, selectedCategory = Category.valueOf(value), logo = Res.Image.logo)
        if (hasCategoryParams) {
            SpanText(
                value,
                modifier = Modifier.fillMaxWidth().fontSize(18.px).fontWeight(FontWeight.Bold)
                    .fontFamily(FONT_FAMILY)
                    .margin(topBottom = 50.px).textAlign(TextAlign.Center)
            )
        }
        if (apiResponse is ApiListResponse.Success) {
            PostSection(breakpoint = breakPoint,
                posts = searchedPosts,
                title = "",
                showMoreVisibility = showMoreOpened,
                showMore = {
                    scope.launch {
                        if (hasCategoryParams) {
                            searchPostsByCategory(
                                category = Category.valueOf(value),
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < 8) {
                                                showMoreOpened = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += 8
                                        } else {
                                            showMoreOpened = false
                                        }
                                    }
                                },
                                onError = {


                                })

                        } else if (hasQueryParams) {
                            searchPostsByTitle(
                                query = value,
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            if (response.data.size < 8) {
                                                showMoreOpened = false
                                            }
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += 8
                                        } else {
                                            showMoreOpened = false
                                        }
                                    }
                                },
                                onError = {


                                })
                        }
                    }

                },
                onClick = {
                    context.router.navigateTo(Screen.PostPage.getPost(id = it))
                })

        } else {
            LoadingIndicator()
        }
    }
}*/
