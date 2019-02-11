package com.aqrlei.open.todowanandroid.net.resp.todo

import android.os.Parcel
import android.os.Parcelable
import com.aqrlei.open.views.KParcelable
import com.aqrlei.open.views.generateCreator

/**
 * @author aqrlei on 2018/12/25
 */
data class TodoRespBean(
    val id: String? = "",
    val userId: String? = "",
    val type: String? = "",
    val status: String? = "",
    var title: String? = "",
    var content: String? = "",
    val date: String? = "",
    val dateStr: String? = "",
    val completeDate: String? = "",
    val completeDateStr: String? = ""
) : KParcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(userId)
        dest.writeString(type)
        dest.writeString(status)
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(date)
        dest.writeString(dateStr)
        dest.writeString(completeDate)
        dest.writeString(completeDateStr)
    }

    companion object {
        @JvmField
        var CREATOR: Parcelable.Creator<TodoRespBean> = generateCreator(::TodoRespBean)
    }


}