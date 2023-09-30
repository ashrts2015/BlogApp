package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun PostsView(
    post: List<PostWithoutDetails>,
    breakpoint: Breakpoint,
    title: String? = null,
    onShowMoreClicked: () -> Unit,
    showMoreVisibility: Boolean,
    selectable: Boolean = false,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent),
        verticalArrangement = Arrangement.Center
    )
    {

        if (!title.isNullOrEmpty()) {
            SpanText(
                text = title,
                modifier = Modifier.margin(topBottom = 24.px).fontSize(16.px).fontFamily(
                    FONT_FAMILY
                ).fontWeight(FontWeight.Medium)
            )

        }
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2, md = 3, lg = 4)
        )
        {
            post.forEach { it ->
                // SpanText(it.title)
                PostView(
                    post=it,
                    selectable = selectable,
                    onSelect = { id ->
                        onSelect(id)
                    },
                    onClick = {
                        onClick(it)
                    },
                    onDeselect = { id ->
                        onDeselect(id)

                    })
            }
        }
        SpanText(
            text = "ShowMore",
            modifier = Modifier
                .fillMaxWidth()
                .margin(topBottom = 50.px)
                .textAlign(TextAlign.Center)
                .fontWeight(FontWeight.Medium)
                .fontSize(14.px)
                .fontFamily(FONT_FAMILY)
                .color(Colors.Black)
                .cursor(Cursor.Pointer)
                .visibility(if (showMoreVisibility) Visibility.Visible else Visibility.Hidden)
                .onClick {
                    onShowMoreClicked()
                }

        )
    }
}