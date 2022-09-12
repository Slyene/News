package com.example.company.myapplication

import io.reactivex.rxjava3.core.Observable
import java.net.HttpURLConnection
import java.net.URL

fun createRequest(url: String): Observable<String> = Observable.create {
    val urlCon = URL(url).openConnection() as HttpURLConnection
    urlCon.connectTimeout = 1000
    try {
        urlCon.connect()

        if (urlCon.responseCode != HttpURLConnection.HTTP_OK)
            it.onError(RuntimeException(urlCon.responseMessage))
        else {
            val str = urlCon.inputStream.bufferedReader().readText()
            it.onNext(str)
        }
    } finally {
        urlCon.disconnect()
    }
}