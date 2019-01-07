package com.aqrlei.open.todowanandroid.tasks.todo

import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelFragment
import com.aqrlei.open.todowanandroid.databinding.FragMeBinding
import com.aqrlei.open.todowanandroid.databinding.FragTodoBinding

/**
 * @author aqrlei on 2019/1/7
 */
class MeFragment : ViewModelFragment<MeViewModel, FragMeBinding>() {

    companion object {
        fun newInstance() = MeFragment()
    }

    override val viewModel: MeViewModel
        get() = ViewModelProviders.of(this).get(MeViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_todo

    override fun initComponents(binding: FragMeBinding) {
        binding.viewModel = viewModel
    }
}