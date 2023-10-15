package com.example.vtb.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vtb.R
import com.example.vtb.databinding.ItemListBinding
import com.example.vtb.interfaces.ListenerDepartment
import com.example.vtb.models.DepartmentWithTime
import java.time.format.DateTimeFormatter

class ResultRecyclerAdaptor(
    private val isMan: Boolean,
    private val listenerClick: ListenerDepartment,
): RecyclerView.Adapter<ResultRecyclerAdaptor.ResultViewHolder>() {

    private val listAdapter = mutableListOf<DepartmentWithTime>()

    class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val item = ItemListBinding.bind(view)
        fun bind(department: DepartmentWithTime, isMan: Boolean, listenerClick: ListenerDepartment) {
            item.ivAvatar.setImageResource(if (isMan) R.drawable.man_item_image else R.drawable.car_item_image)
            item.nameStreet.text = department.address
            item.durection.text = department.time
            itemView.setOnClickListener {
                listenerClick.onClick(department, isMan)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(listAdapter[position], isMan, listenerClick)
    }

    override fun getItemCount(): Int = listAdapter.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<DepartmentWithTime>) {
        listAdapter.clear()
        listAdapter.addAll(list)
        notifyDataSetChanged()
    }

}