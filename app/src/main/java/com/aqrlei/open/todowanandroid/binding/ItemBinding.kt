package com.aqrlei.open.todowanandroid.binding

import android.util.SparseArray
import androidx.databinding.ViewDataBinding

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


    val itemBindingList = ArrayList<OnItemBind<T>>()
    val itemBindingClassList = ArrayList<Class<out T>>()
    fun set(variableId: Int, layoutRes: Int) {
        this.variableId = variableId
        this.layoutRes = layoutRes
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