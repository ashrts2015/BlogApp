package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.models.User
import org.example.blogmultiplatform.models.UserWithoutPassword
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.styles.LoginInputStyle
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Id.passwordInput
import org.example.blogmultiplatform.util.Id.userNameInput
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.checkUserExistence
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set


@Page
@Composable
fun LoginScreen() {

    val scope = rememberCoroutineScope()
    val context = rememberPageContext()
    var errorText by remember {
        mutableStateOf(" ")
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        Column(
            modifier = Modifier.padding(leftRight = 50.px, top = 80.px, bottom = 24.px)
                .background(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                src = Res.Image.logo,
                desc = "logo_image",
                modifier = Modifier.width(100.px).margin(bottom = 50.px)
            )

            org.jetbrains.compose.web.dom.Input(
                type = InputType.Text,
                attrs = LoginInputStyle.toModifier()
                    .id(userNameInput)

                    .width(350.px).height(54.px)
                    .padding(leftRight = 20.px)
                    .margin(bottom = 12.px)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .background(Colors.White)
                    .outline(width = 0.px, style = LineStyle.None, color = Colors.Transparent)

                    .toAttrs {
                        attr("placeholder", "Username")
                    }
            )
            org.jetbrains.compose.web.dom.Input(
                type = InputType.Password,
                attrs = LoginInputStyle.toModifier()
                    .id(passwordInput)

                    .width(350.px).height(54.px)
                    .padding(leftRight = 20.px)
                    .margin(bottom = 12.px)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .background(Colors.White)
                    .outline(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                    .toAttrs {
                        attr("placeholder", "Password")
                    }
            )
            Button(
                attrs = Modifier
                    .width(350.px)
                    .height(54.px)
                    .margin(bottom = 24.px)
                    .background(Theme.Primary.rgb)
                    .color(Colors.White)
                    .borderRadius(4.px)
                    .fontWeight(FontWeight.Medium)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .cursor(Cursor.Pointer)
                    .outline(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                    .border(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                    .onClick {
                        scope.launch {
                            val userName =
                                (document.getElementById(userNameInput) as HTMLInputElement).value
                            val password =
                                (document.getElementById(passwordInput) as HTMLInputElement).value
                            if (userName.isNotEmpty() && password.isNotEmpty()) {
                                val user = checkUserExistence(
                                    User(
                                        userName = userName,
                                        password = password
                                    )
                                )
                                if (user != null) {
                                    rememberLoggedIn(true,user)
                                    context.router.navigateTo(Screen.AdminHome.route)
                                } else {
                                    errorText = "User Doesn't exists"
                                    delay(3000)
                                    errorText = ""
                                }


                            } else {
                                errorText = "Input Fields are Empty"
                                delay(300)
                                errorText = " "
                            }
                        }

                    }
                    .toAttrs()
            ) {
                SpanText(text = "Sign in")
            }
            SpanText(
                text = errorText, modifier = Modifier.width(350.px).color(Colors.Red).textAlign(
                    TextAlign.Center
                )
            )

        }

    }


}

private fun rememberLoggedIn(remember:Boolean,user: UserWithoutPassword?=null)
{
    localStorage["remember"]=remember.toString()
    if(user!=null)
    {
        localStorage["userId"]=user._id.toString()
        localStorage["userName"]=user.userName


    }
}
