package org.example.blogmultiplatform.util

import com.example.blogmultiplatform.models.Newsletter
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.compose.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.ApiResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.RandomJoke
import org.example.blogmultiplatform.models.User
import org.example.blogmultiplatform.models.UserWithoutPassword
import org.example.blogmultiplatform.util.Constants.AUTHOR_PARAM
import org.example.blogmultiplatform.util.Constants.POST_ID_PARAM
import org.example.blogmultiplatform.util.Constants.QUERY_PARAM
import org.example.blogmultiplatform.util.Constants.SKIP_PARAM
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

suspend fun checkUserExistence(user: User): UserWithoutPassword? {
    return try {
        window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )?.decodeToString().parseData()


    } catch (e: Exception) {
        println(e.message)
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(id).encodeToByteArray()
        )?.decodeToString().parseData()


    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun addPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "addpost",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()

    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun updatePost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "updatepost",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString().toBoolean()

    } catch (e: Exception) {
        println(e.message)
        false
    }
}


suspend fun fetchMyPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readmyposts?${SKIP_PARAM}=$skip&${AUTHOR_PARAM}=${localStorage["userName"]}"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        onError(e)
        print(e.message.toString())

    }

}

suspend fun fetchMainPosts(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readmainposts"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        onError(e)
        print(e.message.toString())

    }

}

suspend fun fetchSponsoredPosts(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readsponsoredposts"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        onError(e)
        print(e.message.toString())

    }

}

suspend fun fetchLatestPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readlatestposts?skip=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        onError(e)
        print(e.message.toString())

    }

}

suspend fun fetchPopularPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readpopularposts?skip=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        onError(e)
        print(e.message.toString())

    }

}

suspend fun searchPostsByTitle(
    query: String,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "searchposts?${QUERY_PARAM}=$query&${SKIP_PARAM}=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }
}

suspend fun searchPostsByCategory(
    category: Category, skip: Int, onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "searchpostsbycategory?category=${category.name}&${SKIP_PARAM}=$skip"
        )?.decodeToString()
        onSuccess(result.parseData())
    } catch (e: Exception) {
        println(e.message)
        onError(e)
    }


}

suspend fun fetchRandomJoke(onComplete: (RandomJoke) -> Unit) {
    val date = localStorage["date"]
    if (date != null) {
        val difference = (Date.now() - date.toDouble())
        val daypassed = difference >= 86400000
        if (daypassed) {
            try {
                val result = window.http.get(Constants.HUMOUR_API_URL).decodeToString()
                onComplete(result.parseData())
                localStorage["date"] = Date.now().toString()
                localStorage["joke"] = result

            } catch (e: Exception) {
                println(e.message.toString())
                onComplete(RandomJoke(id = -1, joke = e.message.toString()))

            }


        } else {
            try {
                localStorage["joke"]?.parseData<RandomJoke>()?.let {
                    onComplete(it)
                }

            } catch (e: Exception) {
                println(e.message.toString())
                onComplete(RandomJoke(id = -1, joke = e.message.toString()))

            }

        }

    } else {
        try {
            val result = window.http.get(Constants.HUMOUR_API_URL).decodeToString()
            onComplete(result.parseData())
            localStorage["date"] = Date.now().toString()
            localStorage["joke"] = result

        } catch (e: Exception) {
            println(e.message.toString())
            onComplete(RandomJoke(id = -1, joke = e.message.toString()))
        }

    }
}

suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
    return try {
        val result = window.api.tryPost(
            apiPath = "deleteselectedposts",
            body = Json.encodeToString(ids).encodeToByteArray()
        )?.decodeToString()
        return result.toBoolean()
    } catch (e: Exception) {
        println(e.message)
        false
    }

}

suspend fun fetchSelectedPosts(id: String): ApiResponse {
    return try {

        val result = window.api.tryGet(
            apiPath = "readselectedposts?${POST_ID_PARAM}=$id",
        )?.decodeToString()

        result?.parseData() ?: ApiResponse.Error(message = "Result is Null")

    } catch (e: Exception) {
        ApiResponse.Error(e.message.toString())
    }

}


suspend fun subscribeToNewsletter(newsletter: Newsletter): String {
    return window.api.tryPost(
        apiPath = "subscribe",
        body = Json.encodeToString(newsletter).encodeToByteArray()
    )?.decodeToString().toString().replace("\"", "")
}


inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString(this.toString())
}