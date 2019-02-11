package com.aqrlei.open.todowanandroid.tasks.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActModifyBinding
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean
import com.aqrlei.open.utils.IntentUtil

/**
 * @author aqrlei on 2019/2/11
 */
class ModifyTodoItemActivity : ViewModelActivity<TodoViewModel, ActModifyBinding>() {
    companion object {
        private const val DATA_KEY = "data_key"
        private const val ITEM_MODIFY = 11
        private const val ITEM_CREATE = 10
        fun startForModify(context: AppCompatActivity, data: TodoRespBean?) {
            startForResult(context, data, ITEM_MODIFY)
        }

        fun startForCreate(context: AppCompatActivity) {
            startForResult(context, null, ITEM_CREATE)
        }

        private fun startForResult(context: AppCompatActivity, data: TodoRespBean?, reqCode: Int) {
            val intent = Intent(context, ModifyTodoItemActivity::class.java)
                .putExtra(DATA_KEY, data)
            if (IntentUtil.queryActivities(context, intent)) {
                context.startActivityForResult(intent, reqCode)
            }
        }

    }


    val data: TodoReqBean
        get() {
            return intent.getParcelableExtra<TodoRespBean>(DATA_KEY)?.let {
                TodoReqBean(
                    it.id,
                    it.title,
                    it.content,
                    it.date,
                    it.dateStr,
                    it.type,
                    it.status
                )
            } ?: TodoReqBean()
        }

    override val viewModel: TodoViewModel
        get() = ViewModelProviders.of(this).get(TodoViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_modify

    override fun initComponents(binding: ActModifyBinding) {
        binding.item = data
    }

}
