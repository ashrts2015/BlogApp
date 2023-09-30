package com.example.androidapp.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class Post: RealmObject {
    @PrimaryKey
    var _id: String = ""
    var author: String = ""
    var date: Double = 0.0
    var title: String = ""
    var subtitle: String = ""
    var thumbnail: String = ""
    var category: String = Category.Programming.name
}
