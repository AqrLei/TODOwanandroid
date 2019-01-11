package com.aqrlei.open.todowanandroid.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * @author aqrlei on 2019/1/10
 */
class DataBindingRecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val DATA_INVALIDATION = Any()
    }

    private var items: List<T>? = null
    private lateinit var itemBinding: ItemBinding<T>

    private val callback = WeakReferenceOnListChangedCallback(this)

    private var containerView: RecyclerView? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (containerView == null) {
            (items as? ObservableList<T>)?.addOnListChangedCallback(callback)

        }
        containerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (containerView != null) {
            (items as? ObservableList<T>)?.removeOnListChangedCallback(callback)
        }
        containerView = null
    }

    override fun getItemViewType(position: Int): Int {
        items?.run {
            itemBinding.itemBind(position, this[position])
        }
        return itemBinding.layoutRes
    }

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): RecyclerView.ViewHolder {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutId, parent, false)
        val holder = CommonViewHolder(binding.root)
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {

            override fun onPreBind(binding: ViewDataBinding): Boolean {
                return containerView?.isComputingLayout == true
            }

            override fun onCanceled(binding: ViewDataBinding) {
                if (containerView == null || containerView?.isComputingLayout == true) {
                    return
                }
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION)
                }
            }
        })
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
        items?.run {
            if (itemBinding.bind(binding, this[position])) {
                binding?.executePendingBindings()
            }
        }

    }

    fun setItems(items: List<T>) {
        if (this.items == items) {
            return
        }
        if (containerView != null) {
            (this.items as? ObservableList<T>)?.removeOnListChangedCallback(callback)
            (items as? ObservableList<T>)?.addOnListChangedCallback(callback)
        }
        this.items = items
        notifyDataSetChanged()
    }


    fun getItem(position: Int): T? {
        return items?.get(position)
    }

    fun setItemBind(itemBinding: ItemBinding<T>) {
        this.itemBinding = itemBinding
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class WeakReferenceOnListChangedCallback<T> internal constructor(adapter: DataBindingRecyclerAdapter<T>) :
        ObservableList.OnListChangedCallback<ObservableList<T>>() {
        internal val adapterRef: WeakReference<DataBindingRecyclerAdapter<T>> = WeakReference(adapter)

        override fun onChanged(sender: ObservableList<T>?) {
            val adapter = adapterRef.get() ?: return
            //  Utils.ensureChangeOnMainThread()
            adapter.notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            val adapter = adapterRef.get() ?: return
            //  Utils.ensureChangeOnMainThread()
            adapter.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            val adapter = adapterRef.get() ?: return
            //   Utils.ensureChangeOnMainThread()
            adapter.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            val adapter = adapterRef.get() ?: return
            //  Utils.ensureChangeOnMainThread()
            for (i in 0 until itemCount) {
                adapter.notifyItemMoved(fromPosition + i, toPosition + i)
            }
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            val adapter = adapterRef.get() ?: return
            //   Utils.ensureChangeOnMainThread()
            adapter.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }
}