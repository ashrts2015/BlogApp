package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.components.ControlPopup
import com.example.blogmultiplatform.components.MessagePopup
import com.example.blogmultiplatform.models.ControlStyle
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.styles.EditorKeyStyle
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.models.ApiResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.POST_ID_PARAM
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.Id
import org.example.blogmultiplatform.util.Id.subtitleInput
import org.example.blogmultiplatform.util.Id.titleInput
import org.example.blogmultiplatform.util.addPost
import org.example.blogmultiplatform.util.applyControlStyle
import org.example.blogmultiplatform.util.applyStyle
import org.example.blogmultiplatform.util.fetchSelectedPosts
import org.example.blogmultiplatform.util.getEditor
import org.example.blogmultiplatform.util.getSelectedText
import org.example.blogmultiplatform.util.isLoggedIn
import org.example.blogmultiplatform.util.noBorder
import org.example.blogmultiplatform.util.updatePost
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import kotlin.js.Date


data class CreatePageUiState(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var thumbnail: String = "",
    var thumbnailInputDisabled: Boolean = false,
    var content: String = "",
    var category: Category = Category.Programming,
    var buttonText: String = "Create",
    var popular: Boolean = false,
    var main: Boolean = false,
    var sponsored: Boolean = false,
    var editorVisibility: Boolean = true,
    var messagePopup: Boolean = false,
    var linkPopup: Boolean = false,
    var imagePopup: Boolean = false
) {
    fun reset() = this.copy(
        id = "",
        title = "",
        subtitle = "",
        thumbnail = "",
        content = "",
        category = Category.Programming,
        buttonText = "Create",
        main = false,
        popular = false,
        sponsored = false,
        editorVisibility = true,
        messagePopup = false,
        linkPopup = false,
        imagePopup = false
    )
}

@Page
@Composable
fun CreatePage() {
    isLoggedIn {

        CreateScreen()
    }


}

