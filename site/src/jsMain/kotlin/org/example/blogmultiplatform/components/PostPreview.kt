package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.CategoryChip
import com.example.blogmultiplatform.styles.PostPreviewStyle
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.parseDateToString
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput


@Composable
fun PostView(
    modifier: Modifier = Modifier,
    post: PostWithoutDetails,
    selectable: Boolean = false,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick: (String) -> Unit = {},
    darkTheme: Boolean = false,
    vertical: Boolean = true,
    titleColor: CSSColorValue = Colors.Black,
    titleMaxLines: Int = 2,
    thumbNailHeight: CSSSizeValue<CSSUnit.px> = 320.px
) {
    var checked by remember(selectable) { mutableStateOf(false) }

    if (vertical) {
        Column(
            modifier = PostPreviewStyle.toModifier()
                .then(modifier)
                .fillMaxWidth(if (darkTheme) 95.percent else 90.percent)
                .margin(bottom = 24.px)
                .padding(all = if (selectable) 10.px else 0.px)
                .borderRadius(r = 4.px)
                .border(
                    width = if (selectable) 4.px else 0.px,
                    style = if (selectable) LineStyle.Solid else LineStyle.None,
                    color = if (checked) Theme.Primary.rgb else Theme.Gray.rgb
                )
                .cursor(Cursor.Pointer)
                .transition(CSSTransition(property = TransitionProperty.All, duration = 200.ms))
                .onClick {
                    if (selectable) {
                        checked = !checked
                        if (checked) {
                            onSelect(post._id.toString())
                        } else {
                            onDeselect(post._id)
                        }
                    } else {
                        onClick(post._id)
                    }
                }
        )
        {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectable = selectable,
                checked = checked, vertical = vertical,
                thumbNailHeight = thumbNailHeight,
                titleMaximumLength = titleMaxLines,
                titleColor = titleColor


            )
        }
    } else {
        Row(modifier = PostPreviewStyle.toModifier()
            .then(modifier).cursor(Cursor.Pointer).onClick {
            onClick(post._id)
        })
        {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectable = selectable,
                checked = checked,
                vertical = vertical,
                thumbNailHeight = thumbNailHeight,
                titleMaximumLength = titleMaxLines,
                titleColor = titleColor
            )
        }

    }


}

@Composable
fun PostContent(
    post: PostWithoutDetails,
    selectable: Boolean,
    darkTheme: Boolean,
    checked: Boolean,
    vertical: Boolean,
    thumbNailHeight: CSSSizeValue<CSSUnit.px>,
    titleMaximumLength: Int,
    titleColor: CSSColorValue,


) {
    Image(
        modifier = Modifier.fillMaxWidth()
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .height(thumbNailHeight)
            .objectFit(ObjectFit.Cover),
        src = post.thumbnail,
        desc = "post thumb image"
    )
    Column(
        modifier = Modifier
            .thenIf(
                condition = !vertical,
                other = Modifier.margin(left = 20.px)
            )
            .fillMaxWidth()
    )
    {
        SpanText(
            modifier = Modifier.fontSize(12.px).fontFamily(FONT_FAMILY)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.toLong().parseDateToString()
        )
        SpanText(
            modifier = Modifier.fontSize(16.px).fontFamily(FONT_FAMILY)
                .margin(bottom = 8.px)
                .color(if (darkTheme) Colors.White else titleColor)
                .fontWeight(FontWeight.Bold)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "$titleMaximumLength")
                    property("line-clamp", "$titleMaximumLength")
                    property("-webkit-box-orient", "vertical")


                },
            text = post.title
        )
        SpanText(
            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY)
                .color(if (darkTheme) Colors.White else Colors.Black)
                .fontWeight(FontWeight.Normal)
                .margin(bottom = 10.px)

                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("-webkit-box-orient", "vertical")


                },
            text = post.subtitle
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            CategoryChip(category = post.category, darkTheme)
            if (selectable) {
                CheckboxInput(checked = checked, attrs = Modifier.size(20.px).toAttrs {})
            }

        }
    }

}


