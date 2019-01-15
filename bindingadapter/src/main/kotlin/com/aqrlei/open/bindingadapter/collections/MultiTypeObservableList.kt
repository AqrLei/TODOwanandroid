package com.aqrlei.open.bindingadapter.collections

import androidx.databinding.ListChangeRegistry
import androidx.databinding.ObservableList
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author aqrlei on 2019/1/14
 * Collections.singletonList 只有一个item时的内存优化"
 * modCount 在对集合进行迭代时，同时进行结构性修改，根据此值“modCount”抛出异常ConcurrentModificationException
 */
class MultiTypeObservableList<T> : AbstractList<T>(), ObservableList<T> {
    private val listeners = ListChangeRegistry()
    private val callback = ListChangeCallBack<T>()
    private val lists: ArrayList<List<T>> = ArrayList()

    fun insertItem(any: T): MultiTypeObservableList<T> {
        lists.add(listOf(any))
        modCount++
        listeners.notifyInserted(this, size, 1)
        return this
    }

    fun insertList(list: ObservableList<T>): MultiTypeObservableList<T> {
        list.addOnListChangedCallback(callback)
        val oldSize = size
        lists.add(list)
        if (list.isNotEmpty()) {
            listeners.notifyInserted(this, oldSize, list.size)
        }
        return this
    }

    fun removeItem(any: T): Boolean {
        var tempSize = 0
        for (list in lists) {
            if (list !is ObservableList) {
                val item = list[0]
                if (item == any) {
                    lists.remove(list)
                    modCount++
                    listeners.notifyRemoved(this, tempSize, 1)
                    return true
                }
            }
            tempSize += list.size
        }
        return false
    }

    fun removeList(listItem: ObservableList<T>): Boolean {
        var tempSize = 0
        for (list in lists) {
            if (list == listItem) {
                listItem.removeOnListChangedCallback(callback)
                lists.remove(list)
                modCount++
                listeners.notifyRemoved(this, tempSize, list.size)
                return true
            }
            tempSize += list.size
        }
        return false
    }

    fun removeAll() {
        val tempSize = size
        if (size == 0) return
        for (list in lists) {
            if (list is ObservableList) {
                list.removeOnListChangedCallback(callback)
            }
        }
        lists.clear()
        modCount++
        listeners.notifyRemoved(this, 0, size)
    }

    override fun removeOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.remove(callback)
    }

    override fun addOnListChangedCallback(callback: ObservableList.OnListChangedCallback<out ObservableList<T>>?) {
        listeners.add(callback)
    }

    override val size: Int
        get() {
            var tempSize = 0
            for (list in lists) {
                tempSize += list.size
            }
            return tempSize
        }

    override fun get(index: Int): T {
        if (index < 0) {
            throw IndexOutOfBoundsException()
        }
        var tempSize = 0
        for (list in lists) {
            if (index - tempSize < list.size) {
                return list[index - tempSize]
            }
            tempSize += list.size
        }
        throw java.lang.IndexOutOfBoundsException()
    }

    inner class ListChangeCallBack<T> : ObservableList.OnListChangedCallback<ObservableList<T>>() {
        override fun onChanged(sender: ObservableList<T>?) {
            modCount++
            listeners.notifyChanged(this@MultiTypeObservableList)
        }

        override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            modCount++
            var tempSize = 0
            for (list in lists) {
                if (list == sender) {
                    listeners.notifyRemoved(this@MultiTypeObservableList, tempSize + positionStart, itemCount)
                    return
                }
                tempSize += list.size
            }
        }

        override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            var tempSize = 0
            for (list in lists) {
                if (list == sender) {
                    listeners.notifyMoved(
                        this@MultiTypeObservableList,
                        tempSize + fromPosition,
                        tempSize + toPosition,
                        itemCount
                    )
                    return
                }
                tempSize += list.size
            }
        }

        override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            modCount++
            var tempSize = 0
            for (list in lists) {
                if (list == sender) {
                    listeners.notifyInserted(this@MultiTypeObservableList, tempSize + positionStart, itemCount)
                    return
                }
                tempSize += list.size
            }
        }

        override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
            var tempSize = 0
            for (list in lists) {
                if (list == sender) {
                    listeners.notifyChanged(this@MultiTypeObservableList, tempSize + positionStart, itemCount)
                    return
                }
                tempSize += list.size
            }
        }
    }
}