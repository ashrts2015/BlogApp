package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.Path
import com.varabyte.kobweb.compose.dom.Svg
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.styles.NavigationItemStyle
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.logout
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh


@Composable
fun SidePanel(onMenuClick: () -> Unit) {
    val breakPoint = rememberBreakpoint()
    if (breakPoint > Breakpoint.MD) {
        SidePanelInternal()
    } else {
        collapsedSidePanel {
            onMenuClick()
        }
    }

}

@Composable
private fun SidePanelInternal() {
    val context = rememberPageContext()
    Column(
        modifier = Modifier.background(Theme.Secondary.rgb)
            .width(SIDE_PANEL_WIDTH.px)
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(9)
            .padding(leftRight = 40.px, topBottom = 50.px)
    )
    {
        Image(
            src = Res.Image.logo,
            desc = "",
            modifier = Modifier.bottom(60.px).width(100.px).height(50.px).margin(bottom = 30.px)
                .onClick {
                }
        )
        NavigationItems()


    }
}


@Composable
fun NavigationItems() {
    val context = rememberPageContext()
    SpanText(
        text = "Dash Board",
        modifier = Modifier.fontFamily(FONT_FAMILY).fontSize(14.px).margin(bottom = 30.px)
            .color(Theme.HalfWhite.rgb)
    )
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "Home",
        onSelected = context.route.path == Screen.AdminHome.route,
        icon = Res.PathIcon.home,
        onClick = {
            context.router.navigateTo(Screen.AdminHome.route)

        })
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "Create Post",
        icon = Res.PathIcon.create,
        onSelected = context.route.path == Screen.AdminCreate.route,
        onClick = {
            context.router.navigateTo(Screen.AdminCreate.route)

        })
    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        title = "My Posts",
        onSelected = context.route.path == Screen.AdminMyPosts.route,
        icon = Res.PathIcon.posts,
        onClick = {
            context.router.navigateTo(Screen.AdminMyPosts.route)

        })
    NavigationItem(title = "Logout", icon = Res.PathIcon.logout, onClick = {
        logout()
        context.router.navigateTo(Screen.AdminLogin.route)
    })

}

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    onSelected: Boolean = false,
    onClick: () -> Unit,
    title: String,
    icon: String
) {

    Row(
        modifier = NavigationItemStyle.toModifier().then(modifier)
            .cursor(Cursor.Pointer).onClick {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        VectorIcon(
            modifier = Modifier.margin(right = 10.px),
            pathData = icon,
            selected = onSelected
        )
        SpanText(
            modifier = Modifier
                .id("navigationText")
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .thenIf(condition = onSelected, other = Modifier.color(Theme.Primary.rgb)),
            text = title
        )
    }


}

@Composable
private fun VectorIcon(
    modifier: Modifier = Modifier,
    pathData: String,
    selected: Boolean
) {
    Svg(
        attrs = modifier
            .id("svgParent")
            .width(24.px)
            .height(24.px)
            .toAttrs {
                attr("viewBox", "0 0 24 24")
                attr("fill", "none")
            }
    ) {
        Path(
            attrs = Modifier
                .id("vectorIcon")
                .thenIf(condition = selected,
                    other = Modifier.styleModifier {
                        property("stroke", Theme.Primary.hex)
                    })
                .toAttrs {
                    attr("d", pathData)
                    attr("stroke-width", "2")
                    attr("stroke-linecap", "round")
                    attr("stroke-linejoin", "round")
                }
        )
    }
}


@Composable
fun collapsedSidePanel(onMenuClick: () -> Unit) {
    val context = rememberPageContext()
    Row(
        modifier = Modifier.fillMaxWidth().height(100.px).padding(leftRight = 25.px)
            .background(Theme.Secondary.rgb), verticalAlignment = Alignment.CenterVertically
    ) {
        FaBars(
            modifier = Modifier.margin(24.px).color(Colors.White).cursor(Cursor.Pointer)
                .onClick { onMenuClick() }, size = IconSize.X1
        )
        Image(
            src = Res.Image.logo,
            desc = "",
            modifier = Modifier.width(100.px).height(60.px))

    }

}


@Composable
fun overFlowSidePanel(onMenuClose: () -> Unit, content: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    val context= rememberPageContext()
    var translatex by remember { mutableStateOf((-100).percent) }
    var opacity by remember { mutableStateOf(0.percent) }

    LaunchedEffect(key1 = breakpoint)
    {
        translatex = 0.percent
        opacity = 100.percent
        if (breakpoint > Breakpoint.MD) {
            translatex = (-100).percent
            opacity = 0.percent
            delay(500)
            onMenuClose()
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth().height(100.vh).position(Position.Fixed).zIndex(9)
            .backgroundColor(Theme.HalfBlack.rgb).opacity(opacity)
            .transition(CSSTransition(property = "opacity", duration = 300.ms))
    )
    {
        Column(
            modifier = Modifier.padding(all = 24.px)
                .fillMaxHeight()
                .width(if (breakpoint < Breakpoint.MD) 50.percent else 25.percent)
                .translateX(translatex)
                .transition(CSSTransition(property = "translate", duration = 300.ms))
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)

                .backgroundColor(Theme.Secondary.rgb)
        ) {

            Row(
                modifier = Modifier.margin(bottom = 60.px),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                FaXmark(
                    size = IconSize.LG,
                    modifier = Modifier.onClick {
                        scope.launch {
                            translatex = (-100).percent
                            opacity = 0.percent
                            delay(500)
                            onMenuClose()

                        }
                    }.margin(right = 20.px)
                        .cursor(Cursor.Pointer)
                        .color(Colors.White)
                )
                Image(
                    src = Res.Image.logo,
                    desc = "",
                    modifier = Modifier.width(80.px).height(60.px).cursor(Cursor.Pointer).onClick {
                        context.router.navigateTo(Screen.HomePage.route)
                    }
                )
            }
            content()

        }
    }
}