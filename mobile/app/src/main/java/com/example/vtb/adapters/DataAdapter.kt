package com.example.vtb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vtb.R
import com.example.vtb.databinding.ItemDataBinding

class DataAdapter(val list: List<String>): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item = ItemDataBinding.bind(view)
        fun bind(data: String) {
            item.time.text = data
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return DataViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(list[position])
    }
}