package com.aqrlei.open.todowanandroid.tasks.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.CacheConst
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelFragment
import com.aqrlei.open.todowanandroid.databinding.FragMeBinding

/**
 * @author aqrlei on 2019/1/7
 */
class MeFragment : ViewModelFragment<MeViewModel, FragMeBinding>() {

    companion object {
        fun newInstance(userName:String):MeFragment{
           return MeFragment().apply {
                arguments = Bundle().also { it.putString(CacheConst.USER_NAME_KEY,userName) }
            }
        }
    }

    override val viewModel: MeViewModel
        get() = ViewModelProviders.of(this).get(MeViewModel::class.java)

    override fun bindLayout(): Int = R.layout.frag_me

    override fun initComponents(binding: FragMeBinding) {
        binding.viewModel = viewModel
    }
}