package com.aqrlei.open.todowanandroid.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * @author aqrlei on 2018/12/28
 */

@BindingAdapter("android:errorMsg")
fun TextInputLayout.setErrorMsg(errorMsg: String?) {
    this.error = errorMsg
}