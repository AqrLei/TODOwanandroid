package com.aqrlei.open.bindingadapter.item

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * @author aqrlei on 2019/1/11
 * copy from DefaultItemAnimator
 */
class AlphaItemAnimator : SimpleItemAnimator() {
    companion object {
        private val defaultInterpolator = ValueAnimator().interpolator
    }

    private class ChangeInfo private constructor(
        var oldHolder: RecyclerView.ViewHolder?,
        var newHolder: RecyclerView.ViewHolder?
    ) {
        var fromX: Int = 0
        var fromY: Int = 0
        var toX: Int = 0
        var toY: Int = 0

        internal constructor(
            oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder?,
            fromX: Int, fromY: Int, toX: Int, toY: Int
        ) : this(oldHolder, newHolder) {
            this.fromX = fromX
            this.fromY = fromY
            this.toX = toX
            this.toY = toY
        }
    }

    private data class MoveInfo(
        var holder: RecyclerView.ViewHolder,
        var fromX: Int,
        var fromY: Int,
        var toX: Int,
        var toY: Int
    )


    private val movesList = ArrayList<ArrayList<MoveInfo>>()
    private val changesList = ArrayList<ArrayList<ChangeInfo>>()
    private val additionsList = ArrayList<ArrayList<RecyclerView.ViewHolder>>()
    override fun runPendingAnimations() {
        val removalsPending = pendingRemovals.isNotEmpty()
        val movesPending = pendingMoves.isNotEmpty()
        val changesPending = pendingChanges.isNotEmpty()
        val additionsPending = pendingAdditions.isNotEmpty()
        if (!removalsPending && !movesPending && !changesPending && !additionsPending) {
            return
        }
        for (holder in pendingRemovals) {
            animateRemoveImpl(holder)
        }
        pendingRemovals.clear()
        if (movesPending) {
            val moves = ArrayList<MoveInfo>()
            moves.addAll(pendingMoves)
            movesList.add(moves)
            pendingMoves.clear()
            val mover = Runnable {
                for (moveInfo in moves) {
                    animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY)
                }
                moves.clear()
                movesList.remove(moves)
            }
            if (removalsPending) {
                val view = moves[0].holder.itemView
                ViewCompat.postOnAnimationDelayed(view, mover, removeDuration)
            } else {
                mover.run()
            }
        }

