package com.aqrlei.open.todowanandroid.tasks.main

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aqrlei.open.bindingadapter.bind.ItemBinding
import com.aqrlei.open.todowanandroid.BR
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelFragment
import com.aqrlei.open.todowanandroid.databinding.FragTodoBinding
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean

/**
 * @author aqrlei on 2019/1/7
 */
class TodoFragment : ViewModelFragment<TodoViewModel, FragTodoBinding>() {

    companion object {
        fun newInstance() = TodoFragment()
    }

    val addNewPos: Int
        get() = (binding.contentRv.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: 0


    override val viewModel: TodoViewModel
        get() = ViewModelProviders.of(this).get(TodoViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_todo


    override fun initComponents(binding: FragTodoBinding) {
        viewModel.navigator = Navigator()
        binding.viewModel = viewModel
        binding.itemBinding = ItemBinding.create<TodoRespBean>().set(BR.item, R.layout.list_item_todo)
            .bindExtra(BR.backgroundLevel,viewModel.itemLevel)
        viewModel.init()
        binding.contentSRL.setColorSchemeResources(
            R.color.refresh_blue,
            R.color.refresh_green,
            R.color.refresh_yellow,
            R.color.refresh_red
        )
    }

    inner class Navigator : TodoViewModel.TodoNavigator {
        override fun addNew() {
            viewModel.addContent(addNewPos)
        }
    }
}