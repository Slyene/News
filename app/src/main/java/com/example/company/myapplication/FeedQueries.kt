package com.example.company.myapplication

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults

class FeedQueries {
    private var instance: Realm? = null

    fun save(feed: Feed): Completable = Completable.create {
        instance = Realm.getDefaultInstance()
        instance?.executeTransaction { realm ->
            realm.copyToRealm(feed)
        }
        it.onComplete()
        instance?.close()
        instance = null
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun delete(): Completable = Completable.create {
        lateinit var feed: RealmResults<Feed>

        instance = Realm.getDefaultInstance()
        try {
            instance?.executeTransaction { realm ->
                feed = realm.where(Feed::class.java).findAll()
                if (feed.size > 0)
                    for (item in feed) item.deleteFromRealm()
            }
            it.onComplete()
        } finally {
            instance?.close()
            instance = null
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun findAll(): Observable<RealmResults<Feed>> = Observable.create {
        lateinit var feed: RealmResults<Feed>

        instance = Realm.getDefaultInstance()

        instance?.executeTransaction { realm ->
            feed = realm.where(Feed::class.java).findAll()
        }
        it.onNext(feed)
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}