        if (changesPending) {
            val changes = ArrayList<ChangeInfo>()
            changes.addAll(pendingChanges)
            changesList.add(changes)
            val changer = Runnable {
                for (change in changes) {
                    animateChangeImpl(change)
                }
                changes.clear()
                changesList.remove(changes)
            }
            if (removalsPending) {
                changes[0].oldHolder?.let {
                    ViewCompat.postOnAnimationDelayed(it.itemView, changer, removeDuration)
                }

            } else {
                changer.run()
            }
        }
        if (additionsPending) {
            val additions = ArrayList<RecyclerView.ViewHolder>()
            additions.addAll(pendingAdditions)
            additionsList.add(additions)
            pendingAdditions.clear()
            val adder = Runnable {
                for (holder in additions) {
                    animateAddImpl(holder)
                }
                additions.clear()
                additionsList.remove(additions)
            }
            if (removalsPending || movesPending || changesPending) {
                val rDuration = if (removalsPending) removeDuration else 0
                val mDuration = if (movesPending) moveDuration else 0
                val cDuration = if (changesPending) changeDuration else 0
                val totalDelay = rDuration + Math.max(mDuration, cDuration)
                val view = additions[0].itemView
                ViewCompat.postOnAnimationDelayed(view, adder, totalDelay)

            } else {
                adder.run()
            }
        }
    }

    private val pendingRemovals = ArrayList<RecyclerView.ViewHolder>()
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        resetAnimation(holder)
        pendingRemovals.add(holder)
        return true
    }

    private val removeAnimations = ArrayList<RecyclerView.ViewHolder>()
    private fun animateRemoveImpl(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView
        val animation = view.animate()
        removeAnimations.add(holder)
        animation?.apply {
            duration = removeDuration
            alpha(0f)
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator?) {
                    dispatchRemoveStarting(holder)
                }

                override fun onAnimationEnd(animator: Animator?) {
                    animation.setListener(null)
                    view.alpha = 1F
                    dispatchRemoveFinished(holder)
                    removeAnimations.remove(holder)
                    dispatchFinishedWhenDone()
                }
            })
            start()
        }
    }

    private val pendingAdditions = ArrayList<RecyclerView.ViewHolder>()
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemId
        resetAnimation(holder)
        holder.itemView.alpha = 0F
        pendingAdditions.add(holder)
        return true
    }

    private val addAnimations = ArrayList<RecyclerView.ViewHolder>()
    private fun animateAddImpl(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView
        val animation = view.animate()
        addAnimations.add(holder)
        animation?.apply {
            alpha(1F)
            duration = addDuration
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator?) {
                    dispatchAddStarting(holder)
                }

                override fun onAnimationCancel(animator: Animator?) {
                    view.alpha = 1F
                }

                override fun onAnimationEnd(animator: Animator?) {
                    animation.setListener(null)
                    dispatchAddFinished(holder)
                    addAnimations.remove(holder)
                    dispatchFinishedWhenDone()
                }
            })
            start()
        }

    }


    private val pendingMoves = ArrayList<MoveInfo>()
    override fun animateMove(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        holder.itemView.apply {
            val fX = (translationX + fromX).toInt()
            val fY = (translationY + fromY).toInt()
            resetAnimation(holder)
            val deltaX = toX - fX
            val deltaY = toY - fY
            if (deltaX == 0 && deltaY == 0) {
                dispatchMoveFinished(holder)
                return false
            }
            if (deltaX != 0) {
                translationX = -deltaX.toFloat()
            }
            if (deltaY != 0) {
                translationY = -deltaY.toFloat()
            }
            pendingMoves.add(MoveInfo(holder, fX, fY, toX, toY))
        }
        return true
    }

    private val moveAnimations = ArrayList<RecyclerView.ViewHolder>()
    private fun animateMoveImpl(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val view = holder.itemView
        val deltaX = toX - fromX
        val deltaY = toY - fromY
        if (deltaX != 0) {
            view.animate().translationX(0F)
        }
        if (deltaY != 0) {
            view.animate().translationY(0F)
        }
        val animation = view.animate()
        moveAnimations.add(holder)
        animation?.apply {
            duration = moveDuration
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animator: Animator?) {
                    dispatchMoveStarting(holder)
                }

                override fun onAnimationCancel(animator: Animator?) {
                    if (deltaX != 0) {
                        view.translationX = 0F
                    }
                    if (deltaY != 0) {
                        view.translationY = 0F
                    }
                }

                override fun onAnimationEnd(animator: Animator?) {
                    animation.setListener(null)
                    dispatchMoveFinished(holder)
                    moveAnimations.remove(holder)
                    dispatchFinishedWhenDone()
                }
            })
            start()
        }
    }

    private val pendingChanges = ArrayList<ChangeInfo>()
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        if (oldHolder == newHolder) {
            return animateMove(oldHolder, fromLeft, fromTop, toLeft, toTop)
        }
        oldHolder.itemView.apply {
            val prevX = translationX
            val prevY = translationY
            val prevAlpha = alpha
            resetAnimation(oldHolder)
            val deltaX = (toLeft - fromLeft - prevX).toInt()
            val deltaY = (toTop - fromTop - prevY).toInt()
            translationX = prevX
            translationY = prevY
            alpha = prevAlpha
            if (newHolder != null) {
                resetAnimation(newHolder)
                newHolder.itemView.translationX = -deltaX.toFloat()
                newHolder.itemView.translationY = -deltaY.toFloat()
                newHolder.itemView.alpha = 0F

            }
            pendingChanges.add(ChangeInfo(oldHolder, newHolder, fromLeft, fromTop, toLeft, toTop))
        }
        return true
    }


    private val changeAnimations = ArrayList<RecyclerView.ViewHolder>()
    private fun animateChangeImpl(changeInfo: ChangeInfo) {
        val holder = changeInfo.oldHolder
        val view = holder?.itemView
        val newHolder = changeInfo.newHolder
        val newView = newHolder?.itemView
        if (view != null) {
            val oldViewAnim = view.animate().setDuration(changeDuration)
            changeAnimations.add(changeInfo.oldHolder!!)
            oldViewAnim?.apply {
                translationX((changeInfo.toX - changeInfo.fromX).toFloat())
                translationY((changeInfo.toY - changeInfo.fromY).toFloat())
                alpha(0F)
                setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        dispatchChangeStarting(changeInfo.oldHolder, true)
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        oldViewAnim.setListener(null)
                        view.alpha = 0F
                        view.translationX = 0F
                        view.translationY = 0F
                        dispatchChangeFinished(changeInfo.oldHolder, true)
                        changeAnimations.remove(changeInfo.oldHolder!!)
                        dispatchFinishedWhenDone()
                    }
                })
                start()
            }
        }
        if (newView != null) {
            val newViewAnim = newView.animate()
            changeAnimations.add(changeInfo.newHolder!!)
            newViewAnim?.apply {
                translationX(0F)
                translationY(0F)
                duration = changeDuration
                alpha(1F)
                setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        dispatchChangeStarting(changeInfo.newHolder, false)
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        newViewAnim.setListener(null)
                        newView.alpha = 1F
                        newView.translationX = 0F
                        newView.translationY = 0F
                        dispatchChangeFinished(changeInfo.newHolder, false)
                        changeAnimations.remove(changeInfo.newHolder!!)
                        dispatchFinishedWhenDone()
                    }
                })
                start()
            }
        }

    }

    override fun isRunning(): Boolean {
        return (pendingAdditions.isNotEmpty()
                || pendingMoves.isNotEmpty()
                || pendingRemovals.isNotEmpty()
                || pendingChanges.isNotEmpty()
                || moveAnimations.isNotEmpty()
                || addAnimations.isNotEmpty()
                || removeAnimations.isNotEmpty()
                || changeAnimations.isNotEmpty()
                || movesList.isNotEmpty()
                || additionsList.isNotEmpty()
                || changesList.isNotEmpty())
    }

    fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    private fun resetAnimation(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate()?.interpolator = defaultInterpolator
        endAnimation(holder)
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        item.itemView.apply {
            animate().cancel()
            for (i in pendingMoves.indices.reversed()) {
                val moveInfo = pendingMoves[i]
                if (moveInfo.holder == item) {
                    translationX = 0F
                    translationY = 0F
                    dispatchMoveFinished(item)
                    pendingMoves.removeAt(i)
                }
            }
            endChangeAnimation(pendingChanges, item)
            if (pendingRemovals.remove(item)) {
                alpha = 1F
                dispatchRemoveFinished(item)
            }
            if (pendingAdditions.remove(item)) {
                alpha = 1F
                dispatchAddFinished(item)
            }
            for (i in changesList.indices.reversed()) {
                val changes = changesList[i]
                endChangeAnimation(changes, item)
                if (changes.isEmpty()) {
                    changesList.removeAt(i)
                }
            }
            for (i in movesList.indices.reversed()) {
                val moves = movesList[i]
                for (j in moves.indices.reversed()) {
                    val moveInfo = moves[j]
                    if (moveInfo.holder === item) {
                        translationY = 0f
                        translationX = 0f
                        dispatchMoveFinished(item)
                        moves.removeAt(j)
                        if (moves.isEmpty()) {
                            movesList.removeAt(i)
                        }
                        break
                    }
                }
            }
            for (i in additionsList.indices.reversed()) {
                val additions = additionsList[i]
                if (additions.remove(item)) {
                    alpha = 1f
                    dispatchAddFinished(item)
                    if (additions.isEmpty()) {
                        additionsList.removeAt(i)
                    }
                }
            }
            dispatchFinishedWhenDone()
        }
    }

    private fun endChangeAnimation(infoList: ArrayList<ChangeInfo>, item: RecyclerView.ViewHolder) {
        for (i in infoList.indices.reversed()) {
            val changeInfo = infoList[i]
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    infoList.remove(changeInfo)
                }
            }
        }
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo) {
        if (changeInfo.oldHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder!!)
        }
        if (changeInfo.newHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder!!)
        }
    }

    private fun endChangeAnimationIfNecessary(changeInfo: ChangeInfo, item: RecyclerView.ViewHolder): Boolean {
        var oldItem = false
        when {
            changeInfo.newHolder === item -> changeInfo.newHolder = null
            changeInfo.oldHolder === item -> {
                changeInfo.oldHolder = null
                oldItem = true
            }
            else -> return false
        }
        item.itemView.alpha = 1f
        item.itemView.translationX = 0f
        item.itemView.translationY = 0f
        dispatchChangeFinished(item, oldItem)
        return true
    }

    override fun endAnimations() {
        for (i in pendingMoves.indices.reversed()) {
            val item = pendingMoves[i]
            val view = item.holder.itemView
            view.translationY = 0f
            view.translationX = 0f
            dispatchMoveFinished(item.holder)
            pendingMoves.removeAt(i)
        }
        for (i in pendingRemovals.indices.reversed()) {
            val item = pendingRemovals[i]
            dispatchRemoveFinished(item)
            pendingRemovals.removeAt(i)
        }

        for (i in pendingAdditions.indices.reversed()) {
            val item = pendingAdditions[i]
            item.itemView.alpha = 1f
            dispatchAddFinished(item)
            pendingAdditions.removeAt(i)
        }
        for (i in pendingChanges.indices.reversed()) {
            endChangeAnimationIfNecessary(pendingChanges[i])
        }
        pendingChanges.clear()
        if (!isRunning) {
            return
        }
        for (i in movesList.indices.reversed()) {
            val moves = movesList[i]
            for (j in moves.indices.reversed()) {
                val moveInfo = moves[j]
                val item = moveInfo.holder
                val view = item.itemView
                view.translationY = 0f
                view.translationX = 0f
                dispatchMoveFinished(moveInfo.holder)
                moves.removeAt(j)
                if (moves.isEmpty()) {
                    movesList.remove(moves)
                }
            }
        }
        for (i in additionsList.indices.reversed()) {
            val additions = additionsList[i]
            for (j in additions.indices.reversed()) {
                val item = additions[j]
                val view = item.itemView
                view.alpha = 1f
                dispatchAddFinished(item)
                additions.removeAt(j)
                if (additions.isEmpty()) {
                    additionsList.remove(additions)
                }
            }
        }
        for (i in changesList.indices.reversed()) {
            val changes = changesList[i]
            for (j in changes.indices.reversed()) {
                endChangeAnimationIfNecessary(changes[j])
                if (changes.isEmpty()) {
                    changesList.remove(changes)
                }
            }
        }

        cancelAll(removeAnimations)
        cancelAll(moveAnimations)
        cancelAll(addAnimations)
        cancelAll(changeAnimations)

        dispatchAnimationsFinished()

    }

    private fun cancelAll(holders: List<RecyclerView.ViewHolder>) {
        for (i in holders.indices.reversed()) {
            holders[i].itemView.animate().cancel()
        }
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>): Boolean {
        return payloads.isNotEmpty() || super.canReuseUpdatedViewHolder(viewHolder, payloads)
    }
}