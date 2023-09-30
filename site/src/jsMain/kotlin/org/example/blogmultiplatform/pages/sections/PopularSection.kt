package org.example.blogmultiplatform.pages.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.example.blogmultiplatform.components.PostsView
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.jetbrains.compose.web.css.px

@Composable
fun PopularSection(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    title: String,
    showMoreVisibility: Boolean,
    showMore: () -> Unit,
    onClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().maxWidth(1920.px).cursor(Cursor.Pointer), contentAlignment = Alignment.Center)
    {
        PostsView(
            breakpoint = breakpoint,
            post = posts,
            title = title,
            showMoreVisibility = showMoreVisibility,
            onShowMoreClicked = showMore,
            onClick = onClick
        )
    }

}