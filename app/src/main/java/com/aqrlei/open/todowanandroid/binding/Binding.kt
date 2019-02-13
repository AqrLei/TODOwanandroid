package com.aqrlei.open.todowanandroid.binding

import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.aqrlei.open.bindingadapter.adapter.DataBindingPagingAdapter
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


@BindingAdapter("android:onPagingRefresh")
fun RecyclerView.onPageRefresh(refreshEvent: Boolean?) {
    if (refreshEvent == true) {
        (this.adapter as? DataBindingPagingAdapter<*>)?.let { adapter ->
            (this.context as? LifecycleOwner)?.let { lifecycleOwner ->
                adapter.refresh(lifecycleOwner)
            }
        }
    }
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


