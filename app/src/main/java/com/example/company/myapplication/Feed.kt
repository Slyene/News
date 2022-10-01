package com.example.company.myapplication

import io.realm.RealmList
import io.realm.RealmObject

class FeedAPI (
    val items: ArrayList<FeedItemAPI>
    )

class FeedItemAPI (
    val title: String,
    val pubDate: String,
    val link: String,
    val thumbnail: String,
    val description: String,
    val guid: String,
)

open class Feed (
    var items: RealmList<FeedItem> = RealmList<FeedItem>()
) : RealmObject()

open class FeedItem(
    var title: String = "",
    var pubDate: String = "",
    var link: String = "",
    var thumbnail: String = "",
    var description: String = "",
    var guid: String = "",
) : RealmObject()