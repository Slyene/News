package com.example.company.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.company.myapplication.databinding.ActivityMainFragmentBinding


class MainActivity : AppCompatActivity() {
    lateinit var b: ActivityMainFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainFragmentBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction().replace(R.id.fragment_place, MainFragment()).commitAllowingStateLoss()
        }
    }

    fun showArticle(url: String) {
        val bundle = Bundle()
        bundle.putString("url", url)
        val fragment = SecondFragment()
        fragment.arguments = bundle

        val frag2 = findViewById<View>(R.id.fragment_place2)
        if (frag2 == null)
            supportFragmentManager.beginTransaction().add(R.id.fragment_place, fragment).addToBackStack("").commitAllowingStateLoss()
        else {
            frag2.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.fragment_place2, fragment)
                .commitAllowingStateLoss()
        }
    }

    fun playMusic(url: String) {
        val i = Intent(this, PlayService::class.java)
        i.putExtra("mp3", url)
        startService(i)
    }
}