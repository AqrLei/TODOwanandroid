package com.aqrlei.app.open.todowanandroid.tasks.main

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.aqrlei.app.open.todowanandroid.R
import com.aqrlei.app.open.todowanandroid.databinding.ActModifyBinding
import com.aqrlei.app.open.todowanandroid.net.req.TodoReqBean
import com.aqrlei.app.open.todowanandroid.net.resp.todo.TodoRespBean
import com.aqrlei.app.open.todowanandroid.tasks.main.ItemModifyConstant.ITEM_CREATE
import com.aqrlei.app.open.todowanandroid.tasks.main.ItemModifyConstant.ITEM_LOOK
import com.aqrlei.app.open.todowanandroid.tasks.main.ItemModifyConstant.ITEM_UPDATE
import com.aqrlei.open.utils.IntentUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author aqrlei on 2019/2/11
 */
class ModifyTodoItemActivity : com.aqrlei.app.open.todowanandroid.base.ViewModelActivity<ModifyViewModel, ActModifyBinding>() {
    companion object {
        private const val DATA_KEY = "dataKey"
        private const val ITEM_PROCESS_KEY = "itemProcessKey"
        fun startForModify(frag: Fragment, data: TodoRespBean?) {
            startForResult(frag, data, ITEM_UPDATE)
        }

        fun startForLook(frag: Fragment, data: TodoRespBean?) {
            startForResult(frag, data, ITEM_LOOK)
        }

        fun startForCreate(frag: Fragment, data: TodoRespBean?) {
            startForResult(frag, data, ITEM_CREATE)
        }

        private fun startForResult(frag: Fragment, data: TodoRespBean?, reqCode: Int) {
            frag.context?.let { context ->
                val intent = Intent(context, ModifyTodoItemActivity::class.java)
                    .putExtra(DATA_KEY, data)
                    .putExtra(ITEM_PROCESS_KEY, reqCode)
                if (IntentUtil.queryActivities(context, intent)) {
                    if (reqCode == ITEM_LOOK) {
                        frag.startActivity(intent)
                    } else {
                        frag.startActivityForResult(intent, reqCode)
                    }
                }
            }

        }
    }

    val data: TodoReqBean
        get() {
            return intent.getParcelableExtra<TodoRespBean>(DATA_KEY)?.let {
                TodoReqBean(
                    it.id.orEmpty(),
                    it.title.orEmpty(),
                    it.content.orEmpty(),
                    it.date.orEmpty(),
                    it.dateStr.orEmpty(),
                    it.type.orEmpty(),
                    it.status.orEmpty()
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
                    dateStr = SimpleDateFormat("yyyy-MM-dd",Locale.ROOT).format(it)
                }
            }.show()
        }

        override fun save() {
            when (itemProcessKey) {
                ITEM_CREATE -> viewModel.add()
                ITEM_UPDATE -> viewModel.update()
                else -> showToast("Unknown error")
            }
        }

        override fun modifyDone() {
            setResult(Activity.RESULT_OK)
            this@ModifyTodoItemActivity.finish()
        }
    }
}