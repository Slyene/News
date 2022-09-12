package com.example.company.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.company.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.RealmList

class MainActivity : AppCompatActivity() {
    lateinit var b: ActivityMainBinding
    lateinit var act1RecView: RecyclerView
    var request: Disposable? = null
    var feedRequest: Disposable? = null
    var deleteRequest: Disposable? = null
    var saveRequest: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        act1RecView = b.act1RecView

        val o =
            createRequest("https://api.rss2json.com/v1/api.json?rss_url=https%3A%2F%2Flenta.ru%2Frss%2Farticles")
                .map { Gson().fromJson(it, FeedAPI::class.java) }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = o.subscribe({
            val feed = Feed(it.items.mapTo(RealmList<FeedItem>()) { feed ->
                FeedItem(
                    feed.title,
                    feed.pubDate,
                    feed.link,
                    feed.author,
                    feed.description,
                    feed.enclosure,
                )
            })

            deleteRequest = FeedQueries.delete().subscribe({
                FeedQueries.save(feed)
                showRecView()
            }, { deleteError ->
                Toast.makeText(this, deleteError.message, Toast.LENGTH_LONG).show()
            })

        }, {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            showRecView()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.dispose()
        feedRequest?.dispose()
        saveRequest?.dispose()
    }

    private fun showRecView() {
        feedRequest = FeedQueries.findAll().subscribe() {
            if (it.isNotEmpty()) {
                act1RecView.adapter = RecAdapter(it[0]!!.items)
                act1RecView.layoutManager = LinearLayoutManager(this)
            }
        }
    }
}

//"title": "Украинский десант попытался высадиться в Запорожской области"
//"pubDate": "2022-09-09 07:26:00"
//"link": "https://lenta.ru/news/2022/09/09/ukrodesant/"
//"guid": "https://lenta.ru/news/2022/09/09/ukrodesant/"
//"author": "Василий Мека"
//"thumbnail": ""
//"description": "Вооруженные силы Украины (ВСУ) попытались высадить десант через Днепр в Запорожской области в районе Каменки-Днепровской, Вооруженные силы России отразили эту атаку, сообщил член администрации региона Владимир Рогов. Он также отметил, что таким образом ВСУ пытаются прощупать оборону союзных сил."
//"content": "Вооруженные силы Украины (ВСУ) попытались высадить десант через Днепр в Запорожской области в районе Каменки-Днепровской, Вооруженные силы России отразили эту атаку, сообщил член администрации региона Владимир Рогов. Он также отметил, что таким образом ВСУ пытаются прощупать оборону союзных сил."
//"enclosure": {
//    "link": "https://icdn.lenta.ru/images/2022/09/09/10/20220909101932289/pic_3797bfa5acac7872a31834b4221d0706.jpeg"
//    "type": "image/jpeg"
//    "length": 45551
//}
//"categories": [
//"Бывший СССР"
//]