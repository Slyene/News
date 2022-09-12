package com.example.company.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.realm.RealmList

class RecAdapter(val items: RealmList<FeedItem>) : RecyclerView.Adapter<RecHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)

        return RecHolder(view)
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {
        val item = items[position]!!

        holder?.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class RecHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: FeedItem) {
        val vTitle = itemView.findViewById<TextView>(R.id.item_title)
        val vDesc = itemView.findViewById<TextView>(R.id.item_description)
        val vThumb = itemView.findViewById<ImageView>(R.id.item_thumbnail)
        val vPubTime = itemView.findViewById<TextView>(R.id.item_pubTime)
        vTitle.text = item.title
        vDesc.text = item.description
        vPubTime.text = item.pubDate

        try {
            Picasso.get().load(item.enclosure?.link).error(R.drawable.default_img).into(vThumb)
        } catch (e: Exception) {
            Picasso.get().load(R.drawable.default_img).into(vThumb)
        }

        itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(item.link)
            vThumb.context.startActivity(intent)
        }
    }
}
