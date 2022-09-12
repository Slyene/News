package com.example.company.myapplication

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults

class FeedQueries {
    companion object {
        val instance = Realm.getDefaultInstance()

        fun save(feed: Feed): Observable<Unit> = Observable.create {
            instance.executeTransaction { realm ->
                realm.copyToRealm(feed)
            }
        }

        fun delete(): Completable = Completable.create {
            findAll().map { feed ->
                if (feed.size > 0)
                    for (item in feed) item.deleteFromRealm()
            }
            it.onComplete()
        }

        fun findAll(): Observable<RealmResults<Feed>> = Observable.create {
            lateinit var feed: RealmResults<Feed>

            instance.executeTransaction { realm ->
                feed = realm.where(Feed::class.java).findAll()
            }

            it.onNext(feed)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}