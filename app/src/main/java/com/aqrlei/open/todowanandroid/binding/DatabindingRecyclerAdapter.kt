package com.aqrlei.open.todowanandroid.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author aqrlei on 2019/1/10
 */

@BindingMethods(
    value = [
        BindingMethod(
            type = RecyclerView::class,
            attribute = "android:items",
            method = "setItems"),
    BindingMethod(
        type = RecyclerView::class,
        attribute = "android:itemBinding",
        method = "setItemBind"
    )])
class DatabindingRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var items: List<T>
    private lateinit var itemBinding:ItemBinding<T>
    override fun getItemViewType(position: Int): Int {
        itemBinding.itemBind(position,items[position])
        return itemBinding.layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutId, parent, false)
        return CommonViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
        itemBinding.bind(binding,items[position])
    }

    fun setItems(items: List<T>) {
        this.items = items
    }
    fun setItemBind(itemBinding:ItemBinding<T>){
        this.itemBinding = itemBinding
    }

    override fun getItemCount() = items.size

    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}