package com.aqrlei.open.todowanandroid.view

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * @author aqrlei on 2019/1/8
 */
object DialogUtil {

    fun generateBottomDialog(context: Context, bindClickAction: () -> View) =
        BottomSheetDialog(context).apply {
            setContentView(bindClickAction.invoke())
        }
}