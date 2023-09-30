package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.components.PostsView
import org.example.blogmultiplatform.components.SearchBar
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.POSTS_PER_PAGE
import org.example.blogmultiplatform.util.Constants.QUERY_PARAM
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.Id
import org.example.blogmultiplatform.util.deleteSelectedPosts
import org.example.blogmultiplatform.util.fetchMyPosts
import org.example.blogmultiplatform.util.isLoggedIn
import org.example.blogmultiplatform.util.noBorder
import org.example.blogmultiplatform.util.parseSwitchText
import org.example.blogmultiplatform.util.searchPostsByTitle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement

@Page
@Composable
fun MyPostsPage() {
    isLoggedIn {

        MyPostScreen()
    }


}

@Composable
fun MyPostScreen() {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()

    var selectable by remember {
        mutableStateOf(false)
    }
    var showMoreVisibity by remember {
        mutableStateOf(false)
    }

    val selectedPosts = remember {
        mutableListOf<String>()
    }

    var postsToSkip by remember {
        mutableStateOf(0)
    }
    var switchText by remember {
        mutableStateOf("Select")
    }


    val myPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val hasParams = remember(key1 = context.route) { context.route.params.containsKey(QUERY_PARAM) }
    val query = remember(key1 = context.route) { context.route.params[QUERY_PARAM] ?: "" }

    LaunchedEffect(context.route)
    {
        postsToSkip = 0
        if (hasParams) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value =
                query.replace("%20", " ")
            searchPostsByTitle(query = query,
                skip = postsToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postsToSkip += POSTS_PER_PAGE
                        showMoreVisibity = it.data.size >= POSTS_PER_PAGE

                    }

                },
                onError = {

                })

        } else {
            fetchMyPosts(
                skip = postsToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        if (it.data.isNotEmpty()) {
                            myPosts.clear()
                            myPosts.addAll(it.data)
                            postsToSkip += POSTS_PER_PAGE
                            showMoreVisibity = it.data.size >= POSTS_PER_PAGE

                        } else {
                            println("No List added")
                        }
                    }
                },
                onError = {
                    println(it.message.toString())

                }
            )
        }

    }
    AdminPageLayout {

        Column(
            modifier = Modifier.fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(
                modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 30.percent else 50.percent),
                contentAlignment = Alignment.Center
            )
            {
                SearchBar(
                    modifier = Modifier.visibility(if (selectable) Visibility.Hidden else Visibility.Visible)
                        .transition(
                            CSSTransition(
                                property = TransitionProperty.All,
                                duration = 200.ms
                            )
                        ),
                    onEnterClick = {

                        val searchInput =
                            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value
                        if (searchInput.isNotEmpty()) {
                            context.router.navigateTo(Screen.AdminMyPosts.searchByTitle(query = searchInput))
                        } else {
                            context.router.navigateTo(Screen.AdminMyPosts.route)
                        }
                    }, breakpoint = breakpoint, onSearchIconClick = {})
            }
            Row(
                modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        size = SwitchSize.MD,
                        checked = selectable,
                        onCheckedChange = {
                            selectable = it
                            switchText = if (!selectable) {
                                selectedPosts.clear()
                                "Select"
                            } else {
                                "0 Posts Selected"

                            }
                        })
                    SpanText(
                        modifier = Modifier.color(if (selectable) Colors.Black else Theme.HalfBlack.rgb),
                        text = switchText
                    )

                }
                Button(
                    attrs = Modifier
                        .margin(right = 20.px)
                        .height(54.px)
                        .padding(leftRight = 24.px)
                        .backgroundColor(Theme.Red.rgb)
                        .color(Colors.White)
                        .noBorder()
                        .borderRadius(r = 4.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.Medium)
                        //.visibility(if (selectedPosts.isNotEmpty()) Visibility.Visible else Visibility.Hidden)
                        .onClick {
                            scope.launch {
                                val result = deleteSelectedPosts(ids = selectedPosts)
                                if (result) {
                                    selectable = false
                                    switchText = "Select"
                                    postsToSkip -= selectedPosts.size
                                    selectedPosts.forEach { deletedPostId ->
                                        myPosts.removeAll {
                                            it._id == deletedPostId
                                        }
                                    }
                                    selectedPosts.clear()
                                }
                            }
                        }
                        .toAttrs()
                ) {
                    SpanText(text = "Delete")
                }
            }

            PostsView(
                myPosts,
                breakpoint,
                onShowMoreClicked = {

                    scope.launch {
                        println("clicked")
                        if (hasParams) {
                            searchPostsByTitle(
                                query = query,
                                skip = postsToSkip,
                                onSuccess = {
                                    if (it is ApiListResponse.Success) {
                                        if (it.data.isNotEmpty()) {
                                            myPosts.addAll(it.data)
                                            postsToSkip += POSTS_PER_PAGE
                                            showMoreVisibity = it.data.size >= POSTS_PER_PAGE
                                            if (it.data.size <= POSTS_PER_PAGE) showMoreVisibity =
                                                false

                                        } else {
                                            showMoreVisibity = false
                                        }
                                    }
                                },
                                onError = {
                                    println(it.message.toString())

                                }
                            )

                        } else {
                            fetchMyPosts(
                                skip = postsToSkip,
                                onSuccess = {
                                    if (it is ApiListResponse.Success) {
                                        if (it.data.isNotEmpty()) {
                                            myPosts.addAll(it.data)
                                            postsToSkip += POSTS_PER_PAGE
                                            showMoreVisibity = it.data.size >= POSTS_PER_PAGE
                                            if (it.data.size <= POSTS_PER_PAGE) showMoreVisibity =
                                                false

                                        } else {
                                            showMoreVisibity = false
                                        }
                                    }
                                },
                                onError = {
                                    println(it.message.toString())

                                }
                            )
                        }


                    }
                },
                showMoreVisibility = showMoreVisibity,
                selectable = selectable,
                onSelect = {
                    selectedPosts.add(it)
                    println(selectedPosts.size)
                    switchText = parseSwitchText(selectedPosts)


                },
                onDeselect = {
                    selectedPosts.remove(it)
                    switchText = parseSwitchText(selectedPosts)

                },
                onClick = { it ->
                    context.router.navigateTo(Screen.AdminCreate.passPostId(it))

                })

        }

    }
}