@Composable
fun CreateScreen() {
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var context = rememberPageContext()
    var uiState by remember {
        mutableStateOf(CreatePageUiState())
    }

    val hasPostIdParam = remember(key1 = context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }


    LaunchedEffect(key1 = context.route)
    {
        if (hasPostIdParam) {
            val postId = context.route.params[POST_ID_PARAM] ?: ""
            val response = fetchSelectedPosts(postId)
            if (response is ApiResponse.Success) {
                (document.getElementById(Id.editor) as HTMLTextAreaElement).value =
                    response.data.content
                uiState = uiState.copy(
                    id = response.data._id,
                    title = response.data.title,
                    subtitle = response.data.subtitle,
                    content = response.data.content,
                    category = response.data.category,
                    thumbnail = response.data.thumbnail,
                    buttonText = "Update",
                    main = response.data.main,
                    popular = response.data.popular,
                    sponsored = response.data.sponsored
                )
            }
        }
        else
        {
            (document.getElementById(Id.editor) as HTMLTextAreaElement).value = ""
            uiState = uiState.reset()
        }

    }

    AdminPageLayout {
        Box(
            modifier = Modifier.fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier.fillMaxSize().maxWidth(700.px),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                SimpleGrid(numColumns = numColumns(base = 1, sm = 3))
                {
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ), verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Switch(
                            checked = uiState.popular,
                            onCheckedChange = { uiState = uiState.copy(popular = it) },
                            modifier = Modifier.margin(right = 8.px),
                            size = SwitchSize.LG
                        )
                        SpanText(
                            text = "Popular",
                            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb)
                        )

                    }
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ), verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Switch(
                            checked = uiState.main,
                            onCheckedChange = { uiState = uiState.copy(main = it) },
                            modifier = Modifier.margin(right = 8.px),
                            size = SwitchSize.LG
                        )
                        SpanText(
                            text = "Main",
                            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb)
                        )

                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Switch(
                            checked = uiState.sponsored,
                            onCheckedChange = { uiState = uiState.copy(sponsored = it) },
                            modifier = Modifier.margin(right = 8.px),
                            size = SwitchSize.LG
                        )
                        SpanText(
                            text = "Sponsored",
                            modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb)
                        )

                    }

                }

                org.jetbrains.compose.web.dom.Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .id(titleInput)
                        .fillMaxWidth().height(54.px)
                        .padding(leftRight = 20.px)
                        .margin(topBottom = 12.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .background(Theme.LightGray.rgb)
                        .noBorder()

                        .toAttrs {
                            attr("placeholder", "Title")
                            attr("value", uiState.title)

                        }
                )
                org.jetbrains.compose.web.dom.Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .id(subtitleInput)
                        .fillMaxWidth().height(54.px)
                        .padding(leftRight = 20.px)
                        .margin(bottom = 12.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .background(Theme.LightGray.rgb)
                        .noBorder()

                        .toAttrs {
                            attr("placeholder", "Subtitle")
                            attr("value", uiState.subtitle)

                        }
                )

                CategoryDropdown(selectedCategory = uiState.category, onCategorySelect = {
                    uiState = uiState.copy(category = it)
                })
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .margin(
                            bottom = 12.px
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Switch(
                        checked = uiState.thumbnailInputDisabled,
                        onCheckedChange = {
                            uiState = uiState.copy(thumbnailInputDisabled = it)
                        },
                        modifier = Modifier.margin(right = 8.px),
                        size = SwitchSize.MD
                    )
                    SpanText(
                        text = "Paste an image URL instead",
                        modifier = Modifier.fontSize(14.px).fontFamily(FONT_FAMILY)
                            .color(Theme.HalfBlack.rgb)
                    )

                }

                ThumbnailUploader(
                    thumbnail = uiState.thumbnail,
                    thumbnailInputDisabled = uiState.thumbnailInputDisabled,
                    onThumbnailSelect = { fileName, file ->
                        (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value =
                            fileName
                        uiState = uiState.copy(thumbnail = file)


                    })
                EditorControls(
                    breakpoint,
                    editorVisibility = uiState.editorVisibility,
                    onEditorVisibility = {
                        uiState = uiState.copy(editorVisibility = !uiState.editorVisibility)
                    }, onLinkClick = {
                        uiState = uiState.copy(linkPopup = true)

                    }, onImageClick = {
                        uiState = uiState.copy(imagePopup = true)

                    })
                Editor(uiState.editorVisibility)
                CreateButton(text =uiState.buttonText, onClick = {

                    uiState =
                        uiState.copy(title = (document.getElementById(Id.titleInput) as HTMLInputElement).value)
                    uiState =
                        uiState.copy(subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value)
                    uiState =
                        uiState.copy(content = (document.getElementById(Id.editor) as HTMLTextAreaElement).value)
                    if (uiState.thumbnailInputDisabled) {
                        uiState =
                            uiState.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                    }

                    if (uiState.title.isNotEmpty() &&
                        uiState.subtitle.isNotEmpty() &&
                        uiState.thumbnail.isNotEmpty() &&
                        uiState.content.isNotEmpty()
                    ) {
                        scope.launch {

                            if(hasPostIdParam)
                            {
                                val result = updatePost(
                                    Post(
                                        _id=uiState.id,
                                        title = uiState.title,
                                        subtitle = uiState.subtitle,
                                        thumbnail = uiState.thumbnail,
                                        content = uiState.content,
                                        category = uiState.category,
                                        popular = uiState.popular,
                                        main = uiState.main,
                                        sponsored = uiState.sponsored
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.AdminSuccess.updatedPost())
                                }
                            }
                            else
                            {
                                val result = addPost(
                                    Post(
                                        author = localStorage["userName"].toString(),
                                        title = uiState.title,
                                        subtitle = uiState.subtitle,
                                        date = Date.now(),
                                        thumbnail = uiState.thumbnail,
                                        content = uiState.content,
                                        category = uiState.category,
                                        popular = uiState.popular,
                                        main = uiState.main,
                                        sponsored = uiState.sponsored
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.AdminSuccess.route)
                                }
                            }

                        }

                    } else {
                        scope.launch {
                            uiState = uiState.copy(messagePopup = true)
                            delay(2000)
                            uiState = uiState.copy(messagePopup = false)
                        }
                    }

                }
                )


            }


        }
    }
    if (uiState.messagePopup) {
        MessagePopup(
            message = "Please fill out all fields.",
            onDialogDismiss = { uiState = uiState.copy(messagePopup = false) }
        )
    }
    if (uiState.linkPopup) {
        ControlPopup(
            editorControl = EditorControl.Link,
            onDialogDismiss = { uiState = uiState.copy(linkPopup = false) },
            onAddClick = { href, title ->
                applyStyle(
                    ControlStyle.Link(
                        selectedText = getSelectedText(),
                        href = href,
                        title = title
                    )
                )
            }
        )
    }
    if (uiState.imagePopup) {
        ControlPopup(
            editorControl = EditorControl.Image,
            onDialogDismiss = { uiState = uiState.copy(imagePopup = false) },
            onAddClick = { imageUrl, description ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(),
                        imageUrl = imageUrl,
                        desc = description
                    )
                )
            }
        )
    }
}

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onEditorVisibility: () -> Unit,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit

) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2)
        ) {
            Row(
                modifier = Modifier.height(54.px).backgroundColor(Theme.LightGray.rgb)
                    .borderRadius(4.px)
            ) {
                EditorControl.values().forEach {
                    EditorKeyView(it) {
                        applyControlStyle(
                            it,
                            onLinkClick = { onLinkClick() },
                            onImageClick = { onImageClick() })
                    }
                }
            }

            Box(contentAlignment = Alignment.CenterEnd) {
                Button(
                    attrs = Modifier
                        .height(54.px)

                        .thenIf(
                            condition = breakpoint < Breakpoint.SM,
                            other = Modifier.fillMaxWidth()
                        )
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .onClick {
                            onEditorVisibility()
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }
                        .padding(leftRight = 24.px)
                        .borderRadius(4.px)
                        .backgroundColor(if (editorVisibility) Theme.LightGray.rgb else Theme.Primary.rgb)
                        .color(if (editorVisibility) Theme.DarkGray.rgb else Colors.White)
                        .noBorder()
                        .color(Theme.DarkGray.rgb)
                        .toAttrs()
                )
                {
                    SpanText(
                        text = "Preview", modifier = Modifier.fontFamily(FONT_FAMILY).fontWeight(
                            FontWeight.Medium

                        ).fontSize(14.px)
                            .color(if (editorVisibility) Theme.DarkGray.rgb else Colors.White)
                    )
                }
            }

        }
    }
}

