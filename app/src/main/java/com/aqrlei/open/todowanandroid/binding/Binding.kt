package com.aqrlei.open.todowanandroid.binding

import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

/**
 * @author aqrlei on 2018/12/28
 */


@BindingAdapter("android:backgroundLevel")
fun ViewGroup.setBackgroundLevel(level: Int) {
    this.background.level = level
}

@BindingAdapter("android:onCheckedChanged")
fun RadioGroup.onCheckedChanged(onCheckedAction: (Int) -> Unit) {
    this.setOnCheckedChangeListener { group, checkedId ->
        for (position in 0 until group.childCount) {
            if (group.getChildAt(position).id == checkedId) {
                onCheckedAction(position)
            }
        }
    }
}

@BindingAdapter("android:selectionChanged")
fun TabLayout.onSelectionChanged(onSelectionChangeAction: (Int) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            onSelectionChangeAction(tab?.position ?: 0)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

    })

}

@BindingAdapter("android:refresh_in_progress")
fun SwipeRefreshLayout.setRefresh(refresh: Boolean?) {
    refresh?.let {
        this.isRefreshing = it
    }

}

@BindingAdapter("android:refreshAction")
fun SwipeRefreshLayout.setRefreshAction(refreshAction: () -> Unit) {
    this.setOnRefreshListener(refreshAction)
}

@BindingAdapter("android:loadAction")
fun RecyclerView.setLoadAction(loadAction: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                (recyclerView.layoutManager as? LinearLayoutManager)?.run {
                    if (itemCount == (findLastVisibleItemPosition() + 1)) {
                        loadAction.invoke()
                    }
                }
            }
        }
    })
}

/**
 * TextInputLayout
 * */
@BindingAdapter("android:errorMsg")
fun TextInputLayout.setErrorMsg(errorMsg: String?) {
    this.error = errorMsg
}

/**
 * TabLayout
 * */
@BindingAdapter("android:titles")
fun TabLayout.setTitles(titles: List<String>) {
    titles.forEach { addTab(newTab().setText(it)) }
}

@BindingAdapter("android:nav_item_checked")
fun RadioGroup.setOnNavItemChecked(action: (Int) -> Unit) {
    this.setOnCheckedChangeListener { group, checkedId ->
        for (i in 0 until group.childCount) {
            if (group.getChildAt(i).id == checkedId) {
                action(i)
            }
        }
    }
}