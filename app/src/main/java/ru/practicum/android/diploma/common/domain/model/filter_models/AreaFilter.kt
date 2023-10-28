package ru.practicum.android.diploma.common.domain.model.filter_models

import android.os.Parcel
import android.os.Parcelable

data class AreaFilter(
    var id: Int,
    val name: String,
    val countryName: String,
    val countryId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(countryName)
        parcel.writeInt(countryId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AreaFilter> {
        override fun createFromParcel(parcel: Parcel): AreaFilter = AreaFilter(parcel)

        override fun newArray(size: Int): Array<AreaFilter?> = arrayOfNulls(size)
    }
}
