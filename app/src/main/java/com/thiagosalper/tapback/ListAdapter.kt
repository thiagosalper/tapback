package com.thiagosalper.tapback

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.atalhos_list.view.*

class ListAdapter(private val atalhos: List<Atalhos>, private val context: Context): RecyclerView.Adapter<ListAdapter.ListHolder>() {

    class ListHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title = itemView.txtlabel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.atalhos_list, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount(): Int {
        return atalhos.size
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val item = atalhos[position]
        holder?.title.text = item.label
    }

}

