package com.aqrlei.open.todowanandroid.binding

import android.util.Log
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

/**
 * @author aqrlei on 2018/12/28
 */


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
fun TabLayout.onSelectionChanged(boolean: Boolean) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            Log.d("Test", "${tab?.position ?: 0}")
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

@BindingAdapter("android:refresh_action")
fun SwipeRefreshLayout.onRefresh(refreshAction: () -> Unit) {
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

@BindingAdapter("android:viewPager")
fun TabLayout.setViewPager(viewPager: ViewPager) {
    addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
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


