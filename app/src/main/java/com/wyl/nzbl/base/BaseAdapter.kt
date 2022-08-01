package com.wyl.nzbl.base

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import cn.jpush.android.api.JPushInterface

abstract class BaseAdapter<D>(
    val context: Context,
    val data: List<D>,
    val layouts: SparseArray<Int>,
    val itemClick: BaseItemClick<D>
) : RecyclerView.Adapter<BaseAdapter<D>.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        var dataBinding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), viewType, parent, false)
        return VH(dataBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val type = layouts.get(getItemViewType(position))
        holder.dataBinding.setVariable(type, data[position])
        holder.dataBinding.root.tag = data[position]
        holder.dataBinding.root.setOnClickListener {
            itemClick?.itemClick(data[position])
        }
        bindData(holder.dataBinding, data[position], type)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId(position)
    }

    protected abstract fun layoutId(position: Int): Int
    protected abstract fun bindData(dataBinding: ViewDataBinding, data: D, layout: Int)

    inner class VH(val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root)
}