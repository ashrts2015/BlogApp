package org.example.blogmultiplatform.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.sections.NewsletterSection
import com.example.blogmultiplatform.sections.SponsoredPostsSection
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItem
import org.example.blogmultiplatform.components.overFlowSidePanel
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.pages.sections.HeaderSection
import org.example.blogmultiplatform.pages.sections.MainSection
import org.example.blogmultiplatform.pages.sections.PopularSection
import org.example.blogmultiplatform.pages.sections.PostSection
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.fetchLatestPosts
import org.example.blogmultiplatform.util.fetchMainPosts
import org.example.blogmultiplatform.util.fetchPopularPosts
import org.example.blogmultiplatform.util.fetchSponsoredPosts

@Page
@Composable
fun HomePage() {
    val breakpoint = rememberBreakpoint()
    val context= rememberPageContext()
    val scope = rememberCoroutineScope()
    var overFlowMenuOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val sponsoredPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val popularPosts = remember { mutableStateListOf<PostWithoutDetails>() }

    var latestPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }
    var popularPostsToSkip by remember { mutableStateOf(0) }
    var showMorePopular by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        fetchMainPosts(onSuccess = {
            mainPosts = it

        }, onError = {
            println(it)

        })

        fetchLatestPosts(latestPostsToSkip, onSuccess = {
            if (it is ApiListResponse.Success) {
                latestPosts.addAll(it.data)
                latestPostsToSkip += 8
                if (latestPosts.size >= 8) {
                    showMoreLatest = true
                }
            }

        }, onError = {

        })

        fetchSponsoredPosts(
            onSuccess = { response ->
                if (response is ApiListResponse.Success) {
                    if (response.data.isNotEmpty()) {

                        sponsoredPosts.addAll(response.data)

                    }
                }

            }, onError = {

            })
        fetchLatestPosts(latestPostsToSkip, onSuccess = {
            if (it is ApiListResponse.Success) {
                latestPosts.addAll(it.data)
                latestPostsToSkip += 8
                if (latestPosts.size >= 8) {
                    showMoreLatest = true
                }
            }

        }, onError = {

        })
        fetchPopularPosts(popularPostsToSkip, onSuccess = {
            if (it is ApiListResponse.Success) {
                popularPosts.addAll(it.data)
                popularPostsToSkip += 8
                if (popularPosts.size >= 8) {
                    showMorePopular = true
                }
            }

        }, onError = {

        })
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overFlowMenuOpened) {
            overFlowSidePanel(onMenuClose = { overFlowMenuOpened = false }, content = {
                CategoryNavigationItem(true)
            })
        }
        HeaderSection(breakpoint = breakpoint, onMenuOpen = { overFlowMenuOpened = true })
        MainSection(breakpoint, mainPosts, onClick = {
            context.router.navigateTo(Screen.PostPage.getPost(id = it))
        })
        PostSection(breakpoint,
            latestPosts,
            title = "Latest",
            showMoreVisibility = showMoreLatest,
            showMore = {
                scope.launch {
                    fetchLatestPosts(skip = latestPostsToSkip, onSuccess = { response ->
                        if (response is ApiListResponse.Success) {
                            if (response.data.isNotEmpty()) {
                                if (response.data.size < 8) {
                                    showMoreLatest = false
                                }
                                latestPosts.addAll(response.data)
                                latestPostsToSkip += 8
                            } else {
                                showMoreLatest = false
                            }
                        }
                    }, onError = {

                    })

                }


            },
            onClick = {
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            })
        SponsoredPostsSection(breakpoint = breakpoint, posts = sponsoredPosts, onClick = {
            context.router.navigateTo(Screen.PostPage.getPost(id = it))
        })
        PopularSection(breakpoint = breakpoint, popularPosts,
            title = "Popular",
            showMoreVisibility = showMorePopular,
            showMore = {
                scope.launch {
                    fetchPopularPosts(skip = popularPostsToSkip, onSuccess = { response ->
                        if (response is ApiListResponse.Success) {
                            if (response.data.isNotEmpty()) {
                                if (response.data.size < 8) {
                                    showMorePopular = false
                                }
                                popularPosts.addAll(response.data)
                                popularPostsToSkip += 8
                            } else {
                                showMorePopular = false
                            }
                        }
                    }, onError = {

                    })

                }


            },
            onClick = {
                context.router.navigateTo(Screen.PostPage.getPost(id = it))
            })
        NewsletterSection(breakpoint)

    }


}
