package com.example.kulmkapp.logic.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "taskInfo")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var description : String,
    var date : Date,
    var priority : Int,
    var status : Boolean,
    var category: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        Date(parcel.readLong()) ,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString()
    ) {
    }

    constructor(): this(0, "", Date(), 0, false, "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationEntity> {
        override fun createFromParcel(parcel: Parcel): NotificationEntity {
            return NotificationEntity(parcel)
        }

        override fun newArray(size: Int): Array<NotificationEntity?> {
            return arrayOfNulls(size)
        }
    }
}