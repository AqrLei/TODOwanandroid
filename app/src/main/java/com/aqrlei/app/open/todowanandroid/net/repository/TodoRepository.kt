package com.aqrlei.app.open.todowanandroid.net.repository

import com.aqrlei.open.retrofit.livedatacalladapter.LiveObservable
import com.aqrlei.open.retrofit.livedatacalladapter.LiveResponse
import com.aqrlei.app.open.todowanandroid.net.NetHelper
import com.aqrlei.app.open.todowanandroid.net.req.TodoReqBean
import com.aqrlei.app.open.todowanandroid.net.resp.BaseRespBean
import com.aqrlei.app.open.todowanandroid.net.resp.PaginationRespBean
import com.aqrlei.app.open.todowanandroid.net.resp.todo.TodoRespBean
import com.aqrlei.app.open.todowanandroid.net.resp.todo.TodoTypeRespBean
import retrofit2.http.*

/**
 * @author aqrlei on 2018/12/25
 */
class TodoRepository {
    private val todoService = NetHelper.get().createService(TodoService::class.java)

    @Deprecated("")
    fun fetchTypeList(type: String) = todoService.fetchTypeList(type)

    fun fetchDoneList(type: String, pageNum: String) = todoService.fetchDoneList(type, pageNum)

    fun fetchNotDoList(type: String, pageNum: String) = todoService.fetchNotDoList(type, pageNum)

    fun updateStatus(id: String, status: String) = todoService.updateStatus(id, status)

    fun delete(id: String) = todoService.delete(id)

    fun updateContent(id: String, data: TodoReqBean) = todoService.updateContent(id, HashMap<String, String>().apply {
        put("title", data.title)
        put("content", data.content)
        put("date", data.dateStr)
        put("status", data.status)
        put("type", data.type)
    })

    fun addNew(data: TodoReqBean) = todoService.addNew(HashMap<String, String>().apply {
        put("title", data.title)
        put("content", data.content)
        put("date", data.dateStr)
        put("type", data.type)
    })


    interface TodoService {
        @GET("lg/todo/list/{type}/json")
        fun fetchTypeList(@Path("type") type: String): LiveObservable<LiveResponse<BaseRespBean<TodoTypeRespBean>>>

        @POST("lg/todo/listdone/{type}/json/{page}")
        fun fetchDoneList(
            @Path("type") type: String,
            @Path("page") pageNum: String
        ): LiveObservable<LiveResponse<BaseRespBean<PaginationRespBean<TodoRespBean>>>>

        @POST("lg/todo/listnotdo/{type}/json/{page}")
        fun fetchNotDoList(
            @Path("type") type: String,
            @Path("page") pageNum: String
        ): LiveObservable<LiveResponse<BaseRespBean<PaginationRespBean<TodoRespBean>>>>

        @FormUrlEncoded
        @POST("lg/todo/done/{id}/json")
        fun updateStatus(
            @Path("id") id: String,
            @Field("status") status: String
        ): LiveObservable<LiveResponse<BaseRespBean<Any>>>

        @POST("lg/todo/delete/{id}/json")
        fun delete(@Path("id") id: String): LiveObservable<LiveResponse<BaseRespBean<Any>>>

        @FormUrlEncoded
        @POST("lg/todo/update/{id}/json")
        fun updateContent(
            @Path("id") id: String,
            @FieldMap map: MutableMap<String, String>
        ): LiveObservable<LiveResponse<BaseRespBean<Any>>>

        @FormUrlEncoded
        @POST("lg/todo/add/json")
        fun addNew(
            @FieldMap map: MutableMap<String, String>
        ): LiveObservable<LiveResponse<BaseRespBean<Any>>>
    }
}