@Composable
fun EditorKeyView(key: EditorControl, onClick: () -> Unit) {
    Box(
        modifier = EditorKeyStyle.toModifier().fillMaxHeight().padding(leftRight = 12.px)
            .borderRadius(4.px)
            .cursor(Cursor.Pointer).onClick {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Image(src = key.icon, desc = "")
    }
}


@Composable
fun Editor(editorVisibility: Boolean) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TextArea(
            attrs = Modifier
                .id(Id.editor)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .resize(Resize.None)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(
                    if (editorVisibility) Visibility.Visible
                    else Visibility.Hidden
                )
                .onKeyDown {
                    if (it.code == "Enter" && it.shiftKey) {
                        applyStyle(
                            controlStyle = ControlStyle.Break(
                                selectedText = getSelectedText()
                            )
                        )
                    }
                }
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .toAttrs {
                    attr("placeholder", "Type here...")
                }
        )
        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(
                    if (editorVisibility) Visibility.Hidden
                    else Visibility.Visible
                )
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        )
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: org.example.blogmultiplatform.models.Category,
    onCategorySelect: (org.example.blogmultiplatform.models.Category) -> Unit
) {
    Box(
        modifier = Modifier
            .margin(bottom = 12.px)
            .classNames("dropdown")
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.LightGray.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle", "dropdown")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(14.px)
                    .fontFamily(FONT_FAMILY),
                text = selectedCategory.name
            )
            Box(modifier = Modifier.classNames("dropdown-toggle"))
        }
        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.values().forEach { category ->
                Li {
                    A(
                        attrs = Modifier
                            .classNames("dropdown-item")
                            .color(Colors.Black)
                            .fontFamily(FONT_FAMILY)
                            .fontSize(14.px)
                            .onClick { onCategorySelect(category) }
                            .toAttrs()
                    ) {
                        Text(value = category.name)
                    }
                }
            }
        }
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String,
    thumbnailInputDisabled: Boolean,
    onThumbnailSelect: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
            .height(54.px)
    ) {
        Input(
            type = InputType.Text,
            attrs = Modifier
                .fillMaxSize()
                .id(Id.thumbnailInput)
                .margin(right = 12.px)
                .padding(leftRight = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .noBorder()

                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .thenIf(
                    condition = !thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs {
                    attr("placeholder", "Thumbnail")
                    attr("value", thumbnail)
                }
        )
        Button(
            attrs = Modifier
                .onClick {
                    document.loadDataUrlFromDisk(
                        accept = "image/png, image/jpeg",
                        onLoaded = {
                            onThumbnailSelect(filename, it)
                        }
                    )
                }
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(if (thumbnailInputDisabled) Theme.Gray.rgb else Theme.Primary.rgb)
                .color(if (thumbnailInputDisabled) Theme.DarkGray.rgb else Colors.White)
                .noBorder()
                .borderRadius(4.px)
                .fontFamily(FONT_FAMILY)
                .fontWeight(FontWeight.Medium)
                .fontSize(14.px)
                .thenIf(
                    condition = thumbnailInputDisabled,
                    other = Modifier.disabled()
                )
                .toAttrs()
        ) {
            SpanText(text = "Upload")
        }
    }
}

@Composable
fun CreateButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        attrs = Modifier
            .onClick { onClick() }
            .fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .backgroundColor(Theme.Primary.rgb)
            .color(Colors.White)
            .noBorder()
            .borderRadius(r = 4.px)
            .fontWeight(FontWeight.Medium)
            .fontFamily(FONT_FAMILY)
            .fontSize(14.px)
            .toAttrs()
    ) {
        SpanText(text = text)
    }
}



