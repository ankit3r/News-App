package com.gyanhub.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gyanhub.newsapp.R
import com.gyanhub.newsapp.models.Article


class ArticleAdapter(val article:List<Article>, val context: Context,val click:ReadMore):RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtDate: TextView =view.findViewById(R.id.txtArcDate)
        val txtAuthor: TextView =view.findViewById(R.id.txtArcAuthor)
        val txtTitle: TextView =view.findViewById(R.id.txtArcTitle)
        val txtDis: TextView =view.findViewById(R.id.txtArcDis)
        val imgArt : ImageView = view.findViewById(R.id.artImg)
        val btnReadMore : Button = view.findViewById(R.id.btnReadMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_card, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = article[position]
        holder.txtAuthor.text = item.author
        holder.txtDate.text = item.publishedAt
        holder.txtTitle.text = item.title
        holder.txtDis.text = item.description
        holder.btnReadMore.setOnClickListener {
            click.readMore(item.url,item.title)
        }
        Glide.with(context)
            .load(item.urlToImage)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_newspaper_24)
            .into(holder.imgArt)
    }

    override fun getItemCount(): Int {
       return article.size
    }
}
interface ReadMore{
    fun readMore(url:String,title:String)
}