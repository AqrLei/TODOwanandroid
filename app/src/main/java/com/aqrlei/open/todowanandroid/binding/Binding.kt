package com.aqrlei.open.todowanandroid.binding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout

/**
 * @author aqrlei on 2018/12/28
 */

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