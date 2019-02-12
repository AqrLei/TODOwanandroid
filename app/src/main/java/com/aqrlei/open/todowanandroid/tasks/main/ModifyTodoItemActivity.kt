package com.aqrlei.open.todowanandroid.tasks.main

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.open.todowanandroid.R
import com.aqrlei.open.todowanandroid.base.ViewModelActivity
import com.aqrlei.open.todowanandroid.databinding.ActModifyBinding
import com.aqrlei.open.todowanandroid.net.req.TodoReqBean
import com.aqrlei.open.todowanandroid.net.resp.todo.TodoRespBean
import com.aqrlei.open.utils.IntentUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author aqrlei on 2019/2/11
 */
class ModifyTodoItemActivity : ViewModelActivity<ModifyViewModel, ActModifyBinding>() {
    companion object {
        private const val DATA_KEY = "dataKey"
        private const val ITEM_PROCESS_KEY = "itemProcessKey"
        private const val ITEM_MODIFY = 11
        private const val ITEM_CREATE = 10
        private const val ITEM_LOOK = 100
        fun startForModify(context: AppCompatActivity, data: TodoRespBean?) {
            startForResult(context, data, ITEM_MODIFY)
        }

        fun startForLook(context: AppCompatActivity, data: TodoRespBean?) {
            startForResult(context, data, ITEM_LOOK)
        }

        fun startForCreate(context: AppCompatActivity, data: TodoRespBean?) {
            startForResult(context, data, ITEM_CREATE)
        }


        private fun startForResult(context: AppCompatActivity, data: TodoRespBean?, reqCode: Int) {
            val intent = Intent(context, ModifyTodoItemActivity::class.java)
                .putExtra(DATA_KEY, data)
                .putExtra(ITEM_PROCESS_KEY, reqCode)
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
    val itemProcessKey: Int
        get() = intent.getIntExtra(ITEM_PROCESS_KEY, ITEM_LOOK)
    override val viewModel: ModifyViewModel
        get() = ViewModelProviders.of(this).get(ModifyViewModel::class.java)

    override fun bindLayout(): Int = R.layout.act_modify

    override fun initComponents(binding: ActModifyBinding) {
        viewModel.navigator = Navigator()
        viewModel.editEnable = itemProcessKey != ITEM_LOOK
        viewModel.item.value = data
        binding.viewModel = viewModel
        bindTitleToolbar(binding.titleToolBar)
    }

    fun dataPickerDialog(
        context: Context,
        action: (Long) -> Unit
    ): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(context, { _, year, month, dayOfMonth ->
            calendar?.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            action(calendar.timeInMillis)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    inner class Navigator : ModifyViewModel.ModifyNavigator, CommonNavigator() {
        override fun markDate() {
            dataPickerDialog(this@ModifyTodoItemActivity) {
                viewModel.item.value = viewModel.item.value?.apply {
                    date = it.toString()
                    dateStr = SimpleDateFormat("yyyy-MM-dd").format(it)
                }
            }.show()
        }

        override fun save() {
            when (itemProcessKey) {
                ITEM_CREATE -> viewModel.add()
                ITEM_MODIFY -> viewModel.update()
                else -> showToast("Unknown error")
            }
        }
    }

}
