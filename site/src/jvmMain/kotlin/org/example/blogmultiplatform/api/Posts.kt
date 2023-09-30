package org.example.blogmultiplatform.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.Request
import com.varabyte.kobweb.api.http.Response
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator
import org.example.blogmultiplatform.data.MongoDb
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.ApiResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.util.Constants.CATEGORY_PARAM
import org.example.blogmultiplatform.util.Constants.QUERY_PARAM
import org.example.blogmultiplatform.util.Constants.SKIP_PARAM

@Api(routeOverride = "addpost")
suspend fun addPosts(context: ApiContext) {
    try {
        val post = context.req.getBody<Post>()

        val newPost =
            post?.copy(_id = ObjectIdGenerator().generate().toString())
        context.res.setBody(newPost?.let { context.data.getValue<MongoDb>().addPost(it) })


    } catch (e: Exception) {
        context.res.setBody(e.message)

    }
}

@Api(routeOverride = "updatepost")
suspend fun updatePost(context: ApiContext) {
    try {
        val updatedPost = context.req.getBody<Post>()


        context.res.setBody(updatedPost?.let { context.data.getValue<MongoDb>().updatePost(it) })


    } catch (e: Exception) {
        context.res.setBody(e.message)

    }
}


@Api(routeOverride = "readmyposts")
suspend fun readMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val author = context.req.params["author"] ?: ""

        val result = context.data.getValue<MongoDb>().readMyPosts(skip, author)
        context.res.setBody(ApiListResponse.Success(data = result))


    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))

    }
}

@Api(routeOverride = "readmainposts")
suspend fun readMainPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDb>().readMainPosts()
        context.res.setBody(ApiListResponse.Success(data = result))
    } catch (e: Exception) {
        println(e.message + "Posts")
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = "readlatestposts")
suspend fun readLatestPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0

        val result = context.data.getValue<MongoDb>().readLatestPosts(skip)
        context.res.setBody(ApiListResponse.Success(data = result))
    } catch (e: Exception) {
        println(e.message + "Posts")
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = "readpopularposts")
suspend fun readPopularPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0

        val result = context.data.getValue<MongoDb>().readPopularPosts(skip)
        context.res.setBody(ApiListResponse.Success(data = result))
    } catch (e: Exception) {
        println(e.message + "Posts")
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = "readsponsoredposts")
suspend fun readSponsoredPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDb>().readSponsoredPosts()
        context.res.setBody(ApiListResponse.Success(data = result))
    } catch (e: Exception) {
        println(e.message + "Posts")
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = "deleteselectedposts")
suspend fun deleteSelectedPosts(context: ApiContext) {
    try {

        val request = context.req.getBody<List<String>>()
        context.res.setBody(request?.let {
            context.data.getValue<MongoDb>().deleteSelectedPosts(it)
        })

    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))

    }
}


@Api(routeOverride = "searchposts")
suspend fun searchPostByTitle(context: ApiContext) {
    try {
        val query = context.req.params[QUERY_PARAM] ?: ""
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val posts = context.data.getValue<MongoDb>().searchPostsByTittle(
            query = query, skip = skip
        )

        context.res.setBody(ApiListResponse.Success(posts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = "searchpostsbycategory")
suspend fun searchPostByCategory(context: ApiContext) {
    try {
        val category =
            Category.valueOf(context.req.params[CATEGORY_PARAM] ?: Category.Programming.name)
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val posts = context.data.getValue<MongoDb>().searchPostsByCategory(

            category = category, skip = skip
        )

        context.res.setBody(ApiListResponse.Success(posts))
    } catch (e: Exception) {
        context.res.setBody(ApiListResponse.Error(message = e.message.toString()))
    }
}

@Api(routeOverride = "readselectedposts")
suspend fun readSelectedPost(context: ApiContext) {
    val postId = context.req.params["postId"]
    if (!postId.isNullOrEmpty()) {
        try {
            val selectedPost = context.data.getValue<MongoDb>().readSelectedPost(id = postId)
            context.res.setBody(ApiResponse.Success(selectedPost))
        } catch (e: Exception) {
            context.res.setBody(ApiResponse.Error(e.message.toString()))

        }
    } else {
        //context.res.setBody(ApiResponse.Error("Selected Post doesnot exits")))
        context.res.setBody(ApiResponse.Error("Selected Post doesn't exits"))
    }

}

inline fun <reified T> Response.setBody(data: T) {
    setBodyText(Json.encodeToString(data))
}

inline fun <reified T> Request.getBody(): T? {
    return body?.decodeToString()?.let { return Json.decodeFromString(it) }
}