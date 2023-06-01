package com.example.gtdproject.models

import android.os.Parcel
import android.os.Parcelable

data class Task (
    var id: String = "",
    val UserID: String = "",
    val status: String = "",
    val title: String = "",
    val entry: String = ""

) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(UserID)
        parcel.writeString(status)
        parcel.writeString(title)
        parcel.writeString(entry)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }

}