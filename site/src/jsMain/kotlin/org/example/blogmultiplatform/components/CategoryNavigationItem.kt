package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.toModifier
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.styles.CategoryItemStyle
import org.example.blogmultiplatform.util.Constants
import org.jetbrains.compose.web.css.px


@Composable
fun CategoryNavigationItem(vertical: Boolean, selectedCategory: Category?=null) {
    val context = rememberPageContext()
    Category.values().forEach { category ->
        Link(
            path = "",
            modifier = CategoryItemStyle.toModifier()
                .thenIf(condition = vertical, other = Modifier.margin(bottom = 24.px))
                .thenIf(condition = !vertical, other = Modifier.margin(right = 24.px))
                .thenIf(condition = selectedCategory==category, other = Modifier.color(Theme.Primary.rgb))
                .fontWeight(FontWeight.Medium)
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .textDecorationLine(TextDecorationLine.None).onClick {
                    context.router.navigateTo(Screen.SearchPage.searchByCategory(category))
                }, text = category.name
        )
    }
}