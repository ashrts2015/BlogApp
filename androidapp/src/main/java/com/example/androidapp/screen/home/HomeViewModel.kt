package com.example.androidapp.screen.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.data.MongoSync
import com.example.androidapp.models.Post
import com.example.androidapp.util.Constants.APP_ID
import com.example.androidapp.util.RequestState
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _allPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts
    private val _searchedPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val searchedPosts: State<RequestState<List<Post>>> = _searchedPosts

    init {
        viewModelScope.launch {
            try {
                Log.d("Homeview","start")
                App.create(APP_ID).login(Credentials.anonymous())
                Log.d("Homeview","end")

            } catch (e: Exception) {
                Log.d("Homeview",e.message.toString())

            }
            fetchAllPosts()
        }
    }

    private fun fetchAllPosts() {
        Log.d("Homeview","posts")

        _allPosts.value=RequestState.Loading

        viewModelScope.launch {
            MongoSync.readAllPosts().collectLatest {
               if(it is RequestState.Success)
               {
                   val size=it.data.size
                   Log.d("Homeview",size.toString())

               }
                _allPosts.value = it
            }
        }
    }

    fun getPostsByTitle(query: String) {
        _searchedPosts.value=RequestState.Loading

        viewModelScope.launch {
            MongoSync.searchPostsByTitle(query).collectLatest {
                _searchedPosts.value = it
            }
        }
    }
     fun resetSearch() {
        viewModelScope.launch {
            _searchedPosts.value=RequestState.Idle
        }
    }
}