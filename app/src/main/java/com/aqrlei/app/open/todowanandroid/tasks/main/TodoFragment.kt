package com.aqrlei.app.open.todowanandroid.tasks.main

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.bindingadapter.bind.ItemBinding
import com.aqrlei.app.open.todowanandroid.BR
import com.aqrlei.app.open.todowanandroid.R
import com.aqrlei.app.open.todowanandroid.databinding.FragTodoBinding
import com.aqrlei.app.open.todowanandroid.net.resp.todo.TodoRespBean
import com.aqrlei.app.open.todowanandroid.tasks.main.ItemModifyConstant.ITEM_CREATE
import com.aqrlei.app.open.todowanandroid.tasks.main.ItemModifyConstant.ITEM_UPDATE
import com.aqrlei.open.utils.DialogUtil
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author aqrlei on 2019/1/7
 */
class TodoFragment : com.aqrlei.app.open.todowanandroid.base.ViewModelFragment<TodoViewModel, FragTodoBinding>() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    override val viewModel: TodoViewModel
        get() = ViewModelProviders.of(this).get(TodoViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_todo


    override fun initComponents(binding: FragTodoBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
        binding.itemBinding = ItemBinding.create<TodoRespBean>().set(BR.item, R.layout.list_item_todo)
            .bindExtra(BR.backgroundLevel, viewModel.itemLevel)
            .bindExtra(BR.viewModel, viewModel)
        val tabTitleList = this.resources.getStringArray(R.array.TodoTabTitle)
        viewModel.init(tabTitleList)
        binding.contentSRL.setColorSchemeResources(
            R.color.refresh_blue,
            R.color.refresh_green,
            R.color.refresh_yellow,
            R.color.refresh_red
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && (requestCode == ITEM_UPDATE || requestCode == ITEM_CREATE)) {
            viewModel.refreshAction.invoke()
        }
    }

    inner class Navigator : TodoViewModel.TodoNavigator {
        override fun showNoMoreData() {
            this@TodoFragment.context?.run {
                Snackbar.make(
                    binding.contentRv,
                    this.resources.getString(R.string.loadNoMoreData),
                    Snackbar.LENGTH_SHORT)
                    .addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            viewModel.loading = false
                        }
                    })
                    .show()
            }

        }

        override fun modifyItem(item: TodoRespBean?) {
            if (item?.status == "0") {
                ModifyTodoItemActivity.startForModify(this@TodoFragment, item)
            } else {
                ModifyTodoItemActivity.startForLook(this@TodoFragment, item)
            }
        }

        override fun manageItem(id: String): Boolean {
            this@TodoFragment.context?.run {
                val strArray = this.resources.getStringArray(R.array.TodoItemManage)
                DialogUtil.singleChoiceDialogBuilder(this, strArray, viewModel.itemChoicePos) {
                    if (it != 2 && it != viewModel.itemChoicePos) {
                        viewModel.updateStatus(id, it.toString())
                    } else DialogUtil.simpleDialogBuilder(this)
                        .setTitle(this.resources.getString(R.string.dialogTitle))
                        .setMessage(this.resources.getString(R.string.dialogMessage))
                        .setPositiveButton(this.resources.getString(R.string.dialogOk)) { _, _ ->
                            viewModel.delete(id)
                        }
                        .setNegativeButton(this.resources.getString(R.string.dialogCancel), null)
                        .show()
                }.show()
            }
            return true
        }

        override fun addNew(type: String) {
            val dateTime = Date().time
            val dateStr = SimpleDateFormat("yyyy-MM-dd",Locale.ROOT).format(dateTime)
            ModifyTodoItemActivity.startForCreate(
                this@TodoFragment, TodoRespBean(
                    type = viewModel.type.toString(),
                    date = dateTime.toString(),
                    dateStr = dateStr
                )
            )

        }
    }
}