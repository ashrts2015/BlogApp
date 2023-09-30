package org.example.blogmultiplatform.pages.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.example.blogmultiplatform.components.PostView
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.Theme
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(breakpoint: Breakpoint, posts: ApiListResponse, onClick: (String) -> Unit) {


    Box(
        modifier = Modifier.fillMaxWidth().backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    )
    {
        Box(
            modifier = Modifier.fillMaxWidth().maxWidth(1920.px)
                .backgroundColor(Theme.Secondary.rgb),
            contentAlignment = Alignment.Center
        )
        {
            when (posts) {
                is ApiListResponse.Error -> {

                }

                ApiListResponse.Idle -> {

                }

                is ApiListResponse.Success -> {
                    MainPosts(breakpoint, posts.data, onClick)
                }
            }
        }
    }
}

@Composable
fun MainPosts(breakpoint: Breakpoint, posts: List<PostWithoutDetails>, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
            .margin(topBottom = 50.px)
    )
    {
        if (breakpoint == Breakpoint.XL) {
            PostView(
                post = posts.first(),
                darkTheme = true,
                thumbNailHeight = 640.px,
                onClick = { posts.first()._id })


            Column(modifier = Modifier.fillMaxWidth(80.percent).margin(left = 20.px))
            {
                posts.drop(1).forEach { post ->
                    PostView(
                        post = post,
                        darkTheme = true,
                        vertical = false,
                        thumbNailHeight = 200.px,
                        titleMaxLines = 1,
                        onClick = {
                            onClick(post._id)
                        }
                    )
                }

            }
        } else if (breakpoint >= Breakpoint.LG) {
            Box(modifier = Modifier.margin(right = 20.px)) {
                PostView(
                    post = posts.first(),
                    darkTheme = true,
                    onClick = { onClick(posts.first()._id) })

            }
            PostView(post = posts[1], darkTheme = true, onClick = { onClick(posts[1]._id) })


        } else {
            PostView(
                post = posts.first(),
                darkTheme = true,
                thumbNailHeight = 640.px, onClick = { onClick(posts.first()._id) }
            )

        }

    }
}