package com.aqrlei.open.todowanandroid.tasks.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.bindingadapter.bind.ItemBinding
import com.aqrlei.open.todowanandroid.BR
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelFragment
import com.aqrlei.open.todowanandroid.databinding.FragTodoBinding
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean
import com.aqrlei.open.utils.DialogUtil

/**
 * @author aqrlei on 2019/1/7
 */
class TodoFragment : ViewModelFragment<TodoViewModel, FragTodoBinding>() {

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
        viewModel.init()
        binding.contentSRL.setColorSchemeResources(
            R.color.refresh_blue,
            R.color.refresh_green,
            R.color.refresh_yellow,
            R.color.refresh_red
        )
    }

    inner class Navigator : TodoViewModel.TodoNavigator {
        override fun modifyItem(item: TodoRespBean?) {
            (this@TodoFragment.context as? AppCompatActivity)?.run {
                ModifyTodoItemActivity.startForModify(this, item)
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
            (this@TodoFragment.context as? AppCompatActivity)?.run {
                ModifyTodoItemActivity.startForCreate(this)
            }
        }
    }
}