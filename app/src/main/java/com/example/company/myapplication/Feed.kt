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
    val author: String,
    val description: String,
    val enclosure: EnclosureItemAPI,
)

open class EnclosureItemAPI (
    var link: String = "", // ссылка на картинку
    var type: String = "",
    var length: Int = 0,
) : RealmObject()

open class Feed (
    var items: RealmList<FeedItem> = RealmList<FeedItem>()
) : RealmObject()

open class FeedItem(
    var title: String = "",
    var pubDate: String = "",
    var link: String = "",
    var author: String = "",
    var description: String = "",
    var enclosure: EnclosureItemAPI? = EnclosureItemAPI(),
) : RealmObject()

open class EnclosureItem (
    var link: String = "",
    var type: String = "",
    var length: Int = 0,
) : RealmObject()