package com.aqrlei.open.todowanandroid.binding

import android.util.SparseArray
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author aqrlei on 2019/1/10
 */
interface OnItemBind<T> {
    fun onItemBind(itemBinding: ItemBinding<T>, position: Int, item: T)
}

class ItemBinding<T> private constructor() {
    companion object {
        fun <T> create(): ItemBinding<T> {
            return ItemBinding()
        }
    }

    var variableId: Int = -1
        private set
    var layoutRes: Int = -1
        private set

    private lateinit var extraBinding: SparseArray<Any>


    private val itemBindingList = ArrayList<OnItemBind<T>>()
    private val itemBindingClassList = ArrayList<Class<out T>>()
    fun set(variableId: Int, layoutRes: Int): ItemBinding<T> {
        this.variableId = variableId
        this.layoutRes = layoutRes
        return this
    }

    fun add(variableId: Int, layoutRes: Int, clazz: Class<out T>): ItemBinding<T> {
        itemBindingList.add(object : OnItemBind<T> {
            override fun onItemBind(itemBinding: ItemBinding<T>, position: Int, item: T) {
                itemBinding.set(variableId, layoutRes)
            }
        })
        itemBindingClassList.add(clazz)
        return this
    }

    fun itemBind(position: Int, item: T) {
        for (i in 0 until itemBindingClassList.size) {
            val key = itemBindingClassList[i]
            if (key.isInstance(item)) {
                itemBindingList[i].onItemBind(this, position, item)
            }
        }
    }

    fun bindExtra(variableId: Int, arg: Any): ItemBinding<T> {
        if (!::extraBinding.isInitialized) {
            extraBinding = SparseArray()
        }
        extraBinding.put(variableId, arg)
        return this
    }

    fun bind(binding: ViewDataBinding?, item: T): Boolean {
        return binding?.run {
            val result = setVariable(variableId, item)
            if (result) {
                if (::extraBinding.isInitialized) {
                    for (i in 0 until extraBinding.size()) {
                        setVariable(extraBinding.keyAt(i), extraBinding.valueAt(i))
                    }
                }
            }
            result
        } ?: false
    }
}

@BindingAdapter("android:itemBinding", "android:items", "android:layoutManager", requireAll = false)
fun <T> RecyclerView.setAdapter(
    itemBinding: ItemBinding<T>,
    items: List<T>,
    layoutManager: RecyclerView.LayoutManager?) {
    val oldAdapter = this.adapter as? DataBindingRecyclerAdapter<T>
    val adapter = oldAdapter ?: DataBindingRecyclerAdapter()
    adapter.setItemBind(itemBinding)
    adapter.setItems(items)
    this.itemAnimator = null
    this.layoutManager = layoutManager ?: LinearLayoutManager(this.context)
    if (adapter != oldAdapter) {
        this.adapter = adapter
    }
}