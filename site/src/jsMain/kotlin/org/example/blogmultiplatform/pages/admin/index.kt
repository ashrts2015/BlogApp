package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.LoadingIndicator
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.models.RandomJoke
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.PAGE_WIDTH
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.fetchRandomJoke
import org.example.blogmultiplatform.util.isLoggedIn
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Page
@Composable
fun HomePage() {
    isLoggedIn {

        HomeScreen()
    }


}

@Composable
fun HomeScreen() {

    var randomJoke: RandomJoke? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit)
    {
        fetchRandomJoke {
            randomJoke = it
        }
    }
    AdminPageLayout {
        HomeContent(
            joke = randomJoke
        )
        AddButton()
    }
}

@Composable
fun HomeContent(joke: RandomJoke?) {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
        contentAlignment = Alignment.BottomEnd
    )
    {
        if (joke != null) {

            Column(
                modifier = Modifier.fillMaxSize().padding(topBottom = 50.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (joke.id != -1) {
                    Image(
                        src = Res.Image.laugh,
                        desc = "laugh_iamge",
                        modifier = Modifier.size(150.px).margin(bottom = 24.px)
                    )
                    if (joke.joke.contains("Q:")) {
                        SpanText(
                            modifier = Modifier
                                .margin(bottom = 14.px)
                                .fillMaxWidth(40.percent).textAlign(TextAlign.Center)
                                .color(Theme.Secondary.rgb)
                                .fontSize(28.px)
                                .fontFamily(FONT_FAMILY)
                                .fontWeight(FontWeight.Bold),
                            text = joke.joke.split(":")[1].dropLast(1)
                        )
                        SpanText(
                            modifier = Modifier
                                .fillMaxWidth(40.percent).textAlign(TextAlign.Center)
                                .color(Theme.HalfBlack.rgb)
                                .fontSize(20.px)
                                .fontFamily(FONT_FAMILY)
                                .fontWeight(FontWeight.Normal),
                            text = joke.joke.split(":").last()
                        )
                    } else {
                        SpanText(
                            modifier = Modifier
                                .margin(bottom = 14.px)
                                .fillMaxWidth(40.percent).textAlign(TextAlign.Center)
                                .color(Theme.Secondary.rgb)
                                .fontSize(14.px)
                                .fontFamily(FONT_FAMILY)
                                .fontWeight(FontWeight.Bold),
                            text = joke.joke
                        )
                    }
                }

            }
        } else {
            LoadingIndicator()
            println("Loading...")
        }
    }

}

@Composable
fun AddButton() {
    val breakpoint = rememberBreakpoint()
    val context = rememberPageContext()
    Box(
        modifier = Modifier
            .height(100.vh)
            .fillMaxWidth()
            .maxWidth(PAGE_WIDTH.px)
            .styleModifier {
                property("pointer-events", "none")
            }
            .position(Position.Fixed), contentAlignment = Alignment.BottomEnd
    )
    {
        Box(modifier = Modifier
            .margin(
                right = if (breakpoint > Breakpoint.MD) 40.px else 20.px,
                bottom = if (breakpoint > Breakpoint.MD) 40.px else 20.px
            )
            .size(if (breakpoint > Breakpoint.MD) 80.px else 50.px)
            .styleModifier {
                property("pointer-events", "auto")
            }
            .borderRadius(r = 14.px)
            .cursor(Cursor.Pointer).onClick {
                context.router.navigateTo(Screen.AdminCreate.route)
            }
            .backgroundColor(Theme.Primary.rgb), contentAlignment = Alignment.Center)
        {
            FaPlus(size = IconSize.LG, modifier = Modifier.color(Colors.White))

        }
    }

}