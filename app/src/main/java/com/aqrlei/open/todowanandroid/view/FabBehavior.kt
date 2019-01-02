package com.aqrlei.open.todowanandroid.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author aqrlei on 2019/1/2
 */
class FabBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context, attrs) {
    companion object {

        private val INTERPOLATOR = FastOutSlowInInterpolator()
    }

    private var viewY: Float = 0F
    private var animate: Boolean = false
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int): Boolean {
        if (child.visibility == View.VISIBLE && viewY == 0F) {
            viewY = coordinatorLayout.height - child.y
        }

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int) {
        if (dy > 0 && !animate && child.visibility == View.VISIBLE) {
            hide(child)
        } else if (dy < 0 && !animate && child.visibility == View.INVISIBLE) {
            show(child)
        }

    }

    private fun show(view: View) {
        view.animate().translationY(0F)
            .setInterpolator(INTERPOLATOR)
            .setDuration(300L)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animate = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    hide(view)
                }

                override fun onAnimationRepeat(animation: Animator?) {}
            })
            .start()
    }

    private fun hide(view: View) {
        view.animate().translationY(viewY)
            .setInterpolator(INTERPOLATOR)
            .setDuration(300L)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    animate = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.INVISIBLE
                    animate = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    show(view)
                }

                override fun onAnimationRepeat(animation: Animator?) {}
            })
            .start()
    }
}