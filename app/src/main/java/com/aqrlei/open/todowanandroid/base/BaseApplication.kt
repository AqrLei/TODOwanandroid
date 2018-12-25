package com.aqrlei.open.todowanandroid.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.aqrlei.open.utils.AppCache
import com.aqrlei.open.utils.ToastHelper

/**
 * @author aqrlei on 2018/12/24
 */
class BaseApplication : Application.ActivityLifecycleCallbacks, Application() {
    companion object {
        const val LOG_TAG = "TODO_WanAndroid"
        const val LEFT_ARROW = "<====="
        const val RIGHT_ARROW = "=====>"
    }


    override fun onCreate() {
        super.onCreate()
        ToastHelper.init(this)
        AppCache.init(this, "TODO_WanAndroid")
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onCreated $RIGHT_ARROW")
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onStarted $RIGHT_ARROW")
    }

    override fun onActivityResumed(activity: Activity?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onResumed $RIGHT_ARROW")
    }

    override fun onActivityPaused(activity: Activity?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onPaused $RIGHT_ARROW")
    }

    override fun onActivityStopped(activity: Activity?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onStopped $RIGHT_ARROW")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onSaveInstanceState $RIGHT_ARROW")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.d(LOG_TAG, " $LEFT_ARROW${activity?.javaClass?.simpleName.orEmpty()} is onDestroyed $RIGHT_ARROW")
    }
}