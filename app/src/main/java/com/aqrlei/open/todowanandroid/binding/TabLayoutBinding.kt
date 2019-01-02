package com.aqrlei.open.todowanandroid.binding

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * @author aqrlei on 2019/1/2
 */
@BindingAdapter("android:titles")
fun TabLayout.setTitles(titles: List<String>) {
    titles.forEach { addTab(newTab().setText(it)) }
}

@BindingAdapter("android:viewPager")
fun TabLayout.setViewPager(viewPager: ViewPager) {
    addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
}