package com.gyanhub.newsapp.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gyanhub.newsapp.R

class CategoryAdapter (private val catList:Array<String>, private val click:SelectCategory,private var _selectedPosition : Int = RecyclerView.NO_POSITION):
RecyclerView.Adapter<CategoryAdapter.CatViewHolder>(){

     var selectedPosition = _selectedPosition


    inner class CatViewHolder(view:View):RecyclerView.ViewHolder(view){
        val catTxt: TextView = view.findViewById(R.id.txtCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_card, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = catList[position]
        holder.catTxt.text = item
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.GRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        // handle item click events
        holder.itemView.setOnClickListener {
            click.selectCategoy(item,position)
            if (selectedPosition != position) {
                val prevSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(prevSelectedPosition)
                notifyItemChanged(selectedPosition)
            }
        }

    }

    override fun getItemCount(): Int {
        return catList.size
    }
}

interface SelectCategory{
    fun selectCategoy(text: String,position: Int)
}