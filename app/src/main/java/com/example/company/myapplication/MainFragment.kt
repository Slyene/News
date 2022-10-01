package com.example.company.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.company.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults

class MainFragment : Fragment() {
    lateinit var b: ActivityMainBinding
    var act1RecView: RecyclerView? = null
    lateinit var feedQueries: FeedQueries
    var request: Disposable? = null
    var feedRequest: Disposable? = null
    var deleteRequest: Disposable? = null
    var saveRequest: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        feedQueries = FeedQueries()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            act1RecView = requireView().findViewById(R.id.act1_RecView)
        } catch (e: Exception) {
            Log.e("view", e.message.toString())
        }


        val o =
            createRequest("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.twit.tv%2Fbrickhouse.xml")
                .map { Gson().fromJson(it, FeedAPI::class.java) }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = o.subscribe({
            val feed = Feed(it.items.mapTo(RealmList<FeedItem>()) { feed ->
                FeedItem(
                    feed.title,
                    feed.pubDate,
                    feed.link,
                    feed.thumbnail,
                    feed.description,
                    feed.guid,
                )
            })

            deleteRequest = feedQueries.delete().subscribe({
                try {
                    val instance = Realm.getDefaultInstance()
                    instance.executeTransactionAsync { realm ->
                        realm.copyToRealm(feed)
                        activity?.runOnUiThread {
                            showRecView()
                        } ?: Log.e("activity", "activity is null")
                    }
                    instance.close()
                } catch (e: Exception) {
                    Log.e("save", e.message.toString())
                }
            }, { deleteError ->
                activity ?: Log.e("activity", "activity is null")
                Toast.makeText(activity, "1 " + deleteError.message, Toast.LENGTH_LONG).show()
            })

        }, {
            Toast.makeText(activity, "2 " + it.message, Toast.LENGTH_LONG).show()
            showRecView()
        })
    }

    private fun showRecView() {
        val instance = Realm.getDefaultInstance()
        var feed: RealmResults<Feed> = instance.where(Feed::class.java).findAll()

        if (feed.size > 0) {
            act1RecView?.adapter = RecAdapter(feed[0]!!.items)
            activity ?: Log.e("show", "activity is null")
            act1RecView?.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        request?.dispose()
        feedRequest?.dispose()
        saveRequest?.dispose()
    }
}