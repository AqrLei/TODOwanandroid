package com.aqrlei.open.todowanandroid.view

import android.app.Dialog
import android.content.Context
import android.view.View
import com.aqrlei.open.todowanandroid.R
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * @author aqrlei on 2019/1/8
 */
object DialogUtil {

    fun generateBottomDialog(context: Context, bindClickAction: () -> View) =
        BottomSheetDialog(context).apply {
            setContentView(bindClickAction.invoke())
        }
    fun generateFullScreenDialog(context:Context,bindClickAction: (Dialog) -> View) =
            Dialog(context, R.style.FullScreenDialogStyle).apply {
                setContentView(bindClickAction(this))
            